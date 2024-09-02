package hu.bme.mit.ftsrg.scil.mockfabric.contract.execution;

import com.owlike.genson.Genson;
import java.nio.charset.StandardCharsets;

/**
 * A simple serializer that uses JSON.
 *
 * <p>Analogous to {@link org.hyperledger.fabric.contract.execution.JSONTransactionSerializer} but
 * simpler.
 */
public class JSONSerializer implements Serializer {
  private final Genson genson = new Genson();

  @Override
  public byte[] toBuffer(Object value) {
    return genson.serialize(value).getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public Object fromBuffer(byte[] buffer) {
    return genson.deserialize(new String(buffer, StandardCharsets.UTF_8), Object.class);
  }
}
