package pl.marwin1991.bbdivr.chaincode;

import com.google.gson.Gson;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

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
public final class Bbdivr implements ContractInterface {

    private final Gson jsonConverter = new Gson();

    /**
     * Retrieves a layer with the specified key from the ledger.
     *
     * @param ctx the transaction context
     * @param key the key
     * @return the Car found on the ledger if there was one
     */
    @Transaction()
    public Car queryCar(final Context ctx, final String key) {
        ChaincodeStub stub = ctx.getStub();
        stub.getStringState()
        String carState = stub.getStringState(key);

        if (carState.isEmpty()) {
            String errorMessage = String.format("Car %s does not exist", key);
            System.out.println(errorMessage);
            throw new ChaincodeException(errorMessage, FabCarErrors.CAR_NOT_FOUND.toString());
        }

        Car car = genson.deserialize(carState, Car.class);

        return car;
    }

    private enum BbdivrChainCodeErrors {
        LAYER_NOT_FOUND,
        LAYER_ALREADY_EXISTS
    }
}