package common;

public enum DnsMessageType {
    QUERY(0),
    RESPONSE(1);

    private final int value;

    DnsMessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DnsMessageType fromValue(int value) {
        for (DnsMessageType type : values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DNS message type: " + value);
    }
}
