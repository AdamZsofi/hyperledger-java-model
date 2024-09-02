/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.chaincode;

// Based on:
// https://github.com/hyperledger/fabric-chaincode-java/blob/main/examples/fabric-contract-example-gradle/src/main/java/org/example/MyAssetContract.java
// TODO for now, this contract is hardcoded everywhere

import hu.bme.mit.ftsrg.scil.mockfabric.contract.Context;
import hu.bme.mit.ftsrg.scil.mockfabric.contract.ContractInterface;

public class TrainCrossroadContract implements ContractInterface {
  // @Transaction()
  public void updateState(Context ctx, String value) {
    ctx.getStub().putStringState("canGo", value); // value should be true or false
  }
}
