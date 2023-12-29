package bme.mit.ftsrg;

import bme.mit.ftsrg.model.participants.OrderingService;
import bme.mit.ftsrg.model.participants.Organization;
import bme.mit.ftsrg.model.participants.Peer;

public class Main {

    public static void main(String[] args) {
        Organization r1 = new Organization("R1");
        Organization r2 = new Organization("R2");
        Organization r3 = new Organization("R3");
        Peer p1 = new Peer("P1", "R1");
        Peer p2 = new Peer("P2", "R2");
        OrderingService p3 = new OrderingService("P3", "R3");
    }
}