package bme.mit.ftsrg.model.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ledger {
    private final List<LedgerEntry> entries = new ArrayList<>();
    private final Map<String, LedgerEntry> state = new HashMap<>();

    public void addEntry(String key, String value) {
        int newVersion = state.containsKey(key) ? state.get(key).getVersion() + 1 : 1;
        LedgerEntry entry = new LedgerEntry(key, value, newVersion);
        entries.add(entry);
        updateState(entry);
    }

    private void updateState(LedgerEntry entry) {
        String key = entry.getKey();
        if (!state.containsKey(key) || entry.getVersion() > state.get(key).getVersion()) {
            state.put(key, entry);
        }
    }

    public LedgerEntry getState(String key) {
        return state.get(key);
    }

    public static final class LedgerEntry {
        private final String key;
        private final String value;
        private final int version;

        public LedgerEntry(String key, String value, int version) {
            this.key = key;
            this.value = value;
            this.version = version;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public int getVersion() {
            return version;
        }

        // No setters provided to make the class immutable

        @Override
        public String toString() {
            return "LedgerEntry{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", version=" + version +
                '}';
        }
    }
}
