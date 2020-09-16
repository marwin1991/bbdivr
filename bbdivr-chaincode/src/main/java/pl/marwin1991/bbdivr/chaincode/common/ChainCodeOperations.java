package pl.marwin1991.bbdivr.chaincode.common;

public enum ChainCodeOperations {
    QUERY_LAYER("queryLayer"),
    QUERY_LAYER_WITH_PARENTS("queryLayerWithParents"),
    QUERY_PAGED_LAYERS("queryPagedLayers"),
    ADD_LAYER("addLayer"),
    ADD_VULNERABILITY_TO_LAYER("addVulnerabilityToLayer"),

    QUERY_VULNERABILITY("queryVulnerability"),
    ADD_VULNERABILITY("addVulnerability"),
    QUERY_PAGED_VULNERABILITIES("queryPagedVulnerabilities"),
    ;

    private final String operationName;

    ChainCodeOperations(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationName() {
        return operationName;
    }
}
