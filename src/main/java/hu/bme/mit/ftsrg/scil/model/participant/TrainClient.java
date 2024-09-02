/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model.participant;

import static hu.bme.mit.ftsrg.scil.model.participant.SimulationStepResult.CONTINUE;
import static hu.bme.mit.ftsrg.scil.model.participant.SimulationStepResult.NOTHING_TO_DO;

import hu.bme.mit.ftsrg.scil.model.data.ReadWriteSet;

import java.util.LinkedList;
import java.util.Queue;

// the application instances will be used to initiate transactions through the chaincode
public class TrainClient extends ParticipantWithId {
  private final Peer peer;
  private final OrderingService orderingService;
  private final Queue<ReadWriteSet> readWriteSets = new LinkedList<>();

  public TrainClient(String id, Peer peer, OrderingService orderingService) {
    super(id);
    this.orderingService = orderingService;
    this.peer = peer;
    peer.registerClient(this);
  }

  @Override
  public SimulationStepResult step() {
    if (readWriteSets.isEmpty()) {
      return NOTHING_TO_DO;
    }

    while (!readWriteSets.isEmpty()) {
      System.out.println("Client " + id + " is forwarding transaction to orderer");
      forwardTransactionToOrderer();
    }

    return CONTINUE;
  }

  public void updateCrossroadState(boolean canGo) {
    String canGoStr;
    if (canGo) {
      canGoStr = "true";
    } else {
      canGoStr = "false";
    }

    /* In this example, for now, the one peer is the only endorser, so we don't need to send the request to any others */
    sendToPeer(canGoStr);
  }

  private void sendToPeer(String canGoStr) {
    peer.receiveTransactionRequest(canGoStr);
  }

  public void receiveRWSet(ReadWriteSet readWriteSet) {
    readWriteSets.add(readWriteSet);
  }

  public void forwardTransactionToOrderer() {
    if (!readWriteSets.isEmpty()) {
      orderingService.receiveTransaction(readWriteSets.remove());
    }
  }

  @Override
  public String toString() {
    return super.toString() + String.format("[connected to %s]", peer.getId());
  }
}
