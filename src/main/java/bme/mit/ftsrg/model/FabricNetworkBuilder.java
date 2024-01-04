package bme.mit.ftsrg.model;

import bme.mit.ftsrg.model.channel.Channel;
import bme.mit.ftsrg.model.participants.OrderingService;
import bme.mit.ftsrg.model.participants.Organization;
import bme.mit.ftsrg.model.participants.application.Application;
import bme.mit.ftsrg.model.participants.peers.Peer;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FabricNetworkBuilder {
    public HashMap<String, OrderingService> orderers;
    public HashMap<String, Organization> organizations;
    public HashMap<String, Peer> peers;
    public HashMap<String, Channel> channels;

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

        Network n = new Network(organizations, peers, channels);
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

    public void addChannel(String channelId, List<String> endorsingPeerIds) {
        if(channels.containsKey(channelId)) {
            throw new RuntimeException("Channel with this id already exists: "+ channelId);
        }
        List<Peer> endorsingPeers = endorsingPeerIds.stream().map(s -> peers.get(s)).collect(Collectors.toList());
        channels.put(channelId, new Channel(channelId, endorsingPeers));
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

    public void addOrderingService(String orderingServiceId) {
        orderers.put(orderingServiceId, new OrderingService(orderingServiceId));
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
    }
}
