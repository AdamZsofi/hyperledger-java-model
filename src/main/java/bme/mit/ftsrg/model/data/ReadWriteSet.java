package bme.mit.ftsrg.model.data;

import java.util.HashMap;
import java.util.Map;

public class ReadWriteSet {
    private final Map<String, Integer> readSet = new HashMap<>();
    private final Map<String, String> writeSet = new HashMap<>();

    public Map<String, Integer> getReadSet() {
        return readSet;
    }

    public Map<String, String> getWriteSet() {
        return writeSet;
    }

    public void addWrite(String key, String value) {
        writeSet.put(key, value);
    }

    public void addRead(String key, int version) {
        readSet.put(key, version);
    }
}
