package pl.marwin1991.bbdivr.anchore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClairLayerScanResponse {

    @JsonProperty("Layer")
    private ClairLayerScanResult layer;

}
