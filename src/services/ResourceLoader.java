package services;


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
import services.interfaces.IStateService;
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

    IStateService stateService = (IStateService) Container.resolveDependency(IStateService.class);
    IFileService fileService = (IFileService) Container.resolveDependency(IFileService.class);
    IPathService pathService = (IPathService) Container.resolveDependency(IPathService.class);

    /**
     * This method parses a string appropriately and stores the parsed information
     * in a Config object
     * 
     * @param string string to be parsed
     * @return a Config object with the parsed information
     */
    @Override
    public Config loadConfig(String configPath) {
        // the config is made with the project
        Properties properties = new Properties();

        try (FileInputStream fileInputStream = new FileInputStream(configPath)) {
            properties.load(fileInputStream); // should i use FileReader
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Config config = new Config(properties);

        // FileService.readFileLines(Path.of("je;"), (line) -> {
        // line.split("=");
        // });

        return config;

    }

    /**
     * Get Project object for current project
     */
    @Override
    public Project loadProject(String projectName) {
        // TODO Auto-generated method stub
        pathService.updateProjectName(projectName);
        String configPath = pathService.getConfigPath();
        String versionsPath = pathService.getVersionsPath();
        System.out.println(versionsPath);
        Config config = loadConfig(configPath);
        ObservableList<Version> versions = loadVersions(versionsPath);

        return new Project(config, versions);
    }

    @Override
    public ObservableList<Version> loadVersions(String versionsPath) {
        // TODO Auto-generated method stub
        ObservableList<Version> versions = FXCollections.observableArrayList();
        System.out.println(Files.exists(Paths.get(versionsPath)));
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
    public void saveVersion(Version version, String versionPath) {
        // TODO Auto-generated method stub
        String versionString = version.toString();
        fileService.appendToFile(versionPath, versionString);

    }

    private Version versionFromString(String stringVersion) {
        // versionNumber + " " + fileHash + " " + date + " " + comments;
        String[] versionsObject = stringVersion.split(" ");

        String versionNumber = versionsObject[0].trim();
        String fileName = versionsObject[1].trim();
        String date = versionsObject[2].trim();
        String comments = versionsObject[3].trim(); // There must be a comment OR THE WHOLE THING BREAKS!!!
        

        return new Version(versionNumber, fileName, date, comments);
    }

    @Override
    public void saveConfig(Config config, String configPath) {
        Properties properties = config.getProperties();

        try (FileOutputStream fileOutputStream = new FileOutputStream(configPath)) {
            properties.store(fileOutputStream, null); // no comments
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

}
