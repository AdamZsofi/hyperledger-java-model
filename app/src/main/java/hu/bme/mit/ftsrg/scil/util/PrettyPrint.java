package hu.bme.mit.ftsrg.scil.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public final class PrettyPrint {
  private PrettyPrint() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static <K, V> String prettifyMap(Map<K, V> map) {
    return map.entrySet().stream()
        .map((Entry<K, V> e) -> e.getKey() + "=>" + e.getValue())
        .collect(Collectors.joining(", "));
  }
}
