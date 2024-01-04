package bme.mit.ftsrg.model.channel;

import bme.mit.ftsrg.model.participants.peers.Peer;
import java.util.List;

// TODO generalize for more complicated configs?
public class ChannelConfiguration {
    final List<Peer> endorsingPeers;

    public ChannelConfiguration(List<Peer> endorsingPeers) {
        this.endorsingPeers = endorsingPeers;
    }

    public List<Peer> getEndorsingPeers() {
        return endorsingPeers;
    }
}
