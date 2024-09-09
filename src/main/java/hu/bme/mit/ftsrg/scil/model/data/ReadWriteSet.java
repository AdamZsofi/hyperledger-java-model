/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model.data;

import java.util.HashMap;
import java.util.Map;

public class ReadWriteSet {
  private final Map<String, Integer> readSet = new HashMap<>();
  private final Map<String, byte[]> writeSet = new HashMap<>();

  public Map<String, Integer> getReadSet() {
    return readSet;
  }

  public Map<String, byte[]> getWriteSet() {
    return writeSet;
  }

  public void addWrite(String key, byte[] value) {
    /*
     * TODO
     * I think only last write of the same key should be kept in the RWSet, but it does not matter in the simple train crossroad example
     * -- AdamZsofi
     */
    writeSet.put(key, value);
  }

  public void addRead(String key, int version) {
    readSet.put(key, version);
  }

  /**
   * Add a delete entry to the read-write set.
   *
   * <p>XXX this may not be the behaviour Fabric follows; this implementation simply sets the value
   * of the key to an empty byte array
   *
   * @param key the key to delete
   */
  public void addDelete(String key) {
    writeSet.put(key, new byte[] {});
  }
}
