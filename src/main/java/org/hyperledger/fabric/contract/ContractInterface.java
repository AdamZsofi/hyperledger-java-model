/* SPDX-License-Identifier: Apache-2.0 */
package org.hyperledger.fabric.contract;

import org.hyperledger.fabric.shim.ChaincodeStub;
import hu.bme.mit.ftsrg.scil.model.data.InvocationRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * All smart contracts must implement this interface.
 *
 * <p>Based on {@link org.hyperledger.fabric.contract.ContractInterface}.
 */
public interface ContractInterface {
  default Context createContext(final ChaincodeStub stub) {
    return new Context(stub);
  }

  default Object invoke(Context ctx, InvocationRequest request) {
    try {
      List<Object> args = new ArrayList<>();
      args.add(ctx);
      args.addAll(request.getArgs());

      /*
       * Since we do not have type information in the invocation request, we have to iterate over the defined methods
       * via reflection and find a method with matching name and argument list.
       *
       * Note that we are unable to differentiate between methods having the same name and number of arguments,
       * therefore we select the first method with matching name and argument count.
       */
      Optional<Method> method =
          Arrays.stream(getClass().getMethods())
              .filter(
                  (Method m) ->
                      m.getName().equals(request.getMethod())
                          && m.getParameterTypes().length == args.size())
              .findFirst();

      if (method.isEmpty()) {
        throw new RuntimeException("Unknown method " + request.getMethod());
      } else {
        return method.get().invoke(this, args.toArray(new Object[0]));
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException("Reflection exception", e);
    }
  }
}
