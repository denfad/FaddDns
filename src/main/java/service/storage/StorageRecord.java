package service.storage;

import common.DnsRecordType;
import config.RecordConfig;

import java.util.List;

public class StorageRecord {
    private String domain;
    private List<String> values;
    private DnsRecordType type;

    public StorageRecord(String domain, DnsRecordType type, List<String> values) {
        this.domain = domain;
        this.values = values;
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

    public DnsRecordType getType() {
        return type;
    }

    public void setType(DnsRecordType type) {
        this.type = type;
    }

    public static StorageRecord initFromConfig(RecordConfig recordConfig) {
        return new StorageRecord(recordConfig.getDomain(), recordConfig.getType(), recordConfig.getValues());
    }
}
