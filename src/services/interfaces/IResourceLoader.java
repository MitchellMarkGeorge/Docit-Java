package services.interfaces;

import javafx.collections.ObservableList;
import models.Config;
import models.Project;
import models.Version;

public interface IResourceLoader {
    
    public Config loadConfig(String configPath);
    public ObservableList<Version> loadVersions(String versionsPath);
    // private Version stringToVersion(String stringVersion);
    public void saveConfig(Config config, String configPath);
    public void saveVersion(Version version, String versionPath);

    public Project loadProject(String projectname);


}
