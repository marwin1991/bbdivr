package pl.marwin1991.bbdivr.engine.chaincode.wallet;

import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;

@Service
public class WalletService {

    public Wallet getWallet() throws IOException {
        return Wallets.newFileSystemWallet(Paths.get("wallet"));
    }
}
