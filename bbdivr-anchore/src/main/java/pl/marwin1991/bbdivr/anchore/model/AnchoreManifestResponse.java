package pl.marwin1991.bbdivr.anchore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnchoreManifestResponse {

    private String imageDigest;
    private String matadata;

    @JsonProperty("metadata_type")
    private String metadataType;
}
