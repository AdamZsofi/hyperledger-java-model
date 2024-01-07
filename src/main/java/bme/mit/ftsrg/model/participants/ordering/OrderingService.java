package bme.mit.ftsrg.model.participants.ordering;

import bme.mit.ftsrg.model.NetworkParticipant;
import bme.mit.ftsrg.model.channel.Channel;
import bme.mit.ftsrg.model.data.Block;
import bme.mit.ftsrg.model.data.ReadWriteSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * This is the model for the ordering service
 * In reality this is a cluster of nodes running the Raft protocol,
 * but we will handle ordering as a black box with different failure modes
 */
public class OrderingService implements NetworkParticipant {
    private final String id;
    private final Queue<ReadWriteSet> transactions = new LinkedList<>();
    private final int blockSize;
    private final FaultMode faultMode;
    private Channel channel = null;

    public OrderingService(String id, int blockSize, FaultMode faultMode) {
        this.blockSize = blockSize;
        this.id = id;
        this.faultMode = faultMode;
    }

    @Override
    public boolean step() {
        if(transactions.size()<blockSize) {
            return false;
        }
        while(transactions.size()>=blockSize) {
            System.out.println("Orderer "+id+" is ordering a new block");
            orderTransactions();
        }
        return true;
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
        List<ReadWriteSet> transactionsToOrder;
        // Simulate block creation and ordering logic
        if (transactions.size() == blockSize) {
            transactionsToOrder = new ArrayList<>(transactions);
            transactions.clear();
        } else if (transactions.size() > blockSize) {
            transactionsToOrder = new ArrayList<>();
            int count = 0;
            while (count < blockSize && !transactions.isEmpty()) {
                transactionsToOrder.add(transactions.remove());
                count++;
            }
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
