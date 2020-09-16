package pl.marwin1991.bbdivr.anchore.model;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class RegistryDigestSource {

    private String pullstring;
    private String tag;
    private OffsetDateTime creationTimestampOverride;
    private String dockerfile;

}

