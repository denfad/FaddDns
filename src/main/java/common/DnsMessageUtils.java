package common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DnsMessageUtils {

    public static byte[] toByteArray(String data, DnsRecordType recordType) {
        return switch (recordType) {
            case A -> ipv4ToBytes(data);
            case AAAA -> ipv6ToBytes(data);
            default -> throw new IllegalStateException("Unexpected value: " + recordType);
        };
    }
    public static byte[] ipv4ToBytes(String ipAddress) {
        try {
            return InetAddress.getByName(ipAddress).getAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] ipv6ToBytes(String ipAddress) {
        try {
            return InetAddress.getByName(ipAddress).getAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
