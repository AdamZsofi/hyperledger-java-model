package bme.mit.ftsrg.chaincode;

// Based on: https://github.com/hyperledger/fabric-chaincode-java/blob/main/examples/fabric-contract-example-gradle/src/main/java/org/example/MyAssetContract.java
// TODO for now, this contract is hardcoded everywhere

import bme.mit.ftsrg.mockFabric.Context;
import bme.mit.ftsrg.mockFabric.ContractInterface;

/*
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;

import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
*/

/*
@Contract(name = "CrossroadContract",
    info = @Info(title = "Crossroad contract",
        description = "Very basic Java Contract example",
        version = "0.0.1",
        license =
        @License(name = "SPDX-License-Identifier: Apache-2.0",
            url = ""),
        contact =  @Contact(email = "CrossroadContract@example.com",
            name = "CrossroadContract",
            url = "http://CrossroadContract.me")))
*/
//@Default
public class TrainCrossroadContract implements ContractInterface {
    //@Transaction()
    public void updateState(Context ctx, String value) {
        ctx.getStub().putStringState("canGo", value); // value should be true or false
    }
}