package common;

 public enum DnsRecordClass {
    IN(1),
    CS(2),
    CH(3),
    HS(4);

    private final int value;

    DnsRecordClass(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DnsRecordClass fromValue(int value) {
        for (DnsRecordClass recordClass : values()) {
            if (recordClass.value == value) {
                return recordClass;
            }
        }
        throw new IllegalArgumentException("Invalid DNS record class: " + value);
    }
}
