package pl.marwin1991.bbdivr.chaincode;

import lombok.Data;

import java.util.List;

/*
 * Object that is used to return layers and page info
 */
@Data
public class ChainCodePageLayers {
    private String pageId;
    private int pageSize;
    private String nextPageId;

    private List<ChainCodeLayer> layers;
}
