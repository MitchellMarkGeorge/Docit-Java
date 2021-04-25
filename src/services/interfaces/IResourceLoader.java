package services.interfaces;

import models.Config;
import models.Version;

public interface IResourceLoader {
    
    public Config loadConfig();
    public Version versionFromString(String string);
    public void saveConfig(Config config);
    public void saveVersion(Version version);


}
