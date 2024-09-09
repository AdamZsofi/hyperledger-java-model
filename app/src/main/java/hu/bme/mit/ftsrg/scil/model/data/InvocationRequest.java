package hu.bme.mit.ftsrg.scil.model.data;

import java.util.List;

/**
 * Simplified invocation request data object.
 *
 * <p>Analogous to the {@link org.hyperledger.fabric.contract.execution.InvocationRequest} interface
 * with the {@link org.hyperledger.fabric.contract.execution.impl.ContractInvocationRequest}
 * implementation.
 */
public class InvocationRequest {
  private final String method;
  private final List<Object> args;

  public InvocationRequest(String method, List<Object> args) {
    this.method = method;
    this.args = args;
  }

  public InvocationRequest(String method, Object... args) {
    this(method, List.of(args));
  }

  public String getMethod() {
    return method;
  }

  public List<Object> getArgs() {
    return args;
  }
}
