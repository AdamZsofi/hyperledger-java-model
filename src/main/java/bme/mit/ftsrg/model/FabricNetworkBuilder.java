package bme.mit.ftsrg.model;

import bme.mit.ftsrg.model.channel.Channel;
import bme.mit.ftsrg.model.participants.ordering.FaultMode;
import bme.mit.ftsrg.model.participants.ordering.OrderingService;
import bme.mit.ftsrg.model.participants.Organization;
import bme.mit.ftsrg.model.participants.application.TrainClient;
import bme.mit.ftsrg.model.participants.peers.Peer;
import java.util.HashMap;

public class FabricNetworkBuilder {
    public HashMap<String, OrderingService> orderers;
    public HashMap<String, Organization> organizations;
    public HashMap<String, Peer> peers;
    public HashMap<String, Channel> channels;
    public HashMap<String, TrainClient> clients;

    public FabricNetworkBuilder() {
        resetBuild();
    }

    public void resetBuild() {
        organizations = new HashMap<>();
        peers = new HashMap<>();
        channels = new HashMap<>();
        orderers = new HashMap<>();
    }

    public Network build() {
        for(Channel c : channels.values()) {
            if(c.getOrderingService() == null) {
                throw new RuntimeException("Each channel must have exactly one ordering service. Register one with the builder.");
            }
        }

        Network n = new Network(organizations, peers, channels, clients);
        resetBuild();
        return n;
    }

    public void addOrganization(String orgId) {
        if(organizations.containsKey(orgId)) {
            throw new RuntimeException("Organization with this id already exists: "+ orgId);
        }
        organizations.put(orgId, new Organization(orgId));
    }

    public void addPeer(String peerId, String orgId) {
        if(!organizations.containsKey(orgId)) {
            throw new RuntimeException("Organization with this id does not exist: "+ orgId);
        }
        if(peers.containsKey(peerId)) {
            throw new RuntimeException("Peer with this id already exists: "+ peerId);
        }

        peers.put(peerId, new Peer(peerId, organizations.get(orgId)));
    }

    public void addChannel(String channelId) {
        if(channels.containsKey(channelId)) {
            throw new RuntimeException("Channel with this id already exists: "+ channelId);
        }
    }

    public void registerPeersToChannel(Iterable<String> peerIds, String channelId) {
        Channel channel = channels.get(channelId);
        if (channel == null) {
            throw new RuntimeException("Channel with this id does not exist: "+ channelId);
        }
        for (String peerId : peerIds) {
            if (peers.get(peerId) == null) {
                throw new RuntimeException("Peer can not be registered, it does not exist: "+peerId);
            }
            channel.registerPeer(peers.get(peerId));
        }
    }

    public void addOrderingService(String orderingServiceId, int blockSize, FaultMode faultMode) {
        orderers.put(orderingServiceId, new OrderingService(orderingServiceId, blockSize, faultMode));
    }

    // we will only allow one ordering service per channel
    // but one ordering service can be registered to several channels
    public void registerOrderingServiceToChannel(String orderingServiceId, String channelId) {
        Channel channel = channels.get(channelId);
        if (channel == null) {
            throw new RuntimeException("Channel with this id does not exist: "+ channelId);
        }
        if (orderers.get(orderingServiceId) == null) {
            throw new RuntimeException("Orderer with this id does not exist: "+ orderingServiceId);
        }
        channel.registerOrderingService(orderers.get(orderingServiceId));
        orderers.get(orderingServiceId).registerToChannel(channel);
    }

    public void installContract(String peerId, String channelId) {
        if (peers.get(peerId) == null) {
            throw new RuntimeException("Peer can not be registered, it does not exist: "+peerId);
        }
        if (channels.get(channelId) == null) {
            throw new RuntimeException("Channel with this id does not exist: "+ channelId);
        }
        peers.get(peerId).installContract(channels.get(channelId));
    }

    public void addClient(String clientId, String peerId, String ordererId) {
        if (peers.get(peerId) == null) {
            throw new RuntimeException("Peer can not be registered, it does not exist: "+peerId);
        }
        clients.put(clientId, new TrainClient(clientId, peers.get(peerId), orderers.get(ordererId)));
    }
}
