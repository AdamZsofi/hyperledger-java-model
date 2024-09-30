package hu.bme.mit.ftsrg.scil.cli;

import hu.bme.mit.ftsrg.scil.logging.Logger;
import hu.bme.mit.ftsrg.scil.logging.LoggerType;
import hu.bme.mit.ftsrg.scil.model.participant.OrderingService;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandLineInterface {
  private static final OrderingService.FaultMode DEFAULT_FAULT_MODE =
      OrderingService.FaultMode.FAULT_FREE;
  private static final int DEFAULT_BLOCK_SIZE = 2;

  private static final Logger logger =
      Logger.create(LoggerType.CONSOLE, CommandLineInterface.class.getSimpleName());

  public static void main(String... args) {
    OrderingService.FaultMode faultMode = DEFAULT_FAULT_MODE;
    int blockSize = DEFAULT_BLOCK_SIZE;
    String contractClassName = null;
    String invocationsString = null;

    for (int i = 0; i < args.length; ++i) {
      switch (args[i]) {
        case "-h":
        case "--help":
          printHelp();
          System.exit(0);
        case "-v":
        case "--verbose":
          logger.increaseLevel();
          break;
        case "-i":
        case "--invocations":
          if (i + 1 == args.length - 1) {
            printUsageError();
          }
          invocationsString = args[i + 1];
          i += 1;
          break;
        case "-f":
        case "--fault-mode":
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
        case "--block-size":
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
          contractClassName = args[i];
      }
    }

    if (contractClassName == null || invocationsString == null) {
      printUsageError();
    }

    NetworkRunner.run(
        contractClassName, blockSize, faultMode, InvocationParser.parse(invocationsString));
  }

  private static void printUsageError() {
    printUsage();
    System.exit(1);
  }

  private static void printUsage() {
    System.err.printf(
        "usage: scil [-hv] [-f %s] [-b BLOCK_SIZE] -i INVOCATIONS TARGET_CLASS%n",
        getPossibleFaultModes());
  }

  private static void printHelp() {
    printUsage();
    Stream.of(
            ArgumentHelp.of("-h, --help", "display this help message"),
            ArgumentHelp.of("-v, --verbose", "be more verbose"),
            ArgumentHelp.of(
                "-f, --fault-mode FAULT_MODE",
                String.format(
                    "select fault mode; options: %s; default is %s",
                    getPossibleFaultModes(), DEFAULT_FAULT_MODE)),
            ArgumentHelp.of(
                "-b, --block-size BLOCK_SIZE",
                String.format("specify block size; default is %d", DEFAULT_BLOCK_SIZE)),
            ArgumentHelp.of("-i, --invocations INVOCATIONS STRING", "invocations"),
            ArgumentHelp.of(
                "TARGET_CLASS", "fully qualified class name of smart contract under test"))
        .forEach((ArgumentHelp x) -> System.err.printf("    %-30s %-42s%n", x.names, x.help));
    System.err.println();
  }

  private static String getPossibleFaultModes() {
    return Arrays.stream(OrderingService.FaultMode.values())
        .map(Enum::toString)
        .collect(Collectors.joining("|"));
  }

  private static final class ArgumentHelp {
    String names;
    String help;

    private ArgumentHelp(String names, String help) {
      this.names = names;
      this.help = help;
    }

    public static ArgumentHelp of(String names, String help) {
      return new ArgumentHelp(names, help);
    }
  }
}
