package bme.mit.ftsrg.model.participants;

import java.util.List;

/**
 * This is the model for the ordering service
 * In reality this is a cluster of nodes running the Raft protocol,
 * but we will handle ordering as a black box with different failure modes
 */
public class OrderingService {
    private final String id;

    public OrderingService(String id) {
        this.id = id;
    }

    // TODO model ordering - transaction queue..?

    public void createBlock(List<Object> transactions) {
        // Simulate block creation and ordering logic
        System.out.println("Block created with transactions: " + transactions);
    }

    @Override
    public String toString() {
        return id;
    }

}
