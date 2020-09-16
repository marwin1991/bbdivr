package pl.marwin1991.bbdivr.chaincode.layer;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.List;

/*
 * Object that is used to return layers and page info
 */
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

    public ChainCodePageLayers() {
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getNextPageId() {
        return nextPageId;
    }

    public void setNextPageId(String nextPageId) {
        this.nextPageId = nextPageId;
    }

    public List<ChainCodeLayer> getLayers() {
        return layers;
    }

    public void setLayers(List<ChainCodeLayer> layers) {
        this.layers = layers;
    }
}
