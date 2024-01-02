package bme.mit.ftsrg.model.application;

import bme.mit.ftsrg.model.participants.Peer;

// the application instances will be used to initiate transactions through the chaincode
public class Application {
    private final Peer peer;

    public Application(Peer peer) {
        this.peer = peer;
    }
}
