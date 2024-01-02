// WARNING: NON-FUNCTIONAL MOCK CLASS

package bme.mit.ftsrg.mockFabric;

// Based on: https://github.com/hyperledger/fabric-chaincode-java/blob/68a1ef2c2cdf8a61140f75351da008f33f9ecb9d/fabric-chaincode-shim/src/main/java/org/hyperledger/fabric/contract/Context.java

public class Context {
    protected ChaincodeStub stub;

    // protected ClientIdentity clientIdentity;

    public Context(final ChaincodeStub stub) {
        this.stub = stub;
        try {
            //this.clientIdentity = new ClientIdentity(stub);
        } catch (Exception e) {
            throw new RuntimeException("Could not create new client identity", e);
        }
    }

    public ChaincodeStub getStub() {
        return this.stub;
    }

    //public ClientIdentity getClientIdentity() {
    //    return this.clientIdentity;
    //}
}