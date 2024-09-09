/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model.participant;

import static hu.bme.mit.ftsrg.scil.model.participant.SimulationStepResult.CONTINUE;
import static hu.bme.mit.ftsrg.scil.model.participant.SimulationStepResult.NOTHING_TO_DO;

import hu.bme.mit.ftsrg.scil.logging.Logger;
import hu.bme.mit.ftsrg.scil.logging.LoggerType;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.shim.ChaincodeStubImpl;
import hu.bme.mit.ftsrg.scil.model.Channel;
import hu.bme.mit.ftsrg.scil.model.data.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class Peer extends ParticipantWithId {
  private final Organization org;
  private final Ledger ledger = new Ledger();
  private final Queue<InvocationRequest> invocationRequests = new LinkedList<>();
  private final Queue<Block> blocksToValidate = new LinkedList<>();
  private ContractInstance contractInstance = null;
  private Client client = null;
  private final Logger logger;

  public Peer(String id, Organization org) {
    super(id);
    this.org = org;
    org.registerPeer(this);
    logger = Logger.create(LoggerType.CONSOLE, toString());
  }

  @Override
  public SimulationStepResult step() {
    if (invocationRequests.isEmpty() && blocksToValidate.isEmpty()) {
      return NOTHING_TO_DO;
    }

    while (!invocationRequests.isEmpty()) {
      logger.info("simulating transaction request");
      simulateTransactionRequest();
    }

    while (!blocksToValidate.isEmpty()) {
      logger.info("processing block");
      processBlock();
    }

    return CONTINUE;
  }

  public void registerClient(Client client) {
    this.client = client;
  }

  public String getId() {
    return id;
  }

  public void installContract(ContractInterface contract, Channel channel) {
    contractInstance = new ContractInstance(contract, channel);
  }

  public void receiveInvocationRequest(InvocationRequest request) {
    invocationRequests.add(request);
  }

  public void simulateTransactionRequest() {
    if (invocationRequests.isEmpty()) {
      return;
    }

    /* Get the next request */
    InvocationRequest request = invocationRequests.poll();

    /* Invoke the contract with a context including the stub and the parameters */
    InvocationResult result = contractInstance.simulateUpdateStateTransaction(request);

    /* Send the invocation results back to the peer (the returned value and the resulting read-write set) */
    client.receiveTransactionResponse(result);
  }

  public void receiveBlock(Block block) {
    blocksToValidate.add(block);
  }

  public void processBlock() {
    if (blocksToValidate.isEmpty()) {
      return;
    }

    Block nextBlock = blocksToValidate.poll();
    for (ReadWriteSet rwset : nextBlock.getTransactions()) {
      if (isTransactionValid(rwset)) {
        for (Entry<String, byte[]> entry : rwset.getWriteSet().entrySet()) {
          ledger.addEntry(entry.getKey(), entry.getValue());
        }

        logger.info("transaction applied to ledger, world state in peer updated");
      }
    }
  }

  public boolean isTransactionValid(ReadWriteSet readWriteSet) {
    Map<String, Integer> readSet = readWriteSet.getReadSet();

    for (Entry<String, Integer> entry : readSet.entrySet()) {
      String key = entry.getKey();
      int version = entry.getValue();

      /* MVCC conflict -> invalidate transaction */
      if (ledger.getState(key).getVersion() >= version) {
        return false;
      }
    }

    /* No conflicts (or empty read set) -> consider tx valid */
    return true;
  }

  @Override
  public String toString() {
    if (contractInstance == null) {
      return super.toString();
    } else {
      return super.toString() + "[with contract]";
    }
  }

  public byte[] getWorldState(String key) {
    return ledger.getState(key).getValue();
  }

  private class ContractInstance {
    private final ContractInterface contract;
    private final Channel channel;

    public ContractInstance(ContractInterface contract, Channel channel) {
      this.channel = channel;
      this.contract = contract;
    }

    public InvocationResult simulateUpdateStateTransaction(InvocationRequest request) {
      ChaincodeStubImpl stub = new ChaincodeStubImpl(new Ledger(ledger));
      Object result = contractInstance.getContract().invoke(contract.createContext(stub), request);
      return new InvocationResult(result, stub.getReadWriteSet());
    }

    public ContractInterface getContract() {
      return contract;
    }
  }
}
