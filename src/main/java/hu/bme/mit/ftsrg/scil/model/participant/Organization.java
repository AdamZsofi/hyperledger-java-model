/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model.participant;

import java.util.ArrayList;
import java.util.Collection;

public class Organization {
  public final String id;
  private final Collection<Peer> peers = new ArrayList<>();

  public Organization(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Org " + id + ", peers: " + peers;
  }

  public Collection<Peer> getPeers() {
    return peers;
  }

  public void registerPeer(Peer peer) {
    peers.add(peer);
  }
}
