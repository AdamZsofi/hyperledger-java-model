package org.hyperledger.fabric.contract.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * No-op mock annotation for transactional chaincode methods.
 *
 * <p>Analogous to the {@link org.hyperledger.fabric.contract.annotation.Transaction} annotation.
 *
 * <p>This annotation has been implemented for convenience so that chaincode can be plugged in
 * as-is.
 */
@Retention(SOURCE)
@Target(METHOD)
public @interface Transaction {
  TYPE intent() default TYPE.SUBMIT;

  String name() default "";

  enum TYPE {
    SUBMIT,
    EVALUATE
  }
}
