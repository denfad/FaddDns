import common.DNSRecord;
import config.Configurator;
import server.Server;
import service.Handler;

import java.io.IOException;
import java.util.List;

public class FaddDns {

    private static final int DNS_PORT = 53;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Path to config file not specified");
            return;
        }

        try {
            Configurator configurator = new Configurator();
            List<DNSRecord> records = configurator.parseConfiguration(args[0]);

            Handler handler = new Handler(records);

            Server server = new Server(handler, DNS_PORT);
            server.run();
        } catch (IOException e) {
            System.err.println("Unable to read config file: " + e.getMessage());
        }
    }
}
