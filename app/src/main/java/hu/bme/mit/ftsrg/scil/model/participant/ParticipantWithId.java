package hu.bme.mit.ftsrg.scil.model.participant;

import hu.bme.mit.ftsrg.scil.model.WithId;

public abstract class ParticipantWithId extends WithId implements Participant {
  protected ParticipantWithId(String id) {
    super(id);
  }
}
