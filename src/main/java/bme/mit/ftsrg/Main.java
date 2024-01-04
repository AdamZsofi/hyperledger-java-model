package bme.mit.ftsrg;

import bme.mit.ftsrg.model.FabricNetworkBuilder;
import bme.mit.ftsrg.model.Network;
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

        Network network = builder.build();
        System.out.println(network);

        /*
        int myVariable = 1;  // Your variable
        assert myVariable != 1 : "Violation: myVariable should not be equal to ";
        */

        // TODO chaincode
    }
}