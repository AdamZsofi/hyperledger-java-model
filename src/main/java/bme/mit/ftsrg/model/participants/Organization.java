package bme.mit.ftsrg.model.participants;

import java.util.ArrayList;
import java.util.HashMap;

public class Organization {
    //public static HashMap<String, Organization> organizations;
    public final String id;

    private final ArrayList<Node> nodes = new ArrayList<>();

    public Organization(String id) {
        this.id = id;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public void registerNode(Node node) {
        nodes.add(node);
    }
}
