package bme.mit.ftsrg.model.participants;

import bme.mit.ftsrg.model.data.LedgerInstance;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Node {
    protected String nodeID;
    protected Organization org;
    protected final Map<String, LedgerInstance> ledgerInstances;

    public Node(String nodeID, Organization org) {
        this.nodeID = nodeID;
        this.org = org;
        org.registerNode(this);
        ledgerInstances = new HashMap<>();
        // TODO Organization.organizations.get(orgID).registerNode(this);
    }

    public void registerOnChannel(String channelID) {
        if (ledgerInstances.containsKey(channelID)) {
            throw new RuntimeException("Node already registered on channel: "+channelID);
        }
        ledgerInstances.put(channelID, new LedgerInstance(channelID));
    }

    public String getNodeID() {
        return nodeID;
    }
}
