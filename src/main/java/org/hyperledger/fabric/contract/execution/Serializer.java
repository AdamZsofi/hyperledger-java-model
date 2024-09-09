package org.hyperledger.fabric.contract.execution;

/**
 * Data serializer interface.
 *
 * <p>Analogous to {@link org.hyperledger.fabric.contract.execution.SerializerInterface}, but
 * simplified: there is no {@link org.hyperledger.fabric.contract.metadata.TypeSchema} and all
 * serializers are automatically assumed to apply to all elements.
 */
public interface Serializer {
  byte[] toBuffer(Object value);

  Object fromBuffer(byte[] buffer);
}
