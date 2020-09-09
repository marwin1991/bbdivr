package pl.marwin1991.bbdivr.engine.chaincode.controller;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marwin1991.bbdivr.chaincode.layer.ChainCodeLayer;
import pl.marwin1991.bbdivr.engine.chaincode.converter.RequestToChainCodeConverter;
import pl.marwin1991.bbdivr.engine.chaincode.layer.LayerService;
import pl.marwin1991.bbdivr.engine.chaincode.model.AddLayerRequest;
import pl.marwin1991.bbdivr.engine.chaincode.model.ChainCodeStatus;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
@RestController
@RequestMapping("/layer")
public class LayerController {

    private final LayerService layerService;
    private final RequestToChainCodeConverter converter;

    @Autowired
    public LayerController(LayerService layerService, RequestToChainCodeConverter converter) {
        this.layerService = layerService;
        this.converter = converter;
    }

    @PostMapping
    public ResponseEntity<ChainCodeStatus> addLayer(@RequestBody AddLayerRequest request) {
        ChainCodeLayer chainCodeLayer = converter.convert(request);
        try {
            layerService.addLayer(chainCodeLayer);
            return ResponseEntity.ok(ChainCodeStatus.SUCCESS);
        } catch (IOException | InterruptedException | TimeoutException | ContractException e) {
            log.error("Add layer failed", e);
            return new ResponseEntity<>(ChainCodeStatus.FAIL, HttpStatus.BAD_REQUEST);
        }
    }
}
