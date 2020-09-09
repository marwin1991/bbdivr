package pl.marwin1991.bbdivr.engine.chaincode.layer;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.gateway.Gateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.marwin1991.bbdivr.chaincode.common.ChainCodeOperations;
import pl.marwin1991.bbdivr.chaincode.layer.ChainCodeLayer;
import pl.marwin1991.bbdivr.engine.chaincode.ContractService;
import pl.marwin1991.bbdivr.engine.chaincode.wallet.WalletService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeoutException;

import static pl.marwin1991.bbdivr.engine.chaincode.enroll.EnrollService.APP_USER;
import static pl.marwin1991.bbdivr.engine.chaincode.enroll.EnrollService.NETWORK_CONFIG_PATH;

@Slf4j
@Service
public class LayerService {

    private final Gateway.Builder builder;
    private final Path networkConfigPath;
    private final WalletService walletService;
    private final ContractService contractService;


    @Autowired
    public LayerService(WalletService walletService, ContractService contractService) {
        this.walletService = walletService;
        this.contractService = contractService;
        this.networkConfigPath = Paths.get(NETWORK_CONFIG_PATH);
        this.builder = Gateway.createBuilder();
    }

    public ChainCodeLayer addLayer(ChainCodeLayer layer) throws IOException, InterruptedException, TimeoutException, ContractException {
        builder.identity(walletService.getWallet(), APP_USER).networkConfig(networkConfigPath).discovery(true);
        try (Gateway gateway = builder.connect()) {

            String json = layer.toJson();

            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            log.info("bytes.length = " + bytes.length);

            byte[] result = contractService.getContract(gateway)
                    .createTransaction(ChainCodeOperations.ADD_LAYER.getOperationName())
                    .submit(layer.getId(),
                            layer.getParentId(),
                            layer.toJson());

            return ChainCodeLayer.fromJson(new String(result, StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error("Error evaluating the contract", e);
            throw e;
        }
    }

}
