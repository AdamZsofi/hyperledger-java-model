package bme.mit.ftsrg;

import bme.mit.ftsrg.model.FabricNetworkBuilder;
import bme.mit.ftsrg.model.Network;
import bme.mit.ftsrg.model.participants.application.TrainClient;
import bme.mit.ftsrg.model.participants.ordering.FaultMode;
import bme.mit.ftsrg.model.participants.peers.Peer;
import java.util.List;
import java.util.Objects;

public class Main {
    // TODO configure here (and check assertion below at end of main)
    public static FaultMode ordererFaultMode = FaultMode.canReorder;
    public static int blockSize = 5;

    public static void main(String[] args) {
        FabricNetworkBuilder builder = new FabricNetworkBuilder();
        builder.addOrganization("R1");
        builder.addOrganization("R2");

        Peer p1 = builder.addPeer("P1", "R1");
        Peer p2 = builder.addPeer("P2", "R2");
        builder.addOrderingService("O1", blockSize, ordererFaultMode);

        builder.addChannel("C1");

        builder.registerPeersToChannel(List.of("P1", "P2"), "C1");
        builder.installContract("P1", "C1");
        builder.registerOrderingServiceToChannel("O1", "C1");

        TrainClient client = builder.addClient("Client", "P1", "O1");

        Network network = builder.build();
        System.out.println(network);

        client.updateCrossroadState(false); // init
        client.updateCrossroadState(false); // there is a train coming
        client.updateCrossroadState(false); // there is a train coming
        client.updateCrossroadState(false); // there is a train coming
        client.updateCrossroadState(false); // there is a train coming
        client.updateCrossroadState(true); // no train coming
        client.updateCrossroadState(false); // there is a train coming
        client.updateCrossroadState(false); // there is a train coming
        client.updateCrossroadState(false); // there is a train coming
        client.updateCrossroadState(false); // there is a train coming
        network.execute();

        //assert Objects.equals(p1.getWorldState("canGo"), "true") : "canGo should be true in the end!";
        assert Objects.equals(p1.getWorldState("canGo"), "false") : "canGo should be false in the end!";
        //assert Objects.equals(p2.getWorldState("canGo"), "false") : "canGo should be false in the end!";
        //assert Objects.equals(p1.getWorldState("canGo"), p2.getWorldState("canGo")) : "peers should have the same world state in the end!";
    }
}