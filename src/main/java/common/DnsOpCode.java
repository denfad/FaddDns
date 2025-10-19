package common;

public enum DnsOpCode {
    QUERY(0),
    IQUERY(1),
    STATUS(2);

    private final int value;

    DnsOpCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DnsOpCode fromValue(int value) {
        for (DnsOpCode opCode : values()) {
            if (opCode.value == value) {
                return opCode;
            }
        }
        throw new IllegalArgumentException("Invalid DNS opcode: " + value);
    }
}
