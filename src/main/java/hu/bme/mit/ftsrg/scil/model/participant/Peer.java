/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model.participant;

import hu.bme.mit.ftsrg.scil.chaincode.TrainCrossroadContract;
import hu.bme.mit.ftsrg.scil.mockfabric.contract.Context;
import hu.bme.mit.ftsrg.scil.mockfabric.shim.ChaincodeStubImpl;
import hu.bme.mit.ftsrg.scil.model.Channel;
import hu.bme.mit.ftsrg.scil.model.data.Block;
import hu.bme.mit.ftsrg.scil.model.data.Ledger;
import hu.bme.mit.ftsrg.scil.model.data.ReadWriteSet;

import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import static hu.bme.mit.ftsrg.scil.model.participant.SimulationStepResult.CONTINUE;
import static hu.bme.mit.ftsrg.scil.model.participant.SimulationStepResult.NOTHING_TO_DO;

public class Peer extends ParticipantWithId {
  private final Organization org;
  private final Ledger localLedgerCopy = new Ledger();
  // for now, the only kind of request is update state to a given boolean value (which we receive as
  // a string
  private final Queue<String> transactionRequests = new LinkedList<>();
  private final Queue<Block> blocksToValidate = new LinkedList<>();
  private TrainCrossroadContractInstance contractInstance = null;
  private TrainClient client = null;

  public Peer(String id, Organization org) {
    super(id);
    this.org = org;
    org.registerPeer(this);
  }

  @Override
  public SimulationStepResult step() {
    if (transactionRequests.isEmpty() && blocksToValidate.isEmpty()) {
      return NOTHING_TO_DO;
    }

    while (!transactionRequests.isEmpty()) {
      System.out.println("Peer " + id + " is simulating transaction request");
      simulateTransactionRequest();
    }

    while (!blocksToValidate.isEmpty()) {
      System.out.println("Peer " + id + " is processing block");
      processBlock();
    }

    return CONTINUE;
  }

  public void registerClient(TrainClient client) {
    this.client = client;
  }

  public String getId() {
    return id;
  }

  public void installContract(Channel channel) {
    contractInstance = new TrainCrossroadContractInstance(channel);
  }

  public void receiveTransactionRequest(String request) {
    transactionRequests.add(request);
  }

  public void simulateTransactionRequest() {
    if (!transactionRequests.isEmpty()) {
      String requestValue = transactionRequests.remove();

      // at this point the fabric implementation has a lot of interfaces, shims, etc.
      // which are just skipped and simulation is heavily specified and contract specific
      // see contract instance class below.

      // result of simulation is a read-write set:
      // https://hyperledger-fabric.readthedocs.io/en/latest/readwrite.html
      ReadWriteSet rwSet = contractInstance.simulateUpdateStateTransaction(requestValue);

      // send it back to client
      client.receiveRWSet(rwSet);
    }
  }

  public void receiveBlock(Block block) {
    blocksToValidate.add(block);
  }

  public void processBlock() {
    if (!blocksToValidate.isEmpty()) {
      Block nextBlock = blocksToValidate.remove();
      for (ReadWriteSet rwset : nextBlock.getTransactions()) {
        boolean validated = validateTransaction(rwset);
        if (validated) {
          // apply to ledger
          for (Entry<String, String> entry : rwset.getWriteSet().entrySet()) {
            localLedgerCopy.addEntry(entry.getKey(), entry.getValue());
          }
          System.out.println("Transaction applied to ledger, world state in Peer updated");
        }
      }
    }
  }

  public boolean validateTransaction(ReadWriteSet readWriteSet) {
    Map<String, Integer> readSet = readWriteSet.getReadSet();

    for (Map.Entry<String, Integer> entry : readSet.entrySet()) {
      String key = entry.getKey();
      int version = entry.getValue();

      /* MVCC conflict -> invalidate transaction */
      if (localLedgerCopy.getState(key).getVersion() >= version) {
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

  public String getWorldState(String key) {
    return localLedgerCopy.getState(key).getValue();
  }

  // this is the contract specific part
  private class TrainCrossroadContractInstance {
    private final TrainCrossroadContract contract;
    private final Channel channel;

    public TrainCrossroadContractInstance(Channel channel) {
      this.channel = channel;
      this.contract = new TrainCrossroadContract();
    }

    public ReadWriteSet simulateUpdateStateTransaction(String requestValue) {
      ChaincodeStubImpl stub = new ChaincodeStubImpl(localLedgerCopy);
      Context context =
          new Context(stub); // a new context and stubg for simulating each transaction
      contract.updateState(context, requestValue);
      return stub.readWriteSet;
    }
  }
}
