/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil;

import hu.bme.mit.ftsrg.scil.logging.Logger;
import hu.bme.mit.ftsrg.scil.logging.LoggerType;
import hu.bme.mit.ftsrg.scil.model.Network;
import hu.bme.mit.ftsrg.scil.model.data.InvocationRequest;
import hu.bme.mit.ftsrg.scil.model.participant.Client;
import hu.bme.mit.ftsrg.scil.model.participant.OrderingService;
import hu.bme.mit.ftsrg.scil.model.participant.Peer;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.hyperledger.fabric.contract.ContractInterface;

public class Main {
  private static final Logger logger = Logger.create(LoggerType.CONSOLE, "MAIN");

  public static void main(String[] args) {
    OrderingService.FaultMode faultMode = OrderingService.FaultMode.FAULT_FREE;
    int blockSize = 2;

    /* This is quite fragile... */
    int i = 0;
    for (i = 0; i < args.length - 1; ++i) {
      switch (args[i]) {
        case "-h":
          printHelp(System.err);
          System.exit(0);
        case "-v":
          logger.increaseLevel();
          break;
        case "-f":
          if (i + 1 == args.length - 1) {
            printUsageError();
          }
          try {
            faultMode = OrderingService.FaultMode.valueOf(args[i + 1]);
          } catch (IllegalArgumentException e) {
            printUsageError();
          }
          i += 1;
          break;
        case "-b":
          if (i + 1 == args.length - 1) {
            printUsageError();
          }
          try {
            blockSize = Integer.parseInt(args[i + 1]);
          } catch (NumberFormatException e) {
            printUsageError();
          }
          i += 1;
          break;
        default:
          printUsageError();
      }
    }
    if (i == args.length) {
      printUsageError();
    }
    final String contractClassName = args[i];

    Constructor<ContractInterface> contractConstructor;
    try {
      Class<ContractInterface> contractClass =
          (Class<ContractInterface>) Class.forName(contractClassName);
      contractConstructor = contractClass.getDeclaredConstructor();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Could not find class " + contractClassName, e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("No no-arg constructor found in contract class", e);
    }
    Network.Builder networkBuilder =
        Network.builder()
            .addOrganization("R1")
            .addOrganization("R2")
            .addPeer("P1", "R1")
            .addPeer("P2", "R2")
            .addOrderingService("O1", blockSize, faultMode)
            .addChannel("C1")
            .registerPeersToChannel(List.of("P1", "P2"), "C1");
    try {
      networkBuilder.installContract(contractConstructor.newInstance(), "P1", "C1");
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Exception during dynamic contract instance creation", e);
    }
    networkBuilder.registerOrderingServiceToChannel("O1", "C1").addClient("Client", "P1", "O1");
    Network network = networkBuilder.build();
    logger.info(network);

    Client client = network.getClient("Client");
    client.sendTransactionRequest(new InvocationRequest("updateState", "false")); // init
    client.sendTransactionRequest(new InvocationRequest("updateState", "true")); // no train coming
    client.sendTransactionRequest(
        new InvocationRequest("updateState", "false")); // there is a train coming
    client.sendTransactionRequest(
        new InvocationRequest("updateState", "false")); // there is a train coming
    client.sendTransactionRequest(new InvocationRequest("updateState", "true")); // no train coming
    client.sendTransactionRequest(
        new InvocationRequest("updateState", "false")); // there is a train coming

    network.execute();

    Peer p1 = network.getPeer("P1");
    assert Arrays.equals(p1.getWorldState("canGo"), "false".getBytes(StandardCharsets.UTF_8))
        : "canGo should be false in the end!";

    // assert Objects.equals(p1.getWorldState("canGo"), "true") : "canGo should be true in the
    // end!";
    // assert Objects.equals(p2.getWorldState("canGo"), "false") : "canGo should be false in the
    // end!";
    // assert Objects.equals(p1.getWorldState("canGo"), p2.getWorldState("canGo")) : "peers should
    // have the same world state in the end!";
  }

  private static void printUsageError() {
    printUsage(System.err);
    System.exit(1);
  }

  private static void printUsage(PrintStream stream) {
    stream.printf(
        "usage: scil [-hv] [-f %s] [-b BLOCK_SIZE] TARGET_CLASS%n", getPossibleFaultModes());
  }

  private static void printHelp(PrintStream stream) {
    printUsage(stream);
    stream.println("\t\t-h\t\t\t\tdisplay this help message");
    stream.println("\t\t-v\t\t\t\tbe more verbose");
    stream.println(
        "\t\t-f FAULT_MODE\tselect the given fault mode; default is fault free; options: "
            + getPossibleFaultModes());
    stream.println("\t\t-b BLOCK_SIZE\tset the desired block size (integer, default is 2)");
    stream.println("\t\tTARGET_CLASS\tfully qualified class name of the smart contract under test");
    stream.println();
  }

  private static String getPossibleFaultModes() {
    return Arrays.stream(OrderingService.FaultMode.values())
        .map(Enum::toString)
        .collect(Collectors.joining("|"));
  }
}
