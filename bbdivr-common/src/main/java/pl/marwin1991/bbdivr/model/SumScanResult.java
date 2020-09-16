package pl.marwin1991.bbdivr.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@Builder
public class SumScanResult {

    private String scanToolName;
    private String id;
    private Map<Severity, Set<String>> vulnerabilitiesIds;

}
