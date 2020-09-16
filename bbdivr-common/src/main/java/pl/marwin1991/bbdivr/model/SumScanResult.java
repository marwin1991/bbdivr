package pl.marwin1991.bbdivr.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SumScanResult {

    private String scanToolName;
    private String id;
    private Map<Severity, List<String>> vulnerabilitiesIds;

}
