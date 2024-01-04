// WARNING: MOCK CLASS

package bme.mit.ftsrg.mockFabric;

import bme.mit.ftsrg.model.participants.peers.Peer;
import java.util.HashMap;
import java.util.Map;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public class ChaincodeStub {
    private final Peer peer;
    // TODO this map should be the ledger copy instead
    private final Map<String, byte[]> state = new HashMap<>();

    public ChaincodeStub(Peer peer) {
        this.peer = peer;
    }

    public byte[] getState(String key) {
        return state.get(key);
    }

    public void putState(String key, byte[] value) {
        // TODO start the whole 3 phase process(?)
        // state.put(key, value);
        peer.transaction
    }

    public void delState(String key) throws NotImplementedException {
        throw new NotImplementedException("Not implemented yet");
        //state.remove(key);
    }

    public void reset() {
        state.clear();
    }
}
