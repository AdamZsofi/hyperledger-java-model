package hu.bme.mit.ftsrg.scil.logging;

import java.io.PrintStream;

public interface Logger {
  static Logger create(LoggerType type, String name) {
    switch (type) {
      case CONSOLE:
        return new ConsoleLogger(name);
    }

    throw new UnsupportedOperationException("Could not create a logger with this type");
  }

  static Logger create(LoggerType type) {
    switch (type) {
      case CONSOLE:
        return new ConsoleLogger();
    }

    throw new UnsupportedOperationException("Could not create a logger with this type");
  }

  void setLevel(LogLevel level);

  void increaseLevel();

  void decreaseLevel();

  void setOutputStream(PrintStream stream);

  void setName(String name);

  void log(LogLevel level, Object message);

  void trace(Object message);

  void debug(Object message);

  void info(Object message);

  void warn(Object message);

  void error(Object message);
}
