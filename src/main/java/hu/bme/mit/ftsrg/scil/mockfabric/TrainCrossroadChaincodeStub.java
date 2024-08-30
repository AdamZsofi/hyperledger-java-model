/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.mockfabric;

import hu.bme.mit.ftsrg.scil.model.data.Ledger;
import hu.bme.mit.ftsrg.scil.model.data.Ledger.LedgerEntry;
import hu.bme.mit.ftsrg.scil.model.data.ReadWriteSet;

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
