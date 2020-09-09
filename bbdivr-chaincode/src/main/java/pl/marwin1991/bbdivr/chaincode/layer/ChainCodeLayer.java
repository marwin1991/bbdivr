package pl.marwin1991.bbdivr.chaincode.layer;

import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@DataType
public class ChainCodeLayer {

    @Property
    private String id;

    @Property
    private String parentId;

    @Property
    private OffsetDateTime createDate;

    @Property
    private OffsetDateTime modifyDate;

    @Property
    private Integer modifyIndex;

    @Property
    private List<String> vulnerabilityIds;

    @Property
    private List<String> fixedVulnerabilityIdsFromParents;

    public String toJson() {
        Gson jsonConverter = new Gson();
        return jsonConverter.toJson(this);
    }

    public static ChainCodeLayer fromJson(String json) {
        Gson jsonConverter = new Gson();
        return jsonConverter.fromJson(json, ChainCodeLayer.class);
    }
}
