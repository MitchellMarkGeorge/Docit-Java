package models;

public class Project {
    private Config config;
    private Version version;

    public Project(Config config, Version version) {
        this.config = config;
        this.version = version;
    }

    public Config getConfig() {
        return config;
    }

    public Version getVersion() {
        return version;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setVersion(Version version) {
        this.version = version;
    }
}
