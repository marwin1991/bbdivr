package pl.marwin1991.bbdivr.chaincode.layer;

import lombok.Data;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.List;

/*
 * Object that is used to return layers and page info
 */
@Data
@DataType
public class ChainCodePageLayers {
    @Property
    private String pageId;

    @Property
    private int pageSize;

    @Property
    private String nextPageId;

    @Property
    private List<ChainCodeLayer> layers;
}
