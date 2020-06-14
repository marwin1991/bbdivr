package pl.marwin1991.bbdivr.chaincode;

import lombok.Data;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@DataType
public class Layer {

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
    private List<Vulnerability> vulnerabilities;
}
