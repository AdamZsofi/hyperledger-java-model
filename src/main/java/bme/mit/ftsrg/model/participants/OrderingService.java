package bme.mit.ftsrg.model.participants;

import java.util.List;

public class OrderingService extends Node {

    public OrderingService(String nodeID, String orgID) {
        super(nodeID, orgID);
    }

    public void createBlock(List<Object> transactions) {
        // Simulate block creation and ordering logic
        System.out.println("Block created with transactions: " + transactions);
    }
}
