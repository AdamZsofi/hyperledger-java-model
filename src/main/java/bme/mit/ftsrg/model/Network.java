package bme.mit.ftsrg.model;

import bme.mit.ftsrg.model.channel.Channel;
import bme.mit.ftsrg.model.participants.Organization;
import bme.mit.ftsrg.model.participants.application.TrainClient;
import bme.mit.ftsrg.model.participants.ordering.OrderingService;
import bme.mit.ftsrg.model.participants.peers.Peer;
import java.util.HashMap;

public class Network {
    private final HashMap<String, Organization> organizations;
    private final HashMap<String, Peer> peers;
    private final HashMap<String, Channel> channels;
    private final HashMap<String, TrainClient> clients;
    private final HashMap<String, OrderingService> orderers;

    public Network(HashMap<String, Organization> organizations, HashMap<String, Peer> peers,
        HashMap<String, Channel> channels, HashMap<String, TrainClient> clients,
        HashMap<String, OrderingService> orderers) {
        this.organizations = organizations;
        this.peers = peers;
        this.channels = channels;
        this.clients = clients;
        this.orderers = orderers;
    }

    @Override
    public String toString() {
        return "Network{" +
            "\npeers: " + peers +
            "\norganizations: " + organizations +
            "\nchannels: " + channels +
            "\nclients: " + clients +
            "\n}";
    }

    public void execute() {
        boolean stop1 = false, stop2 = false;

        while(!(stop1 && stop2)) {
            if(stop1) {
                stop2 = true;
            }
            stop1 = true;
            for (Peer peer : peers.values()) {
                if (peer.step()) {
                    stop1 = false;
                }
            }
            for (TrainClient client : clients.values()) {
                if (client.step()) {
                    stop1 = false;
                }
            }
            for (OrderingService orderer : orderers.values()) {
                if (orderer.step()) {
                    stop1 = false;
                }
            }
            if(!stop1) {
                stop2 = false;
            }
        }

        System.out.println("Network stopped");
    }
}
