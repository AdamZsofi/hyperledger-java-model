/* SPDX-License-Identifier: Apache-2.0 */
package org.hyperledger.fabric.shim;

import hu.bme.mit.ftsrg.scil.model.data.Ledger;
import hu.bme.mit.ftsrg.scil.model.data.Ledger.LedgerEntry;
import hu.bme.mit.ftsrg.scil.model.data.ReadWriteSet;

import java.nio.charset.StandardCharsets;

public class ChaincodeStubImpl implements ChaincodeStub {
  private final Ledger ledger;
  public ReadWriteSet readWriteSet = new ReadWriteSet();

  public ChaincodeStubImpl(Ledger ledger) {
    this.ledger = ledger;
  }

  public byte[] getState(String key) {
    LedgerEntry state = ledger.getState(key);
    readWriteSet.addRead(key, state.getVersion());
    return state.getValue();
  }

  @Override
  public String getStringState(String key) {
    return new String(getState(key), StandardCharsets.UTF_8);
  }

  public void putStringState(String key, String value) {
    putState(key, value.getBytes(StandardCharsets.UTF_8));
  }

  public void putState(String key, byte[] value) {
    readWriteSet.addWrite(key, value);
  }

  public void delState(String key) {
    readWriteSet.addDelete(key);
  }

  public ReadWriteSet getReadWriteSet() {
    return readWriteSet;
  }
}
