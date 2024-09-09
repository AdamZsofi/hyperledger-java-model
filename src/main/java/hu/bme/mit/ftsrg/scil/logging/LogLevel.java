package hu.bme.mit.ftsrg.scil.logging;

public enum LogLevel {
  ERROR {
    @Override
    public LogLevel previous() {
      return this;
    }
  },
  WARN,
  INFO,
  DEBUG,
  TRACE {
    @Override
    public LogLevel next() {
      return this;
    }
  };

  public LogLevel next() {
    return values()[ordinal() + 1];
  }

  public LogLevel previous() {
    return values()[ordinal() - 1];
  }
}
