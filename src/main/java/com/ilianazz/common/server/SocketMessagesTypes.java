package main.java.com.ilianazz.common.server;

public enum SocketMessagesTypes {
    USER_CONNECT,
    USER_CONNECTED,
    USER_DISCONNECT,
    USER_UPDATE,

    GET_TRACK,
    DOWNLOAD_TRACK,
    PUBLISH_TRACK,
    UNPUBLISH_TRACK,
    UPDATE_TRACK,

    PUBLISH_COMMENT,
    PUBLISH_RATING,

    SERVER_STOPPED
}
