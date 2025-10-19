package service.storage;

import common.DnsMessageUtils;
import common.DnsRecordType;
import config.RecordConfig;
import config.RecordsConfig;

import java.util.HashMap;
import java.util.Map;

public class Storage {
    private final Map<DnsRecordType, Map<String, StorageRecord>> records;

    private Storage() {
        records = new HashMap<>();

        for(DnsRecordType type: DnsRecordType.values()) {
            records.put(type, new HashMap<>());
        }
    }

    public StorageRecord getRecord(DnsRecordType type, String domain) {
        return records.get(type).get(domain);
    }

    public void addRecord(StorageRecord storageRecord) {
        records.get(storageRecord.getType()).put(storageRecord.getDomain(), storageRecord);
    }

    public static Storage initFromConfiguration(RecordsConfig config) {
        var storage = new Storage();
        for(RecordConfig recordConfig: config.getValues()) {
            storage.addRecord(StorageRecord.initFromConfig(recordConfig));
        }

        return storage;
    }
}
