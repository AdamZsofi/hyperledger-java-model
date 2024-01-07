package bme.mit.ftsrg.mockFabric;

public interface ChaincodeStub {
    byte[] getState(String key);

    void putStringState(String key, String value);

    void putState(String key, byte[] value);

    void delState(String key);
}
