package bme.mit.ftsrg.model.participants;

import bme.mit.ftsrg.model.TransactionStatus;
import bme.mit.ftsrg.model.data.ChaincodeInstance;
import bme.mit.ftsrg.model.data.LedgerInstance;
import java.util.ArrayList;
import java.util.List;

public class Peer extends Node {
    private final List<LedgerInstance> ledgerInstances;
    private final List<ChaincodeInstance> chaincodes;

    public Peer(String nodeID, Organization org) {
        super(nodeID, org);
        ledgerInstances = new ArrayList<>();
        chaincodes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Peer "+nodeID;
    }

    public TransactionStatus endorseTransaction(Object transaction) {
        // Simulate endorsement logic
        // TODO
        if (true) {
            // Additional endorsement logic as needed
            return TransactionStatus.VALID;
        } else {
            return TransactionStatus.INVALID;
        }
    }

    public void commitTransaction(Object transaction) {
        // Simulate committing logic
        // Update ledger, perform validation, etc.
        System.out.println("Transaction committed by Peer: " + nodeID);
    }
}
