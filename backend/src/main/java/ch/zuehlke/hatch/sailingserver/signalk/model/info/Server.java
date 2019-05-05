package ch.zuehlke.hatch.sailingserver.signalk.model.info;

public class Server {

    private String id;
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Server{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
