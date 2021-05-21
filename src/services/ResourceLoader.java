/**
 * This service is responsible for loading projects and version respective configs and versions.
 *  It can also save a projects version and config.
 * This service implements the IResourceLoader interface and all of its methods.
 * 
 * @author Mitchell Mark-George
 */


package services;
// Nesseccary imports
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import di.Container;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Config;
import models.Project;
import models.Version;
import services.interfaces.IFileService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;

public class ResourceLoader implements IResourceLoader { 


    // Injecting the needed services from the dependency injection container
    IFileService fileService =  Container.resolveDependency(IFileService.class);
    IPathService pathService = Container.resolveDependency(IPathService.class);

    /**
     * This method is responisble for loading a Config object from the given path
     * @param configPath path to the config file
     * @return a Config object that was loaded
     */
    private Config loadConfig(String configPath) throws IOException {
        // create a properties object for use later
        Properties properties = new Properties();

        // create a new file stream based on the config path
        try (FileInputStream fileInputStream = new FileInputStream(configPath)) {
            // load the config informwation into the properties using the stream
            properties.load(fileInputStream); 
            // create a  new config object with the properties
            Config config = new Config(properties);
            // return the config
            return config;

        } catch (IOException e) {
            throw e;
        }
       

    }

    /**
     * This inherited method is responsible for loading a project based on its project name
     * @param projectname the name of the project to be loaded
     * @return a Project object that contains the assocciated Config object and list of version
     * 
     * precondition: a project with that project name must exist
     * postcondition: returns a Project object with all the assocciated information
     */
    @Override 
    public Project loadProject(String projectName) throws Exception {
        // get the path of the project using the given projectname
        String projectPath = pathService.getProjectPath(projectName);
        // if the project path does not exist (no project with that name)
        // throw an exception (not going to happen as the UI prevents this)
        if (!Files.exists(Paths.get(projectPath))) { 
            throw new Exception("Project does not exist.");
        }

        // get the config and versions files path from the pathService 
        String configPath = pathService.getConfigPath(projectName);
        String versionsPath = pathService.getVersionsPath(projectName);
        // load the project's config using the config path
        Config config = loadConfig(configPath);
        // load the ject's list of versions using the version path
        ObservableList<Version> versions = loadVersions(versionsPath);
        // return a new Project object
        return new Project(config, versions);
    }

    /**
     * This method is responisble for loading a list of Version objects from the given path
     * @param versionPath path to the versions file
     * @return a list of Version object that was loaded from the path
     */
    private ObservableList<Version> loadVersions(String versionsPath) throws Exception {
        // creaes an empty list
        ObservableList<Version> versions = FXCollections.observableArrayList();
        // if the versions path exists
        // it should because it is made when every project is made
        if (Files.exists(Paths.get(versionsPath))) {
            // read the lines of the versions files
            fileService.readFileLines(versionsPath, (line) -> {
                    // parse each line into a version object
                    Version parsedVersion = versionFromString(line);
                    // add the pased version to the list
                    versions.add(parsedVersion);
                // }
            });
        }

        // return the list of versions (could be empty)
        return versions;
    }

    /**
     * This inherited method is responsible for saving a version object to the file system
     * @param version version object to be saved
     * @param versionPath path that the version object should be save to
     *
     * preconditions: the version object cannot be null (wont be) and the version path must exist (should exist)
     * postcondition: the version was successfully saved to the versions file (added to the "timeline")
     */
    @Override
    public void saveVersion(Version version, String versionPath) throws Exception {
        // if the versions path does not exist for some reason (created with every project)
        // throw an exception
        if (!Files.exists(Paths.get(versionPath))) { 
            throw new Exception("Version path does not exist");
        } 
        // get the version as a string
        String versionString = version.toString();
        // append that string to the end of of the versions file
        fileService.appendToFile(versionPath, versionString);

    }

    /**
     * This method is responsible for parsing a string into a Versions pobject
     * @param stringVersion a valid string to be parsed
     * @return a Versions object parsed from the string
     */
    private Version versionFromString(String stringVersion) {
        // split the sting based on space
        String[] versionsObject = stringVersion.split(" ");
        // get the nesseccary details from each part of the split string
        String versionNumber = versionsObject[0].trim();
        String fileName = versionsObject[1].trim();
        String date = versionsObject[2].trim();
        String comments = versionsObject[3].trim();    
        // retien a new Version object based on the parsed information
        return new Version(versionNumber, fileName, date, comments);
    }

    /**
     * This method is responsible for saving a config object to the file system
     * @param config config object to be saved
     * @param configPath path that the config object should be save to
     * 
     * preconditions: the config object cannot be null (wont be) and the config path must exist (should exist)
     * postcondition: the config was successfully saved to the config file 
     */
    @Override
    public void saveConfig(Config config, String configPath) throws Exception { 
        // if the config path does not exist for some reason (created with every project)
        // throw an exception
        if (!Files.exists(Paths.get(configPath))) {
            throw new Exception("Config path does not exist");
        } 
        // get the properties from the config
        Properties properties = config.getProperties();
        //create a file stream based on the config path
        try (FileOutputStream fileOutputStream = new FileOutputStream(configPath)) {
            // use the stream to write the properties to the config path
            properties.store(fileOutputStream, null); // no comments
        } catch (Exception e) {
            throw e;
        }
    }

}
