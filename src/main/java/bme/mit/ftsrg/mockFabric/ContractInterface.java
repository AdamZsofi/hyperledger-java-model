package bme.mit.ftsrg.mockFabric;

public interface ContractInterface {
    default Context createContext(final ChaincodeStub stub) {
        return new Context(stub);
    }
}
