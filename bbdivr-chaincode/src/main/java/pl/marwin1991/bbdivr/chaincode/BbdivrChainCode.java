package pl.marwin1991.bbdivr.chaincode;

import com.google.gson.Gson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;
import org.hyperledger.fabric.shim.ledger.QueryResultsIteratorWithMetadata;

import java.util.LinkedList;
import java.util.List;

@Contract(
        name = "BBDIVR",
        info = @Info(
                title = "BBDIVR contract",
                description = "BBDIVR contract to store and manage docker images vulnerabilities",
                version = "1.0.0-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "peter.zmilczak@gmail.com",
                        name = "Peter Zmilczak",
                        url = "https://github.com/marwin1991")))
@Default
public final class BbdivrChainCode implements ContractInterface {

    private final Gson jsonConverter = new Gson();

    /**
     * Retrieves a layer with the specified layerId from the ledger.
     *
     * @param ctx     the transaction context
     * @param layerId the layerId
     * @return the Car found on the ledger if there was one
     */
    @Transaction()
    public ChainCodeLayer queryLayer(final Context ctx, final String layerId) {
        return getExistingLayer(ctx, layerId);
    }

    /**
     * Retrieves a list of layers with the specified layerId and all parents layers from the ledger.
     *
     * @param ctx     the transaction context
     * @param layerId the layerId
     * @return the Car found on the ledger if there was one
     */
    @Transaction()
    public List<ChainCodeLayer> queryLayerWithParents(final Context ctx, final String layerId) {
        List<ChainCodeLayer> layers = new LinkedList<>();

        ChainCodeLayer layer = getExistingLayer(ctx, layerId);
        layers.add(layer);

        while (layer.getParentId().isEmpty()) {
            layer = getExistingLayer(ctx, layer.getParentId());
        }

        return layers;
    }

    /**
     * Adds a new layer on the ledger.
     *
     * @param ctx           the transaction context
     * @param layerId       the layer id of the new layer
     * @param parentLayerId the layer id of the parent layer, if layer has no parent, pass empty string
     * @param layerAsJson   the string representation of Layer
     * @return the created layer
     */

    @Transaction()
    public ChainCodeLayer addLayer(final Context ctx, final String layerId, final String parentLayerId, final String layerAsJson) {
        ChaincodeStub stub = ctx.getStub();
        checkIfLayerExist(ctx, layerId);

        if (!parentLayerId.isEmpty())
            checkIfLayerExist(ctx, parentLayerId);

        stub.putStringState(layerId, layerAsJson);

        return jsonConverter.fromJson(layerAsJson, ChainCodeLayer.class);
    }

    /**
     * Adds a new layer on the ledger.
     *
     * @param ctx                 the transaction context
     * @param layerId             the layer id of the new layer
     * @param vulnerabilityAsJson the string representation of Layer
     * @return the created layer
     */

    @Transaction()
    public ChainCodeLayer addVulnerability(final Context ctx, final String layerId, final String vulnerabilityAsJson) {
        ChaincodeStub stub = ctx.getStub();
        ChainCodeLayer layer = getExistingLayer(ctx, layerId);
        ChainCodeVulnerability vulnerability = jsonConverter.fromJson(vulnerabilityAsJson, ChainCodeVulnerability.class);

        if (layer.getVulnerabilities().contains(vulnerability)) {
            String errorMessage = String.format("Layer with id: %s already has this vulnerability %s", layerId, vulnerability.getVulnerabilityName());
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, BbdivrChainCodeErrors.VULNERABILITY_ALREADY_EXIST.toString());
        }

        layer.getVulnerabilities().add(vulnerability);
        stub.putStringState(layerId, jsonConverter.toJson(layer));

        return layer;
    }

    /**
     * Retrieves every layers (not recommended, use with pagination).
     *
     * @param ctx the transaction context
     * @return list of layers found on the ledger
     */
    @Transaction()
    public List<ChainCodeLayer> queryAllLayers(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        final String startKey = "";
        final String endKey = "";

        List<ChainCodeLayer> layers = new LinkedList<>();
        QueryResultsIterator<KeyValue> results = stub.getStateByRange(startKey, endKey);

        results.forEach(r -> layers.add(jsonConverter.fromJson(r.getStringValue(), ChainCodeLayer.class)));

        return layers;
    }


    /**
     * Retrieves page of layers.
     *
     * @param ctx the transaction context
     * @return page containing layers found on the ledger
     */
    @Transaction()
    public ChainCodePageLayers queryPagedLayers(final Context ctx, final String pageId, final String pageSize) {
        ChaincodeStub stub = ctx.getStub();

        final String startKey = "";
        final String endKey = "";
        final int pageSizeInt = Integer.parseInt(pageSize) + 1; //increase to get next bookmark

        List<ChainCodeLayer> layers = new LinkedList<>();
        QueryResultsIteratorWithMetadata<KeyValue> results = stub.getStateByRangeWithPagination(startKey, endKey, pageSizeInt, pageId);

        results.forEach(r -> layers.add(jsonConverter.fromJson(r.getStringValue(), ChainCodeLayer.class)));


        ChainCodePageLayers pageLayers = new ChainCodePageLayers();
        pageLayers.setPageId(results.getMetadata().getBookmark());

        if (pageSizeInt == layers.size())
            pageLayers.setNextPageId(layers.get(layers.size() - 1).getId());

        pageLayers.setPageSize(pageSizeInt - 1);

        //removes last element if exist, as it is returned only to get bookmark
        if (layers.size() > 0)
            layers.remove(layers.size() - 1);

        pageLayers.setLayers(layers);

        return pageLayers;
    }

    private ChainCodeLayer getExistingLayer(final Context ctx, final String layerId) {
        return getLayer(ctx, layerId, true);
    }

    private void checkIfLayerExist(final Context ctx, final String layerId) {
        getLayer(ctx, layerId, false);
    }


    private ChainCodeLayer getLayer(final Context ctx, final String layerId, boolean exist) {
        ChaincodeStub stub = ctx.getStub();
        String layerState = stub.getStringState(layerId);

        if (layerState.isEmpty() && exist) {
            String errorMessage = String.format("Layer with id: %s does not exist", layerId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, BbdivrChainCodeErrors.LAYER_NOT_FOUND.toString());
        }

        if (!layerState.isEmpty() && !exist) {
            String errorMessage = String.format("Layer with id: %s already exists", layerId);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, BbdivrChainCodeErrors.LAYER_ALREADY_EXISTS.toString());
        }

        return jsonConverter.fromJson(layerState, ChainCodeLayer.class);
    }


    private enum BbdivrChainCodeErrors {
        LAYER_NOT_FOUND,
        LAYER_ALREADY_EXISTS,
        VULNERABILITY_ALREADY_EXIST
    }
}