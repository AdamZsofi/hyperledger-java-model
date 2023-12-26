package bme.mit.ftsrg.model;

import java.util.List;

public class Channel {
    private String name;
    private List<Peer> peers;

    public Channel(String name, List<Peer> peers) {
        this.name = name;
        this.peers = peers;
    }

    public void addToChannel(Peer peer) {
        peers.add(peer);
    }

    // Additional channel-specific methods as needed
}
