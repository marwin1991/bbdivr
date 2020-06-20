package pl.marwin1991.bbdivr.clair.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.marwin1991.bbdivr.clair.converter.ScanResultConverter;
import pl.marwin1991.bbdivr.clair.model.ClairLayerScanData;
import pl.marwin1991.bbdivr.clair.model.ClairLayerScanRequest;
import pl.marwin1991.bbdivr.clair.model.ClairLayerScanResponse;
import pl.marwin1991.bbdivr.model.ScanResult;
import pl.marwin1991.bbdivr.service.LayerAnalyseService;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class ClairLayerAnalyseService implements LayerAnalyseService {

    private static final String POST_LAYER_URI = "/v1/layers";
    private static final String CLAIR_SERVER_URL = "http://localhost:6060";
    private static final String GET_LAYER_FEATURES_URI = "/v1/layers/%s?features&vulnerabilities";
    private static final String LOCAL_URI_TO_GET_LAYER = "http://host.docker.internal:8080/layers/";


    private final ScanResultConverter converter;

    @Autowired
    public ClairLayerAnalyseService(ScanResultConverter converter) {
        this.converter = converter;
    }

    @Override
    public ScanResult analyse(List<String> layerIds) {
        for (int i = 0; i < layerIds.size(); i++) {
            log.info("Analyzing " + layerIds.get(i));

            if (i > 0) {
                analyzeLayer(layerIds.get(i), layerIds.get(i - 1));
            } else {
                analyzeLayer(layerIds.get(i), "");
            }
        }

        return converter.convert(fetchVulnerabilities(layerIds));
    }

    private List<ClairLayerScanResponse> fetchVulnerabilities(List<String> layerIds) {
        List<ClairLayerScanResponse> results = new LinkedList<>();
        results.add(getLayerVulnerabilities(layerIds.get(layerIds.size() - 1)));
        return results;
    }

    private void analyzeLayer(String layer, String prevLayer) {
        try {
            ClairLayerScanRequest layerRequest = ClairLayerScanRequest.builder()
                    .layer(ClairLayerScanData.builder()
                            .name(layer)
                            .path(LOCAL_URI_TO_GET_LAYER + layer)
                            .parentName(prevLayer)
                            .format("Docker")
                            .build())
                    .build();


            new RestTemplate().postForObject(CLAIR_SERVER_URL + POST_LAYER_URI, layerRequest, String.class);
        } catch (Exception e) {
            log.error("Could not send layer info to clair", e);
        }
    }

    private ClairLayerScanResponse getLayerVulnerabilities(String layerId) {
        try {
            return new RestTemplate().getForObject(CLAIR_SERVER_URL + GET_LAYER_FEATURES_URI.replace("%s", layerId), ClairLayerScanResponse.class);
        } catch (Exception e) {
            log.error("Could not get layer info from clair", e);
            return new ClairLayerScanResponse();
        }
    }

}
