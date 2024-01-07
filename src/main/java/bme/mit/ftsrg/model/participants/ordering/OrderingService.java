package bme.mit.ftsrg.model.participants.ordering;

import bme.mit.ftsrg.model.channel.Channel;
import bme.mit.ftsrg.model.data.Block;
import bme.mit.ftsrg.model.data.ReadWriteSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * This is the model for the ordering service
 * In reality this is a cluster of nodes running the Raft protocol,
 * but we will handle ordering as a black box with different failure modes
 */
public class OrderingService {
    private final String id;
    private final List<ReadWriteSet> transactions = new ArrayList<>();
    private final int blockSize;
    private final FaultMode faultMode;
    private Channel channel = null;

    public OrderingService(String id, int blockSize, FaultMode faultMode) {
        this.blockSize = blockSize;
        this.id = id;
        this.faultMode = faultMode;
    }

    public void receiveTransaction(ReadWriteSet readWriteSet) {
        if(faultMode == FaultMode.allFaults || faultMode == FaultMode.canLose) {
            Random random = new Random();
            double randomValue = random.nextDouble();
            if (randomValue < 0.5) {
                transactions.add(readWriteSet);
            }
        } else {
            transactions.add(readWriteSet);
        }
    }

    public void orderTransactions() {
        // TODO do I need anything here to be atomic, thread safe, etc.?

        List<ReadWriteSet> transactionsToOrder;
        // Simulate block creation and ordering logic
        if (transactions.size() == blockSize) {
            transactionsToOrder = new ArrayList<>(transactions);
            transactions.clear();
        } else if (transactions.size() > blockSize) {
            // Take the first blockSize transactions, create a list from those,
            // and set transactions to the rest of the transactions
            transactionsToOrder = new ArrayList<>(transactions.subList(0, blockSize));
            List<ReadWriteSet> restOfTransactions = new ArrayList<>(transactions.subList(blockSize, transactions.size()));

            transactions.clear();
            transactions.addAll(restOfTransactions);
            // Now 'firstBlock' contains the first blockSize transactions,
            // and 'transactions' contains the rest of the transactions.
        } else {
            return;
        }

        if(faultMode == FaultMode.canReorder || faultMode == FaultMode.allFaults) {
            Collections.shuffle(transactionsToOrder);
        }

        Block block = new Block(transactionsToOrder);

        // send new block to peers
        channel.broadcastBlock(block);
    }

    @Override
    public String toString() {
        return id;
    }

    public void registerToChannel(Channel channel) {
        this.channel = channel;
    }
}
