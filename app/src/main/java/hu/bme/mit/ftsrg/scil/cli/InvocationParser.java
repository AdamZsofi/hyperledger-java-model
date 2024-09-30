package hu.bme.mit.ftsrg.scil.cli;

import hu.bme.mit.ftsrg.scil.model.data.InvocationRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvocationParser {
  private InvocationParser() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }

  public static List<InvocationRequest> parse(String data) {
    List<InvocationRequest> requests = new ArrayList<>();

    Arrays.stream(data.split("!\\s*")).forEach((String line) -> {
      InvocationRequest request = createRequestFromLine(line);
      if (request != null) {
        requests.add(request);
      }
    });

    return requests;
  }

  private static InvocationRequest createRequestFromLine(String line) {
    line = line.replace("#.*", "");
    String[] tokens = line.split("\\s+");
    if (tokens.length == 0) {
      return null;
    }

    String method = tokens[0];
    String[] args = Arrays.copyOfRange(tokens, 1, tokens.length);
    return new InvocationRequest(method, (Object[]) args);
  }
}
