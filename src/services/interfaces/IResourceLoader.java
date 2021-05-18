package services.interfaces;

import java.io.IOException;


import models.Config;
import models.Project;
import models.Version;

public interface IResourceLoader {
    
    // private Config loadConfig(String configPath) throws IOException;
    // private ObservableList<Version> loadVersions(String versionsPath) throws Exception;
    // private Version stringToVersion(String stringVersion);
    public void saveConfig(Config config, String configPath) throws Exception;
    public void saveVersion(Version version, String versionPath) throws IOException, Exception;

    public Project loadProject(String projectname) throws Exception;


}
