package pl.marwin1991.bbdivr.clair.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClairLayerScanResponse {

    @JsonProperty("Layer")
    private ClairLayerScanResult layer;

}
