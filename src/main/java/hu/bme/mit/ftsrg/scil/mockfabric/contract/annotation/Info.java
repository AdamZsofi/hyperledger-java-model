package hu.bme.mit.ftsrg.scil.mockfabric.contract.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * No-op mock annotation for smart contract (chaincode) classes.
 *
 * <p>Analogous to the {@link org.hyperledger.fabric.contract.annotation.Info} annotation.
 *
 * <p>This annotation has been implemented for convenience so that chaincode can be plugged in
 * as-is.
 */
@Retention(SOURCE)
@Target(TYPE)
public @interface Info {
  String title() default "";

  String description() default "";

  String version() default "";

  String termsOfService() default "";

  License license() default @License();

  Contact contact() default @Contact();
}
