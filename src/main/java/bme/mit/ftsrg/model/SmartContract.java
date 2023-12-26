package bme.mit.ftsrg.model;

public interface SmartContract {
    void execute(Identity identity, Object transaction);
}
