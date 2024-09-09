package hu.bme.mit.ftsrg.chaincode.traincrossing;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeException;

@Contract(
    name = "TrainCrossing",
    info =
        @Info(
            title = "Train crossroad smart contract",
            description = "A dummy smart contract example",
            version = "0.1.0",
            license =
                @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
            contact = @Contact(name = "FTSRG", url = "ftsrg.mit.bme.hu")))
public class TrainCrossing implements ContractInterface {
  /**
   * Update the state of the crossing.
   *
   * @param ctx the transaction context
   * @param value whether vehicles may cross at this moment; either <code>"true"</code> or <code>
   *     "false"</code> is accepted
   */
  @Transaction
  public void updateState(Context ctx, String value) {
    if (!(value.equals("true") || value.equals("false"))) {
      throw new ChaincodeException("Value must be 'true' or 'false'");
    }

    ctx.getStub().putStringState("canGo", value);
  }
}
