package hu.bme.mit.ftsrg.chaincode.traincrossing;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class TrainCrossingTest {

  private TrainCrossing contract;

  @Mock private Context ctx;

  @Mock private ChaincodeStub stub;

  @BeforeEach
  void setup() {
    contract = new TrainCrossing();
  }

  @Test
  void when_update_state_to_invalid_value_then_throw_exception() {
    assertThrows(ChaincodeException.class, () -> contract.updateState(ctx, "invalid value"));
  }

  @Nested
  class when_update_state {
    @BeforeEach
    void setup() {
      given(ctx.getStub()).willReturn(stub);
    }

    @Test
    void to_true_then_put_state_true() {
      contract.updateState(ctx, "true");

      then(stub).should().putStringState("canGo", "true");
    }

    @Test
    void to_false_then_put_state_false() {
      contract.updateState(ctx, "false");

      then(stub).should().putStringState("canGo", "false");
    }
  }
}
