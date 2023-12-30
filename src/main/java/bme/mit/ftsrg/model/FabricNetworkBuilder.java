package bme.mit.ftsrg.model;

import bme.mit.ftsrg.model.participants.Node;
import bme.mit.ftsrg.model.participants.OrderingService;
import bme.mit.ftsrg.model.participants.Organization;
import bme.mit.ftsrg.model.participants.Peer;
import java.util.HashMap;

public class FabricNetworkBuilder {
    public HashMap<String, Organization> organizations;
    public HashMap<String, Node> nodes;
    public HashMap<String, Channel> channels;

    public FabricNetworkBuilder() {
        resetBuild();
    }

    public void resetBuild() {
        organizations = new HashMap<>();
        nodes = new HashMap<>();
        channels = new HashMap<>();
    }

    public Network build() {
        Network n = new Network(organizations, nodes, channels);
        resetBuild();
        return n;
    }

    public void addOrganization(String orgID) {
        if(organizations.containsKey(orgID)) {
            throw new RuntimeException("Organization with this id already exists: "+ orgID);
        }
        organizations.put(orgID, new Organization(orgID));
    }

    public void addPeer(String nodeID, String orgID) {
        if(!organizations.containsKey(orgID)) {
            throw new RuntimeException("Organization with this id does not exist: "+ orgID);
        }
        if(nodes.containsKey(nodeID)) {
            throw new RuntimeException("Node with this id already exists: "+ nodeID);
        }

        nodes.put(nodeID, new Peer(nodeID, organizations.get(orgID)));
    }

    public void addOrderingService(String nodeID, String orgID) {
        if(!organizations.containsKey(orgID)) {
            throw new RuntimeException("Organization with this id does not exist: "+ orgID);
        }
        if(nodes.containsKey(nodeID)) {
            throw new RuntimeException("Node with this id already exists: "+ nodeID);
        }

        nodes.put(nodeID, new OrderingService(nodeID, organizations.get(orgID)));
    }

    public void addChannel(String channelID) {
        if(channels.containsKey(channelID)) {
            throw new RuntimeException("Node with this id already exists: "+ channelID);
        }
        channels.put(channelID, new Channel(channelID));
    }

    public void registerNodesToChannel(Iterable<String> nodeIDs, String channelID) {
        Channel channel = channels.get(channelID);
        if (channel == null) {
            throw new RuntimeException("Channel with this id does not exist: "+ channelID);
        }
        for (String nodeID : nodeIDs) {
            channel.registerNode(nodes.get(nodeID));
        }
    }

    // TODO chaincode
}
