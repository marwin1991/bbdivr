package pl.marwin1991.bbdivr.anchore.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.marwin1991.bbdivr.anchore.model.VulnerabilityList;
import pl.marwin1991.bbdivr.model.ScanResult;
import pl.marwin1991.bbdivr.model.Severity;
import pl.marwin1991.bbdivr.model.SumScanResult;

import java.util.*;

@Slf4j
@Component
public class AnchoreScanResultConverter {

    private static final String SCAN_TOOL_NAME = "Anchore";

    public ScanResult convert(List<VulnerabilityList> responses) {

        return ScanResult.builder()
                .scanToolName(SCAN_TOOL_NAME)
                .layers(null)
                .build();
    }

    public SumScanResult convertToSum(List<VulnerabilityList> responses) {

        return SumScanResult.builder()
                .scanToolName(SCAN_TOOL_NAME)
                .id(responses.get(0).getImageDigest())
                .vulnerabilitiesIds(getVulnerabilities(responses))
                .build();
    }

    private Map<Severity, List<String>> getVulnerabilities(List<VulnerabilityList> responses) {
        Map<Severity, List<String>> vulMap = new HashMap<>();

        responses.get(0).getVulnerabilities().forEach(v -> {
            Severity severity = Severity.valueOf(v.getSeverity());

            if (vulMap.containsKey(severity)) {
                vulMap.get(severity).add(v.getVuln());
            } else {
                vulMap.put(severity, new LinkedList<>(Collections.singleton(v.getVuln())));
            }
        });

        return vulMap;

    }

}
