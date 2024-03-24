# Modeling and verifying Hyperledger Fabric Networks
## How to Use This Prototype

1. Download the [core project of JPF](https://github.com/javapathfinder/jpf-core)
2. Build jpf-core (from jpf-core project root, make sure you are building with <=java 11): `./gradlew buildJars`
3. Configure the Model by changing the [`Main` class](https://github.com/AdamZsofi/hyperledger-java-model/blob/main/src/main/java/bme/mit/ftsrg/Main.java) (and even the smart contract under the package [`chaincode`](https://github.com/AdamZsofi/hyperledger-java-model/tree/main/src/main/java/bme/mit/ftsrg/chaincode))
4. Execute JPF from this project's root (use <=java 11!): `<path-to-jpf>/bin/jpf hyperledger-java-model.jpf` ([JPF configuration here](https://github.com/AdamZsofi/hyperledger-java-model/blob/main/hyperledger-java-model.jpf), [documentation on JPF configurations](https://github.com/javapathfinder/jpf-core/wiki/Configuring-JPF))

_For further information, see [doc.md](doc/doc.md)_
