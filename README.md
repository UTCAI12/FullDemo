# FullDemo

PoC helpful to understand how events are triggered by the model and how can these events been handled by other components. 

## Environment 

Java version: Java 17.0.8.1 Zulu [Link here](https://www.azul.com/downloads/?version=java-17-lts&os=windows&package=jdk-fx#zulu)

IDE : IntelliJ Community edition 2022.2.2 [Link here](https://www.jetbrains.com/edu-products/download/other-IIE.html)

## How to start
- Start the server : main.java.com.ilianazz.server.ServerCommunicationController
- Start one or more clients : main.java.com.ilianazz.client.App

## Features 

When starting the client, notification is sent to the server, server notify all clients
Model and HMI updated in consequence.

Ween killing a client, disconnect message is send to the server,
Server updates its local model and then forward the message to all clients.

Able to click on "send track" to send a "new track" message to the server,
Message forwarded to clients **without** updating any model.
