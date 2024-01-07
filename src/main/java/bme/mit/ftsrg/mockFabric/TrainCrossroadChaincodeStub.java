// Based on: https://github.com/hyperledger/fabric-chaincode-java/blob/main/fabric-chaincode-shim/src/test/java/org/hyperledger/fabric/contract/ChaincodeStubNaiveImpl.java

package bme.mit.ftsrg.mockFabric;

import bme.mit.ftsrg.model.data.Ledger;
import bme.mit.ftsrg.model.data.Ledger.LedgerEntry;
import bme.mit.ftsrg.model.data.ReadWriteSet;
import jdk.jshell.spi.ExecutionControl;
import jdk.jshell.spi.ExecutionControl.NotImplementedException;

public class TrainCrossroadChaincodeStub implements ChaincodeStub {
    private final Ledger ledgerCopy;
    public ReadWriteSet readWriteSet;
    // we will only have a single key in the ledger/world state (canGo)
    public TrainCrossroadChaincodeStub(Ledger peerLedgerCopy) {
        this.ledgerCopy = peerLedgerCopy;
//        ledgerCopy.addEntry("canGo", "false");
        readWriteSet = new ReadWriteSet();
    }

    public byte[] getState(String key) {
        LedgerEntry state = ledgerCopy.getState(key);
        readWriteSet.addRead(key, state.getVersion());
        return state.getValue().getBytes();
    }

    public void putStringState(String key, String value) {
        readWriteSet.addWrite(key, value);
    }

    public void putState(String key, byte[] value) {
        readWriteSet.addWrite(key, new String(value));
    }

    // we never want to remove canGo in this case
    public void delState(String key) {
        throw new RuntimeException("Delete state not implemented, rwSet has no delete flag yet.");
    }
}
