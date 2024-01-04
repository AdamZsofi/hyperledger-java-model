package bme.mit.ftsrg.model.participants.peers;

import bme.mit.ftsrg.model.participants.Organization;
import bme.mit.ftsrg.model.data.ChaincodeInstance;
import bme.mit.ftsrg.model.data.LedgerInstance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Peer {
    private final String peerId;
    private final Organization org;
    private final Map<String, LedgerInstance> ledgerInstances;

    private final List<ChaincodeInstance> chaincodes;

    public Peer(String peerId, Organization org) {
        this.peerId = peerId;
        this.org = org;
        org.registerPeer(this);
        ledgerInstances = new HashMap<>();
        chaincodes = new ArrayList<>();
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

    @Override
    public String toString() {
        return peerId;
    }

    public void endorseTransaction(Object transaction) {
        // Simulate endorsement logic
        // TODO
    }

    public void commitTransaction(Object transaction) {
        // Simulate committing logic
        // Update ledger, perform validation, etc.
        System.out.println("Transaction committed by Peer: " + peerId);
    }
}
