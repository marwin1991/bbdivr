package pl.marwin1991.bbdivr.clair.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.marwin1991.bbdivr.clair.model.Layer;
import pl.marwin1991.bbdivr.clair.model.LayerRequest;
import pl.marwin1991.bbdivr.model.LayerResponse;
import pl.marwin1991.bbdivr.model.ScanResult;
import pl.marwin1991.bbdivr.service.LayerAnalyseService;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class ClairLayerAnalyseService implements LayerAnalyseService {

    private static final String postLayerURI = "/v1/layers";
    private static final String CLAIR_SERVER_URL = "http://localhost:6060";
    private static final String getLayerFeaturesURI = "/v1/layers/%s?features&vulnerabilities";

    @Override
    public ScanResult analyse(List<String> layerIds) {
        String tmpPath = "http://host.docker.internal:8080/layers/";
        for (int i = 0; i < layerIds.size(); i++) {
            log.info("Analyzing " + layerIds.get(i));

            if (i > 0) {
                analyzeLayer(tmpPath + "/" + layerIds.get(i), layerIds.get(i), layerIds.get(i - 1));
            } else {
                analyzeLayer(tmpPath + "/" + layerIds.get(i), layerIds.get(i), "");
            }
        }

        List<LayerResponse> results = new LinkedList<>();
        results.addAll(fetchVulnerabilities(layerIds));

        return ScanResult.builder()
                .result(results)
                .build();
    }

    private List<LayerResponse> fetchVulnerabilities(List<String> layerIds) {
        List<LayerResponse> results = new LinkedList<>();
        results.add(getLayerVulnerabilities(layerIds.get(layerIds.size() - 1)));
        return results;
    }

    private String analyzeLayer(String bbdivrUrl, String layer, String prevLayer) {
        try {
            LayerRequest layerRequest = LayerRequest.builder()
                    .layer(Layer.builder()
                            .name(layer)
                            .path(bbdivrUrl)
                            .parentName(prevLayer)
                            .format("Docker")
                            .build())
                    .build();


            String response = new RestTemplate().postForObject(CLAIR_SERVER_URL + postLayerURI, layerRequest, String.class);
            return response.replaceAll("\\\"", "\"");
        } catch (Exception e) {
            return "";
        }
    }

    private LayerResponse getLayerVulnerabilities(String layerId) {
        try {
            return new RestTemplate().getForObject(CLAIR_SERVER_URL + getLayerFeaturesURI.replace("%s", layerId), LayerResponse.class);
        } catch (Exception e) {
            return new LayerResponse();
        }
    }

}
