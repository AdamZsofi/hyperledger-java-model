package hu.bme.mit.ftsrg.scil.cli;

import hu.bme.mit.ftsrg.scil.logging.Logger;
import hu.bme.mit.ftsrg.scil.logging.LoggerType;
import hu.bme.mit.ftsrg.scil.model.Network;
import hu.bme.mit.ftsrg.scil.model.data.InvocationRequest;
import hu.bme.mit.ftsrg.scil.model.participant.Client;
import hu.bme.mit.ftsrg.scil.model.participant.OrderingService;
import hu.bme.mit.ftsrg.scil.model.participant.Peer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.hyperledger.fabric.contract.ContractInterface;

class NetworkRunner {
  private static final Logger logger = Logger.create(LoggerType.CONSOLE, NetworkRunner.class.getSimpleName());

  private NetworkRunner() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  static void run(String contractClassName, int blockSize, OrderingService.FaultMode faultMode, List<InvocationRequest> invocationRequests) {
    ContractInterface contract;
    try {
      Class<ContractInterface> contractClass =
          (Class<ContractInterface>) Class.forName(contractClassName);
      Constructor<ContractInterface> contractConstructor = contractClass.getDeclaredConstructor();
      contract = contractConstructor.newInstance();
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Could not find class " + contractClassName, e);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("No no-arg constructor found in contract class", e);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Exception during dynamic contract instantiation", e);
    }

    Network network =
        Network.builder()
            .addOrganization("R1")
            .addOrganization("R2")
            .addPeer("P1", "R1")
            .addPeer("P2", "R2")
            .addOrderingService("O1", blockSize, faultMode)
            .addChannel("C1")
            .registerPeersToChannel(List.of("P1", "P2"), "C1")
            .installContract(contract, "P1", "C1")
            .registerOrderingServiceToChannel("O1", "C1")
            .addClient("Client", "P1", "O1")
            .build();
    logger.info(network);

    Client client = network.getClient("Client");
    invocationRequests.forEach(client::sendTransactionRequest);

    network.execute();

    Peer p1 = network.getPeer("P1");
    assert Arrays.equals(p1.getWorldState("canGo"), "false".getBytes(StandardCharsets.UTF_8))
        : "canGo should be false in the end!";
  }
}
