package pl.marwin1991.bbdivr.chaincode.layer;

import com.google.gson.Gson;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.time.OffsetDateTime;
import java.util.List;

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

    public ChainCodeLayer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public OffsetDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(OffsetDateTime createDate) {
        this.createDate = createDate;
    }

    public OffsetDateTime getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(OffsetDateTime modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Integer getModifyIndex() {
        return modifyIndex;
    }

    public void setModifyIndex(Integer modifyIndex) {
        this.modifyIndex = modifyIndex;
    }

    public List<String> getVulnerabilityIds() {
        return vulnerabilityIds;
    }

    public void setVulnerabilityIds(List<String> vulnerabilityIds) {
        this.vulnerabilityIds = vulnerabilityIds;
    }

    public List<String> getFixedVulnerabilityIdsFromParents() {
        return fixedVulnerabilityIdsFromParents;
    }

    public void setFixedVulnerabilityIdsFromParents(List<String> fixedVulnerabilityIdsFromParents) {
        this.fixedVulnerabilityIdsFromParents = fixedVulnerabilityIdsFromParents;
    }

    public String toJson() {
        Gson jsonConverter = new Gson();
        return jsonConverter.toJson(this);
    }

    public static ChainCodeLayer fromJson(String json) {
        Gson jsonConverter = new Gson();
        return jsonConverter.fromJson(json, ChainCodeLayer.class);
    }
}
