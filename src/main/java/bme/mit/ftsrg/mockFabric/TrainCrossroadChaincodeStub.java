// Based on: https://github.com/hyperledger/fabric-chaincode-java/blob/main/fabric-chaincode-shim/src/test/java/org/hyperledger/fabric/contract/ChaincodeStubNaiveImpl.java

package bme.mit.ftsrg.mockFabric;

import bme.mit.ftsrg.model.data.Ledger;

public class TrainCrossroadChaincodeStub implements ChaincodeStub {
    private final Ledger ledgerCopy;
    // we will only have a single key in the ledger/world state (canGo)
    public TrainCrossroadChaincodeStub(Ledger peerLedgerCopy) {
        this.ledgerCopy = peerLedgerCopy;
        ledgerCopy.addEntry("canGo", "false");
    }

    public byte[] getState(String key) {
        return ledgerCopy.getState(key).getValue().getBytes();
    }

    public void putStringState(String key, String value) {
        ledgerCopy.addEntry(key, value);
    }

    public void putState(String key, byte[] value) {
        ledgerCopy.addEntry(key, new String(value));
    }

    // we never want to remove canGo in this case
    public void delState(String key) {}
}
