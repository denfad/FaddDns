package config;

import common.DNSRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Configurator {

    public List<DNSRecord> parseConfiguration(String pathToConfig) throws IOException {
        List<DNSRecord> records = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(pathToConfig));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // даём возможность комментирвоать записи
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            String[] parts = line.split("\\s+");
            if (parts.length >= 2) {
                String domain = parts[0].toLowerCase();
                String value = parts[1];

                DNSRecord record = new DNSRecord(domain, value);
                records.add(record);
            }
        }
        reader.close();

        return records;
    }
}
