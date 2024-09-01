/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.mockfabric.contract;

import hu.bme.mit.ftsrg.scil.mockfabric.shim.ChaincodeStub;

/**
 * Transaction context.
 *
 * <p>Based on <a
 * href="ttps://github.com/hyperledger/fabric-chaincode-java/blob/68a1ef2c2cdf8a61140f75351da008f33f9ecb9d/fabric-chaincode-shim/src/main/java/org/hyperledger/fabric/contract/Context.java
 * ">org.hyperledger.fabric.contract.Context</a>
 *
 * <p><code>ClientIdentity</code>is not part of the model.
 */
public class Context {
  protected ChaincodeStub stub;

  public Context(final ChaincodeStub stub) {
    this.stub = stub;
  }

  public ChaincodeStub getStub() {
    return this.stub;
  }
}
