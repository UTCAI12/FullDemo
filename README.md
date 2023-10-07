# FullDemo

PoC helpful to understand how events are triggered by the model and how can these events been handled by other components. 

## Environment 

Java version: Java 20.0.2 Azul

IDE : IntelliJ Community edition 2022.2.2 [Link here](https://www.jetbrains.com/edu-products/download/other-IIE.html)

## How to start
- Start the server : com.ilianazz.ai12poc.server.ServerCommunicationController
- Start one or more clients : com.ilianazz.ai12poc.client.App

## Features 

When starting the client, notification is sent to the server, server notify all clients
Model and HMI updated in consequence.

Ween killing a client, disconnect message is send to the server,
Server updates it's local model and then forward the message to all clients.

Able to click on "send track" to send a "new track" message to the server,
Message forwarded to clients **without** updating any model.
