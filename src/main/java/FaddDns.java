
import config.Configurator;

import server.DnsServer;


public class FaddDns {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Path to config file not specified");
            return;
        }

        var server = DnsServer.initFromConfiguration(Configurator.loadConfig(args[0]));
        server.start();
    }
}
