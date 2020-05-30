package pl.marwin1991.bbdivr.clair.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Layer {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Path")
    private String path;

    @JsonProperty("ParentName")
    private String parentName;

    @JsonProperty("Format")
    private String format;
}
