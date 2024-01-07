package bme.mit.ftsrg.model.participants.application;

import bme.mit.ftsrg.model.NetworkParticipant;
import bme.mit.ftsrg.model.data.ReadWriteSet;
import bme.mit.ftsrg.model.participants.ordering.OrderingService;
import bme.mit.ftsrg.model.participants.peers.Peer;
import java.util.LinkedList;
import java.util.Queue;

// the application instances will be used to initiate transactions through the chaincode
public class TrainClient implements NetworkParticipant {
    private final String clientId;
    private final Peer peer;
    private final OrderingService orderingService;
    private final Queue<ReadWriteSet> readWriteSets = new LinkedList<ReadWriteSet>();

    public TrainClient(String clientId, Peer peer, OrderingService orderingService) {
        this.clientId = clientId;
        this.orderingService = orderingService;
        this.peer = peer;
        peer.registerClient(this);
    }

    @Override
    public boolean step() {
        if(readWriteSets.isEmpty()) return false;
        while(!readWriteSets.isEmpty()) {
            System.out.println("Client "+clientId+" is forwarding transaction to orderer");
            forwardTransactionToOrderer();
        }
        return true;
    }

    public void updateCrossroadState(boolean canGo) {
        String canGoStr;
        if (canGo) {
            canGoStr  ="true";
        } else {
            canGoStr  = "false";
        }
        // message peer about update
        sendToPeer(canGoStr);
        // In this example, for now, this peer is the only endorser,
        // so we don't need to send the request to any others
    }

    private void sendToPeer(String canGoStr) {
        peer.receiveTransactionRequest(canGoStr);
    }

    public void receiveRWSet(ReadWriteSet readWriteSet) {
        readWriteSets.add(readWriteSet);
    }

    public void forwardTransactionToOrderer() {
        if (!readWriteSets.isEmpty()) {
            orderingService.receiveTransaction(readWriteSets.remove());
        }
    }

    @Override
    public String toString() {
        return "Train Client "+clientId+", connected to "+peer.getPeerId();
    }
}
