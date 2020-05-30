package pl.marwin1991.bbdivr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LayerResponse {
    @JsonProperty("Layer")
    private LayerWithVulnerabilities layer;
}
