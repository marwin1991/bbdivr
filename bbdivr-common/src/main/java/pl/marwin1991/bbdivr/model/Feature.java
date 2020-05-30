package pl.marwin1991.bbdivr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Feature {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("NamespaceName")
    private String nameSpace;

    @JsonProperty("VersionFormat")
    private String versionFormat;

    @JsonProperty("Version")
    private String version;

    @JsonProperty("Vulnerabilities")
    private List<Vulnerability> vulnerabilities;

    @JsonProperty("AddedBy")
    private String addedBy;
}
