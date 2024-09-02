/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model;

import hu.bme.mit.ftsrg.scil.model.participant.Organization;
import hu.bme.mit.ftsrg.scil.model.participant.TrainClient;
import hu.bme.mit.ftsrg.scil.model.participant.OrderingService;
import hu.bme.mit.ftsrg.scil.model.participant.Peer;

import java.util.HashMap;
import java.util.Map;

import static hu.bme.mit.ftsrg.scil.model.participant.SimulationStepResult.CONTINUE;

public class Network {
  private final Map<String, Organization> organizations;
  private final Map<String, Peer> peers;
  private final Map<String, Channel> channels;
  private final Map<String, TrainClient> clients;
  private final Map<String, OrderingService> orderers;

  public Network(
      Map<String, Organization> organizations,
      Map<String, Peer> peers,
      Map<String, Channel> channels,
      Map<String, TrainClient> clients,
      Map<String, OrderingService> orderers) {
    this.organizations = organizations;
    this.peers = peers;
    this.channels = channels;
    this.clients = clients;
    this.orderers = orderers;
  }

  @Override
  public String toString() {
    return String.format(
        "Network{\npeers: %s\norganizations: %s\nchannels: %s\nclients: %s\n}",
        peers, organizations, channels, clients);
  }

  public static Builder builder() {
    return new Builder();
  }

  public Peer getPeer(String peerId) {
    return peers.get(peerId);
  }

  public TrainClient getClient(String clientId) {
    return clients.get(clientId);
  }

  public void execute() {
    /*
     * In each iteration (tick), we make all network participants step.
     *
     * If they have done anything, they will return true and we continue.
     *
     * If no participant's step method returns true in a tick, we are
     * 'almost done.'
     * In that case, we run one final iteration.
     *   If the status quo does not change (ie, every participant
     *   returns false), we are really done and the network is stopped.
     *   Otherwise, if a participant makes a step in this last
     *   iteration, we continue as before.
     */

    boolean almostDone = false;
    boolean done = false;
    while (!(almostDone && done)) {
      if (almostDone) {
        done = true;
      }

      /* We are done unless some network participant says otherwise */
      almostDone = true;
      for (Peer peer : peers.values()) {
        if (peer.step() == CONTINUE) {
          almostDone = false;
        }
      }
      for (TrainClient client : clients.values()) {
        if (client.step() == CONTINUE) {
          almostDone = false;
        }
      }
      for (OrderingService orderer : orderers.values()) {
        if (orderer.step() == CONTINUE) {
          almostDone = false;
        }
      }

      if (!almostDone) {
        done = false;
      }
    }

    System.out.println("Network stopped");
  }

  public static class Builder {
    public Map<String, OrderingService> orderers;
    public Map<String, Organization> organizations;
    public Map<String, Peer> peers;
    public Map<String, Channel> channels;
    public Map<String, TrainClient> clients;

    public Builder() {
      reset();
    }

    public void reset() {
      organizations = new HashMap<>();
      peers = new HashMap<>();
      channels = new HashMap<>();
      orderers = new HashMap<>();
      clients = new HashMap<>();
    }

    public Network build() {
      for (Channel c : channels.values()) {
        if (c.getOrderingService() == null) {
          throw new RuntimeException(
              "Each channel must have exactly one ordering service. Register one with the builder.");
        }
      }

      return new Network(organizations, peers, channels, clients, orderers);
    }

    public Builder addOrganization(String orgId) {
      if (organizations.containsKey(orgId)) {
        throw new RuntimeException("Organization with this id already exists: " + orgId);
      }

      organizations.put(orgId, new Organization(orgId));

      return this;
    }

    public Builder addPeer(String peerId, String orgId) {
      if (!organizations.containsKey(orgId)) {
        throw new RuntimeException("Organization with this id does not exist: " + orgId);
      }

      if (peers.containsKey(peerId)) {
        throw new RuntimeException("Peer with this id already exists: " + peerId);
      }

      peers.put(peerId, new Peer(peerId, organizations.get(orgId)));

      return this;
    }

    public Builder addChannel(String channelId) {
      if (channels.containsKey(channelId)) {
        throw new RuntimeException("Channel with this id already exists: " + channelId);
      }

      channels.put(channelId, new Channel(channelId));

      return this;
    }

    public Builder registerPeersToChannel(Iterable<String> peerIds, String channelId) {
      Channel channel = channels.get(channelId);
      if (channel == null) {
        throw new RuntimeException("Channel with this id does not exist: " + channelId);
      }

      for (String peerId : peerIds) {
        if (peers.get(peerId) == null) {
          throw new RuntimeException("Peer can not be registered, it does not exist: " + peerId);
        }

        channel.registerPeer(peers.get(peerId));
      }

      return this;
    }

    public Builder addOrderingService(
        String orderingServiceId, int blockSize, OrderingService.FaultMode faultMode) {
      orderers.put(orderingServiceId, new OrderingService(orderingServiceId, blockSize, faultMode));

      return this;
    }

    public Builder registerOrderingServiceToChannel(
        String orderingServiceId, String channelId) {
      // we will only allow one ordering service per channel
      // but one ordering service can be registered to several channels
      Channel channel = channels.get(channelId);
      if (channel == null) {
        throw new RuntimeException("Channel with this id does not exist: " + channelId);
      }

      if (orderers.get(orderingServiceId) == null) {
        throw new RuntimeException("Orderer with this id does not exist: " + orderingServiceId);
      }

      channel.registerOrderingService(orderers.get(orderingServiceId));
      orderers.get(orderingServiceId).registerToChannel(channel);

      return this;
    }

    public Builder installContract(String peerId, String channelId) {
      if (peers.get(peerId) == null) {
        throw new RuntimeException("Peer can not be registered, it does not exist: " + peerId);
      }

      if (channels.get(channelId) == null) {
        throw new RuntimeException("Channel with this id does not exist: " + channelId);
      }

      peers.get(peerId).installContract(channels.get(channelId));

      return this;
    }

    public Builder addClient(String clientId, String peerId, String ordererId) {
      if (peers.get(peerId) == null) {
        throw new RuntimeException("Peer can not be registered, it does not exist: " + peerId);
      }

      TrainClient client = new TrainClient(clientId, peers.get(peerId), orderers.get(ordererId));
      clients.put(clientId, client);

      return this;
    }
  }
}
