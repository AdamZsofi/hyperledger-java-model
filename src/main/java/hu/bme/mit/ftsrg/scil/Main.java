/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil;

import hu.bme.mit.ftsrg.scil.model.Network;
import hu.bme.mit.ftsrg.scil.model.participant.TrainClient;
import hu.bme.mit.ftsrg.scil.model.participant.OrderingService;
import hu.bme.mit.ftsrg.scil.model.participant.Peer;

import java.util.List;
import java.util.Objects;

public class Main {
  // TODO configure here (and check assertion below at end of main)
  public static OrderingService.FaultMode ordererFaultMode = OrderingService.FaultMode.CAN_DROP;
  public static int blockSize = 2;

  public static void main(String[] args) {
    Network network =
        Network.builder()
            .addOrganization("R1")
            .addOrganization("R2")
            .addPeer("P1", "R1")
            .addPeer("P2", "R2")
            .addOrderingService("O1", blockSize, ordererFaultMode)
            .addChannel("C1")
            .registerPeersToChannel(List.of("P1", "P2"), "C1")
            .installContract("P1", "C1")
            .registerOrderingServiceToChannel("O1", "C1")
            .addClient("Client", "P1", "O1")
            .build();
    System.out.println(network);

    TrainClient client = network.getClient("Client");
    client.updateCrossroadState(false); // init
    client.updateCrossroadState(true); // no train coming
    client.updateCrossroadState(false); // there is a train coming
    client.updateCrossroadState(false); // there is a train coming
    client.updateCrossroadState(true); // no train coming
    client.updateCrossroadState(false); // there is a train coming
    network.execute();

    Peer p1 = network.getPeer("P1");
    assert Objects.equals(p1.getWorldState("canGo"), "false") : "canGo should be false in the end!";

    // assert Objects.equals(p1.getWorldState("canGo"), "true") : "canGo should be true in the
    // end!";
    // assert Objects.equals(p2.getWorldState("canGo"), "false") : "canGo should be false in the
    // end!";
    // assert Objects.equals(p1.getWorldState("canGo"), p2.getWorldState("canGo")) : "peers should
    // have the same world state in the end!";
  }
}
