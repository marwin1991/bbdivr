package pl.marwin1991.bbdivr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LayerWithVulnerabilities {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("NamespaceName")
    private String nameSpace;

    @JsonProperty("IndexedByVersion")
    private String indexByVersion;

    @JsonProperty("Features")
    private List<Feature> features;

    @JsonProperty("ParentName")
    private String parentName;

}
