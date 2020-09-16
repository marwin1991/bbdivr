package pl.marwin1991.bbdivr.anchore.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.marwin1991.bbdivr.anchore.converter.AnchoreScanResultConverter;
import pl.marwin1991.bbdivr.anchore.model.*;
import pl.marwin1991.bbdivr.model.ScanResult;
import pl.marwin1991.bbdivr.model.SumScanResult;
import pl.marwin1991.bbdivr.service.LayerAnalyseService;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AnchoreLayerAnalyseService implements LayerAnalyseService {

    private static final String LAYER_PATH = "/images";
    private static final String BY_ID = "/by_id";
    private static final String METADATA_MANIFEST_PATH = "/metadata/manifest";
    private static final String VUL = "/vuln/all";
    private static final String SERVER_URL = "http://localhost:8228";


    private final AnchoreScanResultConverter converter;
    private final AnchoreAuthService authService;
    private final ObjectMapper objectMapper;

    @Autowired
    public AnchoreLayerAnalyseService(AnchoreScanResultConverter converter, AnchoreAuthService anchoreAuthService, ObjectMapper objectMapper) {
        this.converter = converter;
        this.authService = anchoreAuthService;
        this.objectMapper = objectMapper;
    }

    @Override
    public ScanResult analyse(String imageName, List<String> layerIds) {
        log.info("Start ANCHORE analysing " + imageName);
        String imageId = postImage(imageName);
        String imageDigest = finishedAnalyseImage(imageId);
        List<String> ids = getLayersIds(imageDigest);
        return converter.convert(fetchVulnerabilities(Collections.singletonList(imageDigest)));
    }

    @Override
    public SumScanResult analyseAndSum(String imageName, List<String> layerIds) {
        log.info("Start ANCHORE analysing " + imageName);
        String imageId = postImage(imageName);
        String imageDigest = finishedAnalyseImage(imageId);
        List<String> ids = getLayersIds(imageDigest);
        return converter.convertToSum(fetchVulnerabilities(Collections.singletonList(imageDigest)));
    }

    private List<VulnerabilityList> fetchVulnerabilities(List<String> layerIds) {
        List<VulnerabilityList> vulnerabilityLists = new LinkedList<>();

        for (String id : layerIds) {
            VulnerabilityList list = new RestTemplate().exchange(
                    SERVER_URL + LAYER_PATH + "/" + id + VUL,
                    HttpMethod.GET,
                    authService.addAuthHeader(null),
                    VulnerabilityList.class).getBody();

            vulnerabilityLists.add(list);
        }

        return vulnerabilityLists;
    }

    private String postImage(String imageName) {
        try {
            AnchoreImageScanRequest request = AnchoreImageScanRequest.builder()
                    .imageType("docker")
                    .source(ImageSource.builder()
                            .tag(RegistryTagSource.builder()
                                    .pullstring(imageName)
                                    .dockerfile(null)
                                    .build())
                            .build())
                    .build();
            AnchoreImageList response = new RestTemplate().postForObject(SERVER_URL + LAYER_PATH, authService.addAuthHeader(request), AnchoreImageList.class);
            return response.get(0).getImageDetail().get(0).getImageId();
        } catch (Exception e) {
            log.error("Could not send image info to anchore", e);
            throw e;
        }
    }

    @SneakyThrows
    private String finishedAnalyseImage(String imageId) {
        AnchoreImage.AnalysisStatusEnum status = AnchoreImage.AnalysisStatusEnum.NOT_ANALYZED;
        while (true) {
            log.info("Anchore Engine Analysing....");
            ResponseEntity<AnchoreImageList> response = new RestTemplate().exchange(SERVER_URL + LAYER_PATH + BY_ID + "/" + imageId, HttpMethod.GET, authService.addAuthHeader(null), AnchoreImageList.class);

            status = response.getBody().get(0).getAnalysisStatus();

            if (AnchoreImage.AnalysisStatusEnum.NOT_ANALYZED.equals(status)) {
                Thread.sleep(10000);
            } else {
                log.info("Finished Anchore Engine Analysing....");
                return response.getBody().get(0).getImageDigest();
            }
        }
    }

    @SneakyThrows
    private List<String> getLayersIds(String imageDigest) {
        ResponseEntity<AnchoreManifestResponse> response =
                new RestTemplate()
                        .exchange(SERVER_URL + LAYER_PATH + "/" + imageDigest + METADATA_MANIFEST_PATH,
                                HttpMethod.GET,
                                authService.addAuthHeader(null),
                                AnchoreManifestResponse.class);


        String[] manifests = response.getBody().getMetadata().split("\n");
        String content = Arrays.stream(manifests).map(m -> new String(Base64.getDecoder().decode(m), StandardCharsets.UTF_8)).collect(Collectors.joining());
        AnchoreManifest manifest = objectMapper.readValue(content, new TypeReference<AnchoreManifest>() {
        });
        List<String> ids = manifest
                .getLayers()
                .stream()
                .map(AnchoreLayerInfo::getDigest)
                .collect(Collectors.toList());
        ids.add(0, manifest.getConfig().getDigest());
        return ids;
    }

}
