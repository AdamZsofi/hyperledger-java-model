// WARNING: MOCK CLASS

package bme.mit.ftsrg.mockFabric;

import java.util.HashMap;
import java.util.Map;
import jdk.jshell.spi.ExecutionControl;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public class ChaincodeStub {
    // TODO this should be the ledger copy
    private final Map<String, byte[]> state = new HashMap<>();

    public byte[] getState(String key) {
        return state.get(key);
    }

    public void putState(String key, byte[] value) {
        // TODO start the whole 3 phase process
        state.put(key, value);
    }

    public void delState(String key) throws NotImplementedException {
        throw new NotImplementedException("Not implemented yet");
        //state.remove(key);
    }

    public void reset() {
        state.clear();
    }
}
