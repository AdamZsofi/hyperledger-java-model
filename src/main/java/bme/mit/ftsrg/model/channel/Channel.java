package bme.mit.ftsrg.model.channel;

import bme.mit.ftsrg.model.participants.OrderingService;
import bme.mit.ftsrg.model.participants.application.Application;
import bme.mit.ftsrg.model.participants.peers.Peer;
import bme.mit.ftsrg.model.participants.peers.TransactionExecutionStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Channel {
    private final String channelID;
    private final HashMap<String, Peer> peers = new HashMap<>();
    private OrderingService orderingService = null;
    private final ChannelConfiguration cc;
    private final Application app = new Application(this);

    public Channel(String channelID, List<Peer> endorsingPeers) {
        this.channelID = channelID;
        this.cc = new ChannelConfiguration(endorsingPeers);
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
        peer.registerOnChannel(channelID);
    }

    @Override
    public String toString() {
        return "Channel " + channelID + ", peers: " + peers + ", orderer: " + orderingService;
    }

    public OrderingService getOrderingService() {
        return orderingService;
    }

    public Application getApplication() {
        return app;
    }

    public Envelope endorse(String key, byte[] value) {
        List<TransactionExecutionStatus> endorsements = new ArrayList<>();
        for (Peer peer : cc.getEndorsingPeers()) {
            // TODO should be async. comm.?
            endorsements.add(peer.endorseTransaction(key, value));
        }
        
        // TODO create envelope:
        // TODO what to do with this? e.g. What happens if not endorsed by all?
        return new Envelope();
    }
}
