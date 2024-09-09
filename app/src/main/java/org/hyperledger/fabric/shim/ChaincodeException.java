package org.hyperledger.fabric.shim;

/**
 * Simplified version of {@link org.hyperledger.fabric.shim.ChaincodeException} that does not
 * support providing a payload.
 */
public class ChaincodeException extends RuntimeException {
  public ChaincodeException() {
    super();
  }

  public ChaincodeException(final String message) {
    super(message);
  }

  public ChaincodeException(final Throwable cause) {
    super(cause);
  }

  public ChaincodeException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
