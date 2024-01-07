package bme.mit.ftsrg.model.participants.peers;

import bme.mit.ftsrg.chaincode.TrainCrossroadContract;
import bme.mit.ftsrg.model.data.Block;
import bme.mit.ftsrg.model.data.Ledger;
import bme.mit.ftsrg.model.data.ReadWriteSet;
import bme.mit.ftsrg.mockFabric.TrainCrossroadChaincodeStub;
import bme.mit.ftsrg.mockFabric.Context;
import bme.mit.ftsrg.model.channel.Channel;
import bme.mit.ftsrg.model.participants.Organization;
import bme.mit.ftsrg.model.participants.application.TrainClient;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

public class Peer {
    private final String peerId;
    private final Organization org;
    private final Ledger localLedgerCopy = new Ledger();
    private TrainCrossroadContractInstance contractInstance = null;
    // for now, the only kind of request is update state to a given boolean value (which we receive as a string
    private final Queue<String> transactionRequests = new LinkedList<String>();
    private final Queue<Block> blocksToValidate = new LinkedList<>();
    private TrainClient client = null;

    public Peer(String peerId, Organization org) {
        this.peerId = peerId;
        this.org = org;
        org.registerPeer(this);
    }

    public void registerClient(TrainClient client) {
        this.client = client;
    }

    public String getPeerId() {
        return peerId;
    }

    public void installContract(Channel channel) {
        contractInstance = new TrainCrossroadContractInstance(channel, localLedgerCopy);
    }

    public void receiveTransactionRequest(String request) {
        transactionRequests.add(request);
    }

    public void receiveBlock(Block block) {
        blocksToValidate.add(block);
    }

    public void processBlock() {
        // TODO update local ledger handling (get it out of stub? or not?)
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

    public void simulateTransactionRequest() {
        if (!transactionRequests.isEmpty()) {
            String requestValue = transactionRequests.remove();

            // at this point the fabric implementation has a lot of interfaces, shims, etc.
            // which are just skipped and simulation is heavily specified and contract specific
            // see contract instance class below.

            // result of simulation is a read-write set:
            // https://hyperledger-fabric.readthedocs.io/en/latest/readwrite.html
            ReadWriteSet rwSet = contractInstance.simulateUpdateStateTransaction(contractInstance.context, requestValue);

            // send it back to client
            client.receiveRWSet(rwSet);
        }
    }

    @Override
    public String toString() {
        if (contractInstance == null) {
            return peerId;
        } else {
            return peerId+" with contract";
        }
    }

    // this is the contract specific part
    private static class TrainCrossroadContractInstance {
        private final TrainCrossroadContract contract;
        private final Context context;
        private final Channel channel;

        public TrainCrossroadContractInstance(Channel channel, Ledger peerLedgerCopy) {
            this.channel = channel;
            this.contract = new TrainCrossroadContract();
            this.context = new Context(new TrainCrossroadChaincodeStub(peerLedgerCopy));
        }

        public ReadWriteSet simulateUpdateStateTransaction(Context context, String requestValue) {
            contract.updateState(context, requestValue);
            // in this case, r/w set only has a single write
            ReadWriteSet rwset = new ReadWriteSet();
            rwset.addWrite("canGo", requestValue);
            return rwset;
        }
    }
}
