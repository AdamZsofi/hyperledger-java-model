package hu.bme.mit.ftsrg.scil.mockfabric.contract.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * No-op mock annotation for smart contract (chaincode) classes.
 *
 * <p>Analogous to the {@link org.hyperledger.fabric.contract.annotation.Contact} annotation.
 *
 * <p>This annotation has been implemented for convenience so that chaincode can be plugged in
 * as-is.
 */
@Retention(SOURCE)
@Target(TYPE)
public @interface Contact {
  String email() default "";

  String name() default "";

  String url() default "";
}
