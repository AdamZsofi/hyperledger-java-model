package hu.bme.mit.ftsrg.scil.model.data;

public class InvocationResult {
  private final Object result;
  private final ReadWriteSet rwset;

  public InvocationResult(Object result, ReadWriteSet rwset) {
    this.result = result;
    this.rwset = rwset;
  }

  public Object getResult() {
    return result;
  }

  public ReadWriteSet getRWSet() {
    return rwset;
  }
}
