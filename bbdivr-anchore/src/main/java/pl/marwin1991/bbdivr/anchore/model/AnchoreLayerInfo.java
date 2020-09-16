package pl.marwin1991.bbdivr.anchore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnchoreLayerInfo {
    private String mediaType;
    private int size;
    private String digest;
}
