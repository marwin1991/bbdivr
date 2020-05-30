package pl.marwin1991.bbdivr.clair.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayerRequest {

    @JsonProperty("Layer")
    private Layer layer;
}
