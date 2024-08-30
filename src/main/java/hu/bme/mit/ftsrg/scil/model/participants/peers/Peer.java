/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model.participants.peers;

import hu.bme.mit.ftsrg.scil.chaincode.TrainCrossroadContract;
import hu.bme.mit.ftsrg.scil.mockFabric.Context;
import hu.bme.mit.ftsrg.scil.mockFabric.TrainCrossroadChaincodeStub;
import hu.bme.mit.ftsrg.scil.model.NetworkParticipant;
import hu.bme.mit.ftsrg.scil.model.channel.Channel;
import hu.bme.mit.ftsrg.scil.model.data.Block;
import hu.bme.mit.ftsrg.scil.model.data.Ledger;
import hu.bme.mit.ftsrg.scil.model.data.ReadWriteSet;
import hu.bme.mit.ftsrg.scil.model.participants.Organization;
import hu.bme.mit.ftsrg.scil.model.participants.application.TrainClient;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class Peer implements NetworkParticipant {
  private final String peerId;
  private final Organization org;
  private final Ledger localLedgerCopy = new Ledger();
  // for now, the only kind of request is update state to a given boolean value (which we receive as
  // a string
  private final Queue<String> transactionRequests = new LinkedList<String>();
  private final Queue<Block> blocksToValidate = new LinkedList<>();
  private TrainCrossroadContractInstance contractInstance = null;
  private TrainClient client = null;

  public Peer(String peerId, Organization org) {
    this.peerId = peerId;
    this.org = org;
    org.registerPeer(this);
  }

  @Override
  public boolean step() {
    if (transactionRequests.isEmpty() && blocksToValidate.isEmpty()) {
      return false;
    }
    while (!transactionRequests.isEmpty()) {
      System.out.println("Peer " + peerId + " is simulating transaction request");
      simulateTransactionRequest();
    }
    while (!blocksToValidate.isEmpty()) {
      System.out.println("Peer " + peerId + " is processing block");
      processBlock();
    }
    return true;
  }

  public void registerClient(TrainClient client) {
    this.client = client;
  }

  public String getPeerId() {
    return peerId;
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

    if (!readSet.isEmpty()) {
      for (HashMap.Entry<String, Integer> entry : readSet.entrySet()) {
        String key = entry.getKey();
        int version = entry.getValue();

        if (localLedgerCopy.getState(key).getVersion() >= version) {
          return false;
        }
      }
      // All entries passed validation
      return true;
    } else {
      // No entries in the writeSet, consider it valid
      return true;
    }
  }

  @Override
  public String toString() {
    if (contractInstance == null) {
      return peerId;
    } else {
      return peerId + " with contract";
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
      TrainCrossroadChaincodeStub stub = new TrainCrossroadChaincodeStub(localLedgerCopy);
      Context context =
          new Context(stub); // a new context and stubg for simulating each transaction
      contract.updateState(context, requestValue);
      return stub.readWriteSet;
    }
  }
}
