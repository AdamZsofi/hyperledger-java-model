package bme.mit.ftsrg;

import bme.mit.ftsrg.chaincode.MyAssetContract;
import bme.mit.ftsrg.mockFabric.ChaincodeStub;
import bme.mit.ftsrg.mockFabric.Context;
import bme.mit.ftsrg.model.FabricNetworkBuilder;
import bme.mit.ftsrg.model.Network;
import bme.mit.ftsrg.model.participants.application.Application;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        FabricNetworkBuilder builder = new FabricNetworkBuilder();
        builder.addOrganization("R1");
        builder.addOrganization("R2");

        builder.addPeer("P1", "R1");
        builder.addPeer("P2", "R2");
        builder.addOrderingService("O1");

        builder.addChannel("C1");

        builder.registerPeersToChannel(List.of("P1", "P2"), "C1");
        builder.registerOrderingServiceToChannel("O1", "C1");

        builder.addApplication("P1");

        Network network = builder.build();
        System.out.println(network);

        // Client/Application proposes transaction - this starts the whole endorsement and consensus process
        Application app = network.getApplication("P1");
        app.contract.createMyAsset(app.context, "A1", "myAssetValue");

        /*
        int myVariable = 1;  // Your variable
        assert myVariable != 1 : "Violation: myVariable should not be equal to ";
        */
    }
}