package pl.marwin1991.bbdivr.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Layer {

    private String id;
    private String parentId;
    private List<Vulnerability> vulnerabilities;
    private List<Vulnerability> vulnerabilitiesNamesFromParents;

}
