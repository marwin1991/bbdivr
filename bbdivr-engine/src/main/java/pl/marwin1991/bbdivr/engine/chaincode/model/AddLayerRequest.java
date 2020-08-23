package pl.marwin1991.bbdivr.engine.chaincode.model;

import lombok.Data;

import java.util.List;

@Data
public class AddLayerRequest {
    private String id;

    private String parentId;

    private List<VulnerabilityDTO> vulnerabilities;
}
