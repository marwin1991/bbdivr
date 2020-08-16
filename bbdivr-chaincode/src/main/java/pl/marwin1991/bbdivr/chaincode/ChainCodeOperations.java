package pl.marwin1991.bbdivr.chaincode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChainCodeOperations {
    QUERY_LAYER("queryLayer"),
    QUERY_LAYER_WITH_PARENTS("queryLayerWithParents"),
    QUERY_PAGED_LAYERS("queryPagedLayers"),
    ADD_LAYER("addLayer"),
    ADD_VULNERABILITY("addVulnerability"),
    ;

    private final String operationName;
}
