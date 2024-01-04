package bme.mit.ftsrg.model.participants.application;

import bme.mit.ftsrg.chaincode.MyAssetContract;
import bme.mit.ftsrg.mockFabric.ChaincodeStub;
import bme.mit.ftsrg.mockFabric.Context;
import bme.mit.ftsrg.model.participants.peers.Peer;

// the application instances will be used to initiate transactions through the chaincode
public class Application {

    public final MyAssetContract contract;
    public final Context context;

    public Application(Peer peer) {
        this.contract = new MyAssetContract();
        this.context = new Context(new ChaincodeStub(peer));
    }
}
