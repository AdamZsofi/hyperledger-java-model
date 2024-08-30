/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.mockfabric;

public interface ContractInterface {
  default Context createContext(final ChaincodeStub stub) {
    return new Context(stub);
  }
}
