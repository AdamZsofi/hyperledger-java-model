// WARNING: MOCK CLASS

package bme.mit.ftsrg.mockFabric;

import bme.mit.ftsrg.model.channel.Channel;
import bme.mit.ftsrg.model.channel.Envelope;
import bme.mit.ftsrg.model.data.Block;
import java.util.HashMap;
import java.util.Map;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public class ChaincodeStub {
    private final Channel channel;

    // TODO this map should be the ledger copy instead(?)
    private final Map<String, byte[]> state = new HashMap<>();

    public ChaincodeStub(Channel channel) {
        this.channel = channel;
    }

    public byte[] getState(String key) {
        return state.get(key);
    }

    public void putState(String key, byte[] value) {
        // TODO start the whole 3 phase process
        Envelope env = endorse(key, value);
        // envelope should be signed here - ignore, not modeled

        // if (endorsed) {
        // TODO I know that peers later check the endorsement,
        // but who checks it beforehand? GS here, orderer, no one..?
        sendToOrderer(key, value);
    }

    private void sendToOrderer(String key, byte[] value) throws NotImplementedException {
        // TODO call channel and channel sends (async) to orderer
        throw new NotImplementedException("Not implemented");
    }

    public void validateAndCommitBlock(Block block) {
        // TODO handle block from ordering service
        // validate
        // state.put(key, value); for each transaction
    }

    private Envelope endorse(String key, byte[] value) {
        return channel.endorse(key, value);
    }

    public void delState(String key) throws NotImplementedException {
        throw new NotImplementedException("Not implemented yet");
        //state.remove(key);
    }

    public void reset() {
        state.clear();
    }
}
