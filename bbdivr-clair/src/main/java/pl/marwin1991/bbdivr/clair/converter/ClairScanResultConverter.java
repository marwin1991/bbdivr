package pl.marwin1991.bbdivr.clair.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.marwin1991.bbdivr.clair.model.ClairFeature;
import pl.marwin1991.bbdivr.clair.model.ClairLayerScanResponse;
import pl.marwin1991.bbdivr.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ClairScanResultConverter {

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
                    .fixedVulnerabilitiesFromParents(new LinkedList<>())
                    //.vulnerabilitiesNamesFromParents(new LinkedList<>(preVulnerabilities))
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
                        .severity(Severity.of(v.getSeverity()))
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

    public SumScanResult convertToSum(List<ClairLayerScanResponse> v) {
        return SumScanResult.builder()
                .scanToolName(SCAN_TOOL_NAME)
                .id(v.get(v.size() - 1).getLayer().getName())
                .vulnerabilitiesIds(getVulnerabilities(v.get(v.size() - 1).getLayer().getFeatures()))
                .build();
    }

    private Map<Severity, Set<String>> getVulnerabilities(List<ClairFeature> features) {
        Map<Severity, Set<String>> vulnerabilities = new HashMap<>();

        if(Objects.isNull(features))
            return vulnerabilities;
        
        for (ClairFeature f : features) {
            getVulnerabilitiesFromFeature(f).forEach((k, v) -> {
                if (vulnerabilities.containsKey(k)) {
                    vulnerabilities.get(k).addAll(v);
                } else {
                    vulnerabilities.put(k, v);
                }
            });
        }

        return vulnerabilities;
    }

    private Map<Severity, Set<String>> getVulnerabilitiesFromFeature(ClairFeature f) {
        Map<Severity, Set<String>> vulMap = new HashMap<>();

        if (f != null && f.getVulnerabilities() != null) {
            f.getVulnerabilities().forEach(v -> {
                Severity severity = Severity.of(v.getSeverity());

                if (vulMap.containsKey(severity)) {
                    vulMap.get(severity).add(v.getName());
                } else {
                    vulMap.put(severity, new LinkedHashSet<>(Collections.singleton(v.getName())));
                }
            });
        }

        return vulMap;
    }
}
