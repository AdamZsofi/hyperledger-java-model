package bme.mit.ftsrg.model;

import bme.mit.ftsrg.model.participants.Node;
import bme.mit.ftsrg.model.participants.Peer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Channel {
    private String channelID;
    private HashMap<String, Node> nodes;

    // TODO do we want to model applications?

    public Channel(String channelID) {
        this.channelID = channelID;
        this.nodes = new HashMap<String, Node>();
    }

    public void registerNode(Node node) {
        if (nodes.containsKey(node.getNodeID())) {
            throw new RuntimeException("Node (id:"+node.getNodeID()+") already registered on channel");
        }

        nodes.put(node.getNodeID(), node);
        node.registerOnChannel(channelID);
    }

    @Override
    public String toString() {
        return "Channel " + channelID + ", nodes: " + nodes;
    }
}
