package common;

// ENUM для кода ответа
public enum DnsResponseCode {
    NO_ERROR(0),
    NOT_FOUND(3);

    private final int value;

    DnsResponseCode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DnsResponseCode fromValue(int value) {
        for (DnsResponseCode rcode : values()) {
            if (rcode.value == value) {
                return rcode;
            }
        }
        throw new IllegalArgumentException("Invalid DNS response code: " + value);
    }
}
