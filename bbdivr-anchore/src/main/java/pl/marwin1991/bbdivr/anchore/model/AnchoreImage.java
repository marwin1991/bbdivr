package pl.marwin1991.bbdivr.anchore.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnchoreImage {
    @JsonProperty("image_content")
    private ImageContent imageContent;

    @JsonProperty("image_detail")
    private List<ImageDetail> imageDetail;

    @JsonProperty("last_updated")
    private OffsetDateTime lastUpdated;

    @JsonProperty("created_at")
    private OffsetDateTime createdAt;

    @JsonProperty("imageDigest")
    private String imageDigest;

    @JsonProperty("userId")
    private String userId;

    @JsonProperty("annotations")
    private Object annotations;

    @JsonProperty("analysis_status")
    private AnalysisStatusEnum analysisStatus;

    @JsonProperty("image_status")
    private ImageStatusEnum imageStatus;


    /**
     * State of the image
     */
    public enum ImageStatusEnum {
        ACTIVE("active"),

        INACTIVE("inactive"),

        DISABLED("disabled");

        private final String value;

        ImageStatusEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static ImageStatusEnum fromValue(String text) {
            for (ImageStatusEnum b : ImageStatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }

    /**
     * A state value for the current status of the analysis progress of the image
     */
    @AllArgsConstructor
    public enum AnalysisStatusEnum {
        NOT_ANALYZED("not_analyzed"),

        ANALYZING("analyzing"),

        ANALYZED("analyzed"),

        ANALYSIS_FAILED("analysis_failed");

        private final String value;

        @JsonCreator
        public static AnalysisStatusEnum fromValue(String text) {
            for (AnalysisStatusEnum b : AnalysisStatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }

}

