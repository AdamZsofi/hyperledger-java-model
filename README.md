# Modeling and verifying Hyperledger Fabric Networks
## CPSoS Homework Project
Zsófia Ádám

*Supervised by Imre Kocsis and Bertalan Zoltán Péter*

Full documentation is under [doc/doc.md](./doc/doc.md)

## Introduction
[Hyperledger Fabric](https://www.hyperledger.org/projects/fabric) is a framework for building permissioned blockchain networks for various (mainly enterprise) use cases. It's modularity and versatility made it well-known, but there are still many challenging use cases for which it still lacks solutions. Critical cyberphysical systems are one such use case.

A good example for this is ordering: the current consensus protocol supported by Fabric is [Raft](https://hyperledger-fabric.readthedocs.io/en/release-2.5/orderer/ordering_service.html#raft), a leader-follower based Crash Fault Tolerant (CFT) protocol. As Fabric networks are permissioned, Byzantine Fault Tolerance (BFT) or other guarantees that often come up in critical systems (e.g. age of information) were not a priority and Raft does not support them, although future updates adding Byzantine fault tolerance are indicated in the documentation.

Formal verification and model checking is a verification method used often in critical systems. However, the exact component, model or any other part of the system under verification has to be well-defined and in the right format for the model checker to understand and even then, scalability problems often arise.

In this project we tried designing a simple system and some interesting error property on which we can show how model checking could be possible to use for a Fabric solution. After considering several possibilities, we decided on creating a high-level model of a network in Java, where participants (client, endorsing nodes, orderers) are shown as fairly simple in function, but in which we can model a malicious ordering service. Creating the model in Java made it possible to insert the contract in the model fairly easily, by creating simple mocks of the original Fabric classes, which the contract depends on.

Although for this homework, we kept everything simple and some parts of the project are unfortunately not completely generic regarding the contract, we will be able to extend the model in the future to accept more complicated contracts and show more complex functionalities and error properties.

The model of the demo system was a smart railroad crossing, in which the railroad system and the cars at the crossing communicate through a Fabric blockchain. The main goal is for the car not to get hit by a train. The world state should always show if a train is coming, thus not letting cars pass at that time. This is not a realistic engineering solution, but an extendable demonstration concept. We even abstracted timeliness away for this project's sake and just formulated the error property around reordering and losing of transactions by malicious orderers.

For the verification, we used [Java Pathfinder](https://github.com/javapathfinder) (JPF), an open source configurable model checking framework developed by Nasa for Java bytecode. JPF is capable of handling non-determinism both from random values and multithreading, from which we utilized the former. Fortunately, contrary to my expectations, this simple model did not yet cause scalability issues, showing hope for future extended versions to be verified.
