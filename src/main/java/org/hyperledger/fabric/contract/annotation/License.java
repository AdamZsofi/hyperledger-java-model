package org.hyperledger.fabric.contract.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * No-op mock annotation for smart contract (chaincode) classes.
 *
 * <p>Analogous to the {@link org.hyperledger.fabric.contract.annotation.License} annotation.
 *
 * <p>This annotation has been implemented for convenience so that chaincode can be plugged in
 * as-is.
 */
@Retention(SOURCE)
@Target(TYPE)
public @interface License {
  String name() default "";

  String url() default "";
}
