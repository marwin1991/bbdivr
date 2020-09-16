package pl.marwin1991.bbdivr.anchore.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnchoreManifest {

    private int schemaVersion;
    private String mediaType;
    private AnchoreLayerInfo config;
    private List<AnchoreLayerInfo> layers;
}
