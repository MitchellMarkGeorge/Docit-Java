package services;


//USE ABSTRACT CLASS FOR THIS SERVICE -> PRIVATE METHODS

import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.Properties;

/**
 * The purpose of this class is to house methods that return an object based on the string passed in.
 * The strings are parsed differently to match the needs of the defined models/objects
 * 
 * It acts as a "factory" to create Config and Version objects from strings
 * @author Mitchell Mark-George
 */

import models.Config;
import models.Project;
import models.Version;

import services.interfaces.IFileService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import di.Container;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ResourceLoader implements IResourceLoader { // think of better name

    // private IStateService stateService;

    // public ResourceLoader() {
    // this.stateService = (IStateService)
    // Container.resolveDependency(IStateService.class);
    // System.out.println(stateService);
    // }

    
    IFileService fileService =  Container.resolveDependency(IFileService.class);
    IPathService pathService = Container.resolveDependency(IPathService.class);

    
    private Config loadConfig(String configPath) throws IOException {
        // the config is made with the project
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(configPath)) {
            properties.load(fileInputStream); // should i use FileReader

            Config config = new Config(properties);

            return config;

        } catch (IOException e) {
            throw e;
        }
       

    }

    /**
     * Get Project object for current project
     * @throws Exception
     */
    @Override // never 
    public Project loadProject(String projectName) throws Exception {
        
        String projectPath = pathService.getProjectPath(projectName);
        
        if (!Files.exists(Paths.get(projectPath))) { // never going to happen
            throw new Exception("Project does not exist.");
        }

        String configPath = pathService.getConfigPath(projectName);
        String versionsPath = pathService.getVersionsPath(projectName);
        System.out.println(versionsPath);
        Config config = loadConfig(configPath);
        ObservableList<Version> versions = loadVersions(versionsPath);

        return new Project(config, versions);
    }

    
    private ObservableList<Version> loadVersions(String versionsPath) throws Exception {
        
        ObservableList<Version> versions = FXCollections.observableArrayList();
        // System.out.println(Files.exists(Paths.get(versionsPath)));
        if (Files.exists(Paths.get(versionsPath))) {
            fileService.readFileLines(versionsPath, (line) -> {
                System.out.println(line);
                // if (!line.isBlank()) {
                    // System.out.println(line);
                    Version parsedVersion = versionFromString(line);
                    System.out.println(parsedVersion == null);
                    versions.add(parsedVersion);
                // }
            });
        }

        return versions;
    }

    @Override
    public void saveVersion(Version version, String versionPath) throws Exception {
        if (!Files.exists(Paths.get(versionPath))) { // the file is meant to be created when the project is first made
            throw new Exception("Version path does not exist");
        } // never going to happen
        String versionString = version.toString();
        fileService.appendToFile(versionPath, versionString);

    }

    private Version versionFromString(String stringVersion) {
        
        String[] versionsObject = stringVersion.split(" ");

        String versionNumber = versionsObject[0].trim();
        String fileName = versionsObject[1].trim();
        String date = versionsObject[2].trim();
        String comments = versionsObject[3].trim().replaceAll("_", " ");; // There must be a comment OR THE WHOLE THING BREAKS!!!
        

        return new Version(versionNumber, fileName, date, comments);
    }

    @Override
    public void saveConfig(Config config, String configPath) throws Exception { // the file is meant to be created when the project is first made

        if (!Files.exists(Paths.get(configPath))) {
            throw new Exception("Config path does not exist");
        } // never going to happen

        Properties properties = config.getProperties();

        try (FileOutputStream fileOutputStream = new FileOutputStream(configPath)) {
            properties.store(fileOutputStream, null); // no comments
        } catch (Exception e) {
            throw e;
        }
    }

}
