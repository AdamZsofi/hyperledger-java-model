/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model.data;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The entire blockchain ledger.
 *
 * <p>Keeps track of both <code>entries</code> and the <code>state</code>. The <code>entries</code>
 * collection contains all key-value-version triples known by the ledger. The <code>state</code>
 * contains the current ledger state; ie, only one value per key.
 *
 * <p>Keys are represented as <code>String</code>s and values are <code>byte</code> arrays.
 *
 * <p>TODO keep invalid transactions (-- AdamZsofi)
 */
public class Ledger {
  private final List<LedgerEntry> entries = new LinkedList<>();
  private final Map<String, LedgerEntry> state = new HashMap<>();

  public Ledger(Ledger other) {
    this.entries.addAll(other.entries);
    this.state.putAll(other.state);
  }

  public Ledger() {}

  /**
   * Adds a new entry to the ledger.
   *
   * <p>The new entry is persisted unconditionally. The version of the <code>LedgerEntry</code> is
   * either <b>1</b> (if the <code>key</code> is new) or the previous version incremented by one.
   *
   * @param key the key where to write the value
   * @param value the value to write
   */
  public void addEntry(String key, byte[] value) {
    int version = state.containsKey(key) ? state.get(key).getVersion() + 1 : 1;
    LedgerEntry entry = new LedgerEntry(key, value, version);
    entries.add(entry);
    updateState(entry);
  }

  /**
   * Internal function that actually updates the state given an entry.
   *
   * <p>A state update only occurs if the key does not exist in the ledger yet or if the version of
   * the entry is greater than the current version in the ledger state.
   *
   * @param entry the entry to get details from
   */
  private void updateState(LedgerEntry entry) {
    String key = entry.getKey();
    if (!state.containsKey(key) || entry.getVersion() > state.get(key).getVersion()) {
      state.put(key, entry);
    }
  }

  /**
   * Get the state for a given key.
   *
   * @param key the key to get the state for
   * @return the entry at the key
   */
  public LedgerEntry getState(String key) {
    return state.get(key);
  }

  /**
   * An entry in the ledger state.
   *
   * <p>This class is designed to be immutable.
   */
  public static final class LedgerEntry {
    private final String key;
    private final byte[] value;
    private final int version;

    public LedgerEntry(String key, byte[] value, int version) {
      this.key = key;
      this.value = value;
      this.version = version;
    }

    /**
     * Get the key for this entry.
     *
     * @return the key
     */
    public String getKey() {
      return key;
    }

    /**
     * Get the value for this entry.
     *
     * @return the value at the <code>key</code>
     * @see #getStringValue
     */
    public byte[] getValue() {
      return value;
    }

    /**
     * Convenience method to get the <code>value</code> as a <code>String</code>.
     *
     * <p>The value is decoded as UTF-8.
     *
     * @return the decoded value at the <code>key</code>
     * @see #getValue
     */
    public String getStringValue() {
      return new String(value, StandardCharsets.UTF_8);
    }

    /**
     * Get the version of this entry.
     *
     * @return the version of the entry at <code>key</code>
     */
    public int getVersion() {
      return version;
    }

    @Override
    public String toString() {
      return String.format(
          "LedgerEntry{key='%s',value='%s',version=%d}", key, Arrays.toString(value), version);
    }
  }
}
