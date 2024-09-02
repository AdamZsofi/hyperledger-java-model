/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model;

import hu.bme.mit.ftsrg.scil.model.data.Block;
import hu.bme.mit.ftsrg.scil.model.participant.OrderingService;
import hu.bme.mit.ftsrg.scil.model.participant.Peer;
import hu.bme.mit.ftsrg.scil.model.participant.TrainClient;
import java.util.HashMap;
import java.util.Map;

public class Channel extends WithId {
  private final Map<String, Peer> peers = new HashMap<>();
  private TrainClient client;
  private OrderingService orderingService;

  public Channel(String id) {
    super(id);
  }

  public void registerOrderingService(OrderingService orderingService) {
    if (this.orderingService != null) {
      throw new RuntimeException("Channel already has a registered ordering service");
    }

    this.orderingService = orderingService;
  }

  public void registerPeer(Peer peer) {
    if (peers.containsKey(peer.getId())) {
      throw new RuntimeException(
          String.format("Peer#%s already registered on channel", peer.getId()));
    }

    peers.put(peer.getId(), peer);
  }

  @Override
  public String toString() {
    return super.toString() + String.format("[peerCount=%d,orderingService=%s]", peers.size(), orderingService);
  }

  public OrderingService getOrderingService() {
    return orderingService;
  }

  public TrainClient getClient() {
    return client;
  }

  public void broadcastBlock(Block block) {
    for (Peer peer : peers.values()) {
      peer.receiveBlock(block);
    }
  }
}
