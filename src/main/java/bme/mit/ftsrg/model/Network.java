package bme.mit.ftsrg.model;

import bme.mit.ftsrg.model.channel.Channel;
import bme.mit.ftsrg.model.participants.OrderingService;
import bme.mit.ftsrg.model.participants.Organization;
import bme.mit.ftsrg.model.participants.peers.Peer;
import java.util.HashMap;

public class Network {
    private final HashMap<String, Organization> organizations;
    private final HashMap<String, Peer> peers;
    private final HashMap<String, Channel> channels;

    public Network(HashMap<String, Organization> organizations, HashMap<String, Peer> peers,
        HashMap<String, Channel> channels) {
        this.organizations = organizations;
        this.peers = peers;
        this.channels = channels;
    }

    @Override
    public String toString() {
        return "Network{" +
            "\npeers: " + peers +
            "\norganizations: " + organizations +
            "\nchannels: " + channels +
            "\n}";
    }
}
