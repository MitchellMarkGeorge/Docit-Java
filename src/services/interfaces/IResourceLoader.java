package services.interfaces;

import java.io.IOException;

import javafx.collections.ObservableList;
import models.Config;
import models.Project;
import models.Version;

public interface IResourceLoader {
    
    public Config loadConfig(String configPath) throws IOException;
    public ObservableList<Version> loadVersions(String versionsPath) throws Exception;
    // private Version stringToVersion(String stringVersion);
    public void saveConfig(Config config, String configPath) throws Exception;
    public void saveVersion(Version version, String versionPath) throws IOException;

    public Project loadProject(String projectname) throws Exception;


}
