/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.mockfabric.contract;

import hu.bme.mit.ftsrg.scil.mockfabric.shim.ChaincodeStub;

/**
 * All smart contracts must implement this interface.
 *
 * <p>Based on <a
 * href="https://github.com/hyperledger/fabric-chaincode-java/blob/68a1ef2c2cdf8a61140f75351da008f33f9ecb9d/fabric-chaincode-shim/src/main/java/org/hyperledger/fabric/contract/ContractInterface.java">org.hyperledger.fabric.contract.ContractInterface</a>.
 */
public interface ContractInterface {
  default Context createContext(final ChaincodeStub stub) {
    return new Context(stub);
  }
}
