package pl.marwin1991.bbdivr.clair.converter;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
public class ScanResultConverter {

    private static final String SCAN_TOOL_NAME = "Clair";

    public ScanResult convert(List<ClairLayerScanResponse> responses) {
        List<Layer> layers = new LinkedList<>();

        List<Vulnerability> preVulnerabilities = new LinkedList<>();
        List<Vulnerability> vulnerabilities;

        for (ClairLayerScanResponse r : responses) {
            log.info("Converting layer: " + r.getLayer().getName());
            vulnerabilities = getVulnerabilities(r.getLayer().getFeatures(), preVulnerabilities);

            Layer layer = Layer.builder()
                    .id(r.getLayer().getName())
                    .parentId(r.getLayer().getParentName())
                    .vulnerabilities(vulnerabilities)
                    .vulnerabilitiesNamesFromParents(new LinkedList<>(preVulnerabilities))
                    .build();
            layers.add(layer);

            preVulnerabilities.addAll(vulnerabilities);

        }

        return ScanResult.builder()
                .scanToolName(SCAN_TOOL_NAME)
                .layers(layers)
                .build();
    }

    private List<Vulnerability> getVulnerabilities(List<ClairFeature> features, List<Vulnerability> previousVulnerabilities) {
        List<Vulnerability> vulnerabilities = new LinkedList<>();

        for (ClairFeature f : features) {
            vulnerabilities.addAll(getVulnerabilitiesFromFeature(f, previousVulnerabilities));
        }

        return vulnerabilities;
    }

    private List<Vulnerability> getVulnerabilitiesFromFeature(ClairFeature feature, List<Vulnerability> previousVulnerabilities) {
        if (feature.getVulnerabilities() == null)
            return Collections.emptyList();

        return feature
                .getVulnerabilities()
                .stream()
                .map(v -> Vulnerability
                        .builder()
                        .id(v.getName())
                        .severity(v.getSeverity())
                        .build()
                )
                .filter(v -> !previousVulnerabilities.contains(v))
                .collect(Collectors.toList());

//        return feature
//                .getVulnerabilities()
//                .stream()
//                .filter(v -> !prevNames.contains(v.getName()))
//                .map(v -> Vulnerability
//                        .builder()
//                        .vulnerabilityName(v.getName())
//                        .featureName(feature.getName())
//                        .featureVersion(feature.getVersion())
//                        .featureVersionFormat(feature.getVersionFormat())
//                        .namespaceName(v.getNamespaceName())
//                        .description(v.getDescription())
//                        .link(v.getLink())
//                        .severity(v.getSeverity())
//                        .fixedBy(v.getFixedBy())
//                        .build())
//                .collect(Collectors.toList());
    }
}
