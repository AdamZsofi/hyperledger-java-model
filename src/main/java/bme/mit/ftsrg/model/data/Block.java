package bme.mit.ftsrg.model.data;

import java.util.List;

public class Block {
    private final List<ReadWriteSet> transactions;

    public Block(List<ReadWriteSet> transactions) {
        this.transactions = transactions;
    }

    public List<ReadWriteSet> getTransactions() {
        return transactions;
    }
}
