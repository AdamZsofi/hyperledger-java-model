package bme.mit.ftsrg;

import bme.mit.ftsrg.model.FabricNetworkBuilder;
import bme.mit.ftsrg.model.Network;
import bme.mit.ftsrg.model.participants.OrderingService;
import bme.mit.ftsrg.model.participants.Organization;
import bme.mit.ftsrg.model.participants.Peer;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        FabricNetworkBuilder builder = new FabricNetworkBuilder();
        builder.addOrganization("R1");
        builder.addOrganization("R2");
        builder.addOrganization("R3");

        builder.addPeer("P1", "R1");
        builder.addPeer("P2", "R2");
        builder.addOrderingService("P3", "R3");

        builder.addChannel("C1");

        builder.registerNodesToChannel(List.of("P1", "P2", "P3"), "C1");

        Network network = builder.build();
        System.out.println(network);

        int myVariable = 1;  // Your variable
        assert myVariable != 1 : "Violation: myVariable should not be equal to ";

        // TODO chaincode
    }
}