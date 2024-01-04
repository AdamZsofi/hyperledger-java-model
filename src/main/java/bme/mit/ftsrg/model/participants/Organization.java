package bme.mit.ftsrg.model.participants;

import bme.mit.ftsrg.model.participants.peers.Peer;
import java.util.ArrayList;

public class Organization {
    //public static HashMap<String, Organization> organizations;
    public final String id;

    @Override
    public String toString() {
        return "Org "+id+
            ", peers: " + peers;
    }

    private final ArrayList<Peer> peers = new ArrayList<>();

    public Organization(String id) {
        this.id = id;
    }

    public ArrayList<Peer> getPeers() {
        return peers;
    }

    public void registerPeer(Peer peer) {
        peers.add(peer);
    }
}
