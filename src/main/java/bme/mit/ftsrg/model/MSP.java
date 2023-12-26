package bme.mit.ftsrg.model;

public interface MSP {
    Identity enrollUser(String userId);
    Identity registerUser(String userId);
    boolean verifyIdentity(Identity identity);
}
