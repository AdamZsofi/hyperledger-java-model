package hu.bme.mit.ftsrg.scil.model;

public abstract class WithId {
  protected final String id;

  protected WithId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return String.format("%s#%s", getClass().getSimpleName(), id);
  }

  public String getId() {
    return id;
  }
}