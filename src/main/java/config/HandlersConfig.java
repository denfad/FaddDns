package config;

public class HandlersConfig {
    private UdpHandlerConfig udp;
    private TlsHandlerConfig tls;
    private TcpHandlerConfig tcp;

    public UdpHandlerConfig getUdp() {
        return udp;
    }

    public void setUdp(UdpHandlerConfig udp) {
        this.udp = udp;
    }

    public TlsHandlerConfig getTls() {
        return tls;
    }

    public void setTls(TlsHandlerConfig tls) {
        this.tls = tls;
    }

    public TcpHandlerConfig getTcp() {
        return tcp;
    }

    public void setTcp(TcpHandlerConfig tcp) {
        this.tcp = tcp;
    }
}
