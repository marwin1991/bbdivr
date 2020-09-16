package pl.marwin1991.bbdivr.engine.chaincode;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.springframework.stereotype.Service;

@Service
public class ContractService {

    private static final String CHANNEL_NAME = "bbdivr-channel";
    private static final String CONTRACT_NAME = "bbdivr";

    public Contract getVulnerabilitiesContract(Gateway gateway) {
        Network network = gateway.getNetwork(CHANNEL_NAME);
        return network.getContract(CONTRACT_NAME, "bbdivr-vulnerability-contract");
    }


    public Contract getLayersContract(Gateway gateway) {
        Network network = gateway.getNetwork(CHANNEL_NAME);
        return network.getContract(CONTRACT_NAME, "bbdivr-layer-contract");
    }
}
