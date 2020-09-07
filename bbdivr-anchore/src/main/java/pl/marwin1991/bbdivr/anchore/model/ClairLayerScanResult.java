package pl.marwin1991.bbdivr.anchore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClairLayerScanResult {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("NamespaceName")
    private String nameSpace;

    @JsonProperty("IndexedByVersion")
    private String indexByVersion;

    @JsonProperty("Features")
    private List<ClairFeature> features;

    @JsonProperty("ParentName")
    private String parentName;

}
