package models;

import javafx.collections.ObservableList;

public class Project {
    private Config config;
    private ObservableList<Version> versions;

    public Project(Config config, ObservableList<Version> versions) {
        this.config = config;
        this.versions = versions;
    }

    public Config getConfig() {
        return config;    
    }

    public ObservableList<Version> getVersions() {
        return versions;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setVersion(ObservableList<Version> versions) {
        this.versions = versions;
    }
}
