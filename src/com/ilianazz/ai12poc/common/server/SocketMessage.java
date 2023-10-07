package com.ilianazz.ai12poc.common.server;

import java.io.Serializable;

public class SocketMessage implements Serializable {
    public SocketMessagesTypes messageType;
    public Object object;

    public SocketMessage(final SocketMessagesTypes messageType, final Serializable object) {
        this.messageType = messageType;
        this.object = object;
    }

    @Override
    public String toString() {
        return "Message<" + this.messageType + ">'" + object + "'";
    }


}
