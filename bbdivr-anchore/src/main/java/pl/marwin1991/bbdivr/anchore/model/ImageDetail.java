package pl.marwin1991.bbdivr.anchore.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDetail {

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("last_updated")
    private OffsetDateTime lastUpdated;

    @JsonProperty("fulltag")
    private String fulltag;

    @JsonProperty("fulldigest")
    private String fulldigest;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("imageId")
    private String imageId;

    @JsonProperty("registry")
    private String registry;

    @JsonProperty("repo")
    private String repo;

    @JsonProperty("dockerfile")
    private String dockerfile;

    @JsonProperty("imageDigest")
    private String imageDigest;

}

