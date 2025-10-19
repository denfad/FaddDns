package config;

public class AppConfig {
    private HandlersConfig handlers;
    private RecordsConfig records;
    private UpstreamsConfig upstreams;

    public UpstreamsConfig getUpstreams() {
        return upstreams;
    }

    public void setUpstreams(UpstreamsConfig upstreams) {
        this.upstreams = upstreams;
    }

    public HandlersConfig getHandlers() {
        return handlers;
    }

    public void setHandlers(HandlersConfig handlers) {
        this.handlers = handlers;
    }

    public RecordsConfig getRecords() {
        return records;
    }

    public void setRecords(RecordsConfig records) {
        this.records = records;
    }
}
