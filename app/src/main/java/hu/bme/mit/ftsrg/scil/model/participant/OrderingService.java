/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model.participant;

import static hu.bme.mit.ftsrg.scil.model.participant.SimulationStepResult.CONTINUE;
import static hu.bme.mit.ftsrg.scil.model.participant.SimulationStepResult.NOTHING_TO_DO;

import hu.bme.mit.ftsrg.scil.logging.Logger;
import hu.bme.mit.ftsrg.scil.logging.LoggerType;
import hu.bme.mit.ftsrg.scil.model.Channel;
import hu.bme.mit.ftsrg.scil.model.data.Block;
import hu.bme.mit.ftsrg.scil.model.data.ReadWriteSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This is the model for the ordering service.
 *
 * <p>In reality, this is a cluster of nodes running a protocol (usually Raft) but we will handle
 * ordering as a black box with different failure modes
 */
public class OrderingService extends ParticipantWithId {
  private static final double TX_LOSS_RATIO = .5;
  private final Queue<ReadWriteSet> transactions = new LinkedList<>();
  private final int blockSize;
  private final FaultMode faultMode;
  private final Random random = new Random();
  private Channel channel;
  private final Logger logger;

  public OrderingService(String id, int blockSize, FaultMode faultMode) {
    super(id);
    this.blockSize = blockSize;
    this.faultMode = faultMode;
    logger = Logger.create(LoggerType.CONSOLE, toString());
  }

  @Override
  public SimulationStepResult step() {
    if (transactions.size() < blockSize) {
      return NOTHING_TO_DO;
    }

    while (transactions.size() >= blockSize) {
      logger.info("building a new block");
      orderTransactions();
    }

    return CONTINUE;
  }

  public void receiveTransaction(ReadWriteSet readWriteSet) {
    if (faultMode == FaultMode.ALL_FAULTS || faultMode == FaultMode.CAN_DROP) {
      if (random.nextDouble() < TX_LOSS_RATIO) {
        transactions.add(readWriteSet);
      }
    }

    /* Normal (fault-free) behaviour */
    transactions.add(readWriteSet);
  }

  public void orderTransactions() {
    /* Not enough transactions yet to form a block */
    if (transactions.size() < blockSize) {
      return;
    }

    /* Take the first blockSize transactions from queue and put into new block */
    List<ReadWriteSet> blockTransactions =
        IntStream.rangeClosed(1, blockSize)
            .mapToObj((_i) -> transactions.poll())
            .collect(Collectors.toCollection(ArrayList::new));

    /* Reorder the block if fault mode is such */
    if (faultMode == FaultMode.CAN_REORDER || faultMode == FaultMode.ALL_FAULTS) {
      Collections.shuffle(blockTransactions);
    }

    Block block = new Block(blockTransactions);

    // send new block to peers
    channel.broadcastBlock(block);
  }

  public void registerToChannel(Channel channel) {
    this.channel = channel;
  }

  public enum FaultMode {
    ALL_FAULTS,
    CAN_REORDER,
    CAN_DROP,
    FAULT_FREE
  }
}
