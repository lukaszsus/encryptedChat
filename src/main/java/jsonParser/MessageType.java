package jsonParser;

public enum MessageType {
    UNKNOWN("UNKNOWN"),
    REG("REG"),
    LOGIN("LOGIN"),
    TEXT("TEXT"),
    LIST("LIST"),
    FIND("FIND"),
    LOGOUT("LOGOUT"),
    PONG("PONG"),
    PING("PING");

    private final String name;

    private MessageType(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
