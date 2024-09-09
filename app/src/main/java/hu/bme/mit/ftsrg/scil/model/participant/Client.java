/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model.participant;

import static hu.bme.mit.ftsrg.scil.model.participant.SimulationStepResult.CONTINUE;
import static hu.bme.mit.ftsrg.scil.model.participant.SimulationStepResult.NOTHING_TO_DO;

import hu.bme.mit.ftsrg.scil.logging.Logger;
import hu.bme.mit.ftsrg.scil.logging.LoggerType;
import hu.bme.mit.ftsrg.scil.model.data.InvocationRequest;
import hu.bme.mit.ftsrg.scil.model.data.InvocationResult;
import hu.bme.mit.ftsrg.scil.model.data.ReadWriteSet;
import java.util.LinkedList;
import java.util.Queue;

public class Client extends ParticipantWithId {
  private final Peer endorsingPeer;
  private final OrderingService orderingService;
  private final Queue<ReadWriteSet> readWriteSets = new LinkedList<>();
  private final Logger logger;

  public Client(String id, Peer endorsingPeer, OrderingService orderingService) {
    super(id);
    this.orderingService = orderingService;
    this.endorsingPeer = endorsingPeer;
    endorsingPeer.registerClient(this);
    logger = Logger.create(LoggerType.CONSOLE, toString());
  }

  @Override
  public SimulationStepResult step() {
    if (readWriteSets.isEmpty()) {
      return NOTHING_TO_DO;
    }

    while (!readWriteSets.isEmpty()) {
      logger.info("forwarding transaction to orderer");
      forwardTransactionToOrderer();
    }

    return CONTINUE;
  }

  public void sendTransactionRequest(InvocationRequest request) {
    endorsingPeer.receiveInvocationRequest(request);
  }

  public void receiveTransactionResponse(InvocationResult result) {
    readWriteSets.add(result.getRWSet());
  }

  public void forwardTransactionToOrderer() {
    if (!readWriteSets.isEmpty()) {
      orderingService.receiveTransaction(readWriteSets.remove());
    }
  }

  @Override
  public String toString() {
    return super.toString() + String.format("[connected to %s]", endorsingPeer.getId());
  }
}
