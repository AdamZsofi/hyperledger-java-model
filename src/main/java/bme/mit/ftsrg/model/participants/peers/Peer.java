package bme.mit.ftsrg.model.participants.peers;

import bme.mit.ftsrg.model.channel.Channel;
import bme.mit.ftsrg.model.participants.Organization;
import bme.mit.ftsrg.model.data.ChaincodeInstance;
import bme.mit.ftsrg.model.data.LedgerInstance;
import bme.mit.ftsrg.model.participants.application.Application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Peer {
    // TODO missing: what chaincode does this peer have installed?
    // TODO assumption for now: it has the chaincode installed if it's on the channel (this is not valid in general)
    private final String peerId;
    private final Organization org;
    private final Map<String, LedgerInstance> ledgerInstances;

    public Peer(String peerId, Organization org) {
        this.peerId = peerId;
        this.org = org;
        org.registerPeer(this);
        ledgerInstances = new HashMap<>();
    }

    public void registerOnChannel(String channelID) {
        if (ledgerInstances.containsKey(channelID)) {
            throw new RuntimeException("Peer already registered on channel: "+channelID);
        }
        ledgerInstances.put(channelID, new LedgerInstance(channelID));
    }

    public String getPeerId() {
        return peerId;
    }

    // TODO check if right chaincode..?
    public TransactionExecutionStatus endorseTransaction(String key, byte[] value) {
        // TODO implement transaction execution
        return TransactionExecutionStatus.SIGNED;
    }

    public void commitTransaction(Object transaction) {
        // Simulate committing logic
        // Update ledger, perform validation, etc.
        System.out.println("Transaction committed by Peer: " + peerId);
    }

    @Override
    public String toString() {
        return peerId;
    }
}
