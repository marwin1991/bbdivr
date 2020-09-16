package pl.marwin1991.bbdivr.anchore.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistryTagSource {

    private String pullstring;
    private String dockerfile;

}

