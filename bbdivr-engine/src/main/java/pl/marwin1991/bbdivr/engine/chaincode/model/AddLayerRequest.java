package pl.marwin1991.bbdivr.engine.chaincode.model;

import lombok.Data;

import java.util.List;

@Data
public class AddLayerRequest {
    private String id;
    private String parentId;
    private List<String> vulnerabilities;
    private List<String> vulnerabilitiesNamesFromParents;
}
