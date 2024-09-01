/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.mockfabric.shim;

/**
 * Provides access to ledger state for smart contracts.
 *
 * <p>Based on <a
 * href="https://github.com/hyperledger/fabric-chaincode-java/blob/68a1ef2c2cdf8a61140f75351da008f33f9ecb9d/fabric-chaincode-shim/src/main/java/org/hyperledger/fabric/shim/ChaincodeStub.java">org.hyperledger.fabric.shim.ChaincodeStub</a>.
 *
 * <p>Only the most crucial methods have been included.
 */
public interface ChaincodeStub {
  /**
   * Returns the value of the specified <code>key</code> from the ledger.
   *
   * @param key name of the value
   * @return value the value read from the ledger
   */
  byte[] getState(String key);

  /**
   * Returns the byte array value specified by the key and decoded as a UTF-8 encoded string, from
   * the ledger.
   *
   * <p>This is a convenience version of {@link #getState(String)}
   *
   * @param key name of the value
   * @return value the value read from the ledger
   */
  String getStringState(String key);

  /**
   * Writes the specified value and key into the ledger.
   *
   * @param key name of the value
   * @param value the value to write to the ledger
   */
  void putStringState(String key, String value);

  /**
   * Puts the specified <code>key</code> and <code>value</code> into the transaction's writeset as a
   * data-write proposal.
   *
   * @param key name of the value
   * @param value the value to write to the ledger
   */
  void putState(String key, byte[] value);

  /**
   * Records the specified <code>key</code> to be deleted in the writeset of the transaction
   * proposal.
   *
   * @param key name of the value to be deleted
   */
  void delState(String key);
}
