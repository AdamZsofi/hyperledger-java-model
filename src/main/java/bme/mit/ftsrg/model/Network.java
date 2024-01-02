package bme.mit.ftsrg.model;

import bme.mit.ftsrg.model.channel.Channel;
import bme.mit.ftsrg.model.participants.Node;
import bme.mit.ftsrg.model.participants.Organization;
import java.util.HashMap;

public class Network {
    public final HashMap<String, Organization> organizations;
    public final HashMap<String, Node> nodes;
    public final HashMap<String, Channel> channels;

    public Network(HashMap<String, Organization> organizations, HashMap<String, Node> nodes,
        HashMap<String, Channel> channels) {
        this.organizations = organizations;
        this.nodes = nodes;
        this.channels = channels;
    }

    @Override
    public String toString() {
        return "Network{" +
            "\nnodes: " + nodes +
            "\norganizations: " + organizations +
            "\nchannels: " + channels +
            "\n}";
    }
}
