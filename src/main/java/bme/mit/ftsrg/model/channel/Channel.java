package bme.mit.ftsrg.model.channel;

import bme.mit.ftsrg.model.data.Block;
import bme.mit.ftsrg.model.participants.ordering.OrderingService;
import bme.mit.ftsrg.model.participants.application.TrainClient;
import bme.mit.ftsrg.model.participants.peers.Peer;
import java.util.HashMap;
import java.util.List;

public class Channel {
    private final String channelID;
    private final HashMap<String, Peer> peers = new HashMap<>();
    private OrderingService orderingService = null;
    private final TrainClient client = null;

    public Channel(String channelID) {
        this.channelID = channelID;
    }

    public void registerOrderingService(OrderingService orderingService) {
        if (this.orderingService != null) {
            throw new RuntimeException("Channel already has an ordering service registered.");
        }
        this.orderingService = orderingService;
    }

    public void registerPeer(Peer peer) {
        if (peers.containsKey(peer.getPeerId())) {
            throw new RuntimeException("Peer (id:"+peer.getPeerId()+") already registered on channel");
        }

        peers.put(peer.getPeerId(), peer);
    }

    @Override
    public String toString() {
        return "Channel " + channelID + ", peers: " + peers + ", orderer: " + orderingService;
    }

    public OrderingService getOrderingService() {
        return orderingService;
    }

    public TrainClient getClient() {
        return client;
    }

    public void broadcastBlock(Block block) {
        for (Peer peer : peers.values()) {
            peer.receiveBlock(block);
        }
    }
}
