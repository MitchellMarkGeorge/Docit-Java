/**
 * This interface defines what a ResourceLoader class should do (how it behaves) and the methods it should implement. 
 * 
 * @author Mitchell Mark-George
 */

package services.interfaces;

import java.io.IOException;


import models.Config;
import models.Project;
import models.Version;

public interface IResourceLoader {
    
    /**
     * This method is responsible for saving a config object to the file system
     * @param config config object to be saved
     * @param configPath path that the config object should be save to
     * 
     */
    public void saveConfig(Config config, String configPath) throws Exception;

    /**
     * This method is responsible for saving a version object to the file system
     * @param version version object to be saved
     * @param versionPath path that the version object should be save to
     *
     */
    public void saveVersion(Version version, String versionPath) throws IOException, Exception;

    /**
     * This method is responsible for loading a project based on its project name
     * @param projectname the name of the project to be loaded
     * @return a Project object that contains the assocciated Config object and list of version
     * 
     */
    public Project loadProject(String projectname) throws Exception;


}
