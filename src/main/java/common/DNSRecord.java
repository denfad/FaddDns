package common;

public class DNSRecord {
    private String domain;
    private String value;

    public DNSRecord(String domain, String value) {
        this.domain = domain;
        this.value = value;
    }

    public String getDomain() { return domain; }
    public String getValue() { return value; }
}