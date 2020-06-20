package pl.marwin1991.bbdivr.clair.converter;

import org.springframework.stereotype.Component;
import pl.marwin1991.bbdivr.clair.model.ClairFeature;
import pl.marwin1991.bbdivr.clair.model.ClairLayerScanResponse;
import pl.marwin1991.bbdivr.model.Layer;
import pl.marwin1991.bbdivr.model.ScanResult;
import pl.marwin1991.bbdivr.model.Vulnerability;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScanResultConverter {

    private static final String SCAN_TOOL_NAME = "Clair";

    public ScanResult convert(List<ClairLayerScanResponse> responses) {
        List<Layer> layers = new LinkedList<>();

        responses.forEach(r -> {

            Layer layer = Layer.builder()
                    .id(r.getLayer().getName())
                    .parentId(r.getLayer().getParentName())
                    .vulnerabilities(getVulnerabilities(r.getLayer().getFeatures()))
                    .build();
            layers.add(layer);

        });

        return ScanResult.builder()
                .scanToolName(SCAN_TOOL_NAME)
                .layers(layers)
                .build();
    }

    private List<Vulnerability> getVulnerabilities(List<ClairFeature> features) {
        List<Vulnerability> vulnerabilities = new LinkedList<>();

        features.forEach(f -> vulnerabilities.addAll(getVulnerabilitiesFromFeature(f)));

        return vulnerabilities;
    }

    private List<Vulnerability> getVulnerabilitiesFromFeature(ClairFeature feature) {
        if (feature.getVulnerabilities() == null)
            return Collections.emptyList();

        return feature
                .getVulnerabilities()
                .stream()
                .map(v -> Vulnerability
                        .builder()
                        .vulnerabilityName(v.getName())
                        .featureName(feature.getName())
                        .featureVersion(feature.getVersion())
                        .featureVersionFormat(feature.getVersionFormat())
                        .namespaceName(v.getNamespaceName())
                        .description(v.getDescription())
                        .link(v.getLink())
                        .severity(v.getSeverity())
                        .fixedBy(v.getFixedBy())
                        .build())
                .collect(Collectors.toList());
    }
}
