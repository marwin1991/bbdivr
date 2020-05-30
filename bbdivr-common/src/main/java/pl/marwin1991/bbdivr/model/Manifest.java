package pl.marwin1991.bbdivr.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Manifest {

    @JsonProperty("Config")
    private String config;

    @JsonProperty("RepoTags")
    private List<String> repoTags;

    @JsonProperty("Layers")
    private List<String> layers;
}
