package hu.bme.mit.ftsrg.scil.logging;

import static hu.bme.mit.ftsrg.scil.logging.LogLevel.*;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleLogger implements Logger {
  private String name = "<unnamed logger>";
  private LogLevel level = WARN;
  private PrintStream outputStream = System.err;
  private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

  public ConsoleLogger(String name) {
    this.name = name;
  }

  public ConsoleLogger() {}

  @Override
  public void setLevel(LogLevel level) {
    this.level = level;
  }

  @Override
  public void increaseLevel() {
    level = level.next();
  }

  @Override
  public void decreaseLevel() {
    level = level.previous();
  }

  @Override
  public void setOutputStream(PrintStream stream) {
    this.outputStream = stream;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void log(LogLevel level, Object message) {
    printLogLine(level, message.toString());
  }

  @Override
  public void trace(Object message) {
    log(TRACE, message.toString());
  }

  @Override
  public void debug(Object message) {
    log(DEBUG, message.toString());
  }

  @Override
  public void info(Object message) {
    log(INFO, message.toString());
  }

  @Override
  public void warn(Object message) {
    log(WARN, message.toString());
  }

  @Override
  public void error(Object message) {
    log(ERROR, message.toString());
  }

  private void printLogLine(LogLevel level, String message) {
    outputStream.printf(
        "[%s | %s @ %s] %s%n",
        level.toString(),
        name,
        // ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME),
        dateFormat.format(new Date()),
        message);
  }
}
