package config;



import common.DnsRecordType;

import java.util.List;

public class RecordConfig {
    private String domain;
    private List<String> values;
    private DnsRecordType type;

    public DnsRecordType getType() {
        return type;
    }

    public void setType(DnsRecordType type) {
        this.type = type;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
