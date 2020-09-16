package pl.marwin1991.bbdivr.anchore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnchoreImageScanRequest {

    @JsonProperty("image_type")
    private String imageType;

    private ImageSource source;

}
