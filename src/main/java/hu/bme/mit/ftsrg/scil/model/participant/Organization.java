/* SPDX-License-Identifier: Apache-2.0 */
package hu.bme.mit.ftsrg.scil.model.participant;

import hu.bme.mit.ftsrg.scil.model.WithId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Organization extends WithId {
  private final Collection<Peer> peers = new ArrayList<>();

  public Organization(String id) {
    super(id);
  }

  @Override
  public String toString() {
    return super.toString() + String.format("[peers=%s]", Arrays.toString(peers.toArray()));
  }

  public Collection<Peer> getPeers() {
    return peers;
  }

  public void registerPeer(Peer peer) {
    peers.add(peer);
  }
}
