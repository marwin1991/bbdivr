package pl.marwin1991.bbdivr.clair.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClairLayerScanRequest {

    @JsonProperty("Layer")
    private ClairLayerScanData layer;
}
