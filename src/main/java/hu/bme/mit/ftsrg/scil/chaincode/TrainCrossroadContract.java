/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.chaincode;

import hu.bme.mit.ftsrg.scil.mockfabric.contract.Context;
import hu.bme.mit.ftsrg.scil.mockfabric.contract.ContractInterface;
import hu.bme.mit.ftsrg.scil.mockfabric.contract.annotation.*;

/**
 * Bare-minimum chaincode for the train crossing example.
 *
 * <p>All it does is update a value at the key <code>canGo</code> with a <code>String</code> value.
 */
@Contract(
    name = "TransactionCrossroadContract",
    info = @Info(
        title = "Train crossroad smart contract",
        description = "A dummy smart contract example",
        version = "0.1.0",
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        ),
        contact = @Contact(
            name = "FTSRG",
            url = "ftsrg.mit.bme.hu"
        )
    )
)
public class TrainCrossroadContract implements ContractInterface {
  @Transaction
  public void updateState(Context ctx, String value) {
    ctx.getStub().putStringState("canGo", value); // value should be true or false
  }
}
