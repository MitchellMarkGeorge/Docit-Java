package services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * The purpose of this class is to house methods that return an object based on the string passed in.
 * The strings are parsed differently to match the needs of the defined models/objects
 * 
 * It acts as a "factory" to create Config and Version objects from strings
 * @author Mitchell Mark-George
 */

 import models.Config;
 import models.Version;
 import services.FileService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;
import di.Container;

public class ResourceLoader implements IResourceLoader { // think of better name
    
    // private IStateService stateService;


    // public ResourceLoader() {
    //     this.stateService = (IStateService) Container.resolveDependency(IStateService.class);
    //     System.out.println(stateService);
    // }

    public static void main(String[] args) {

        // Container.bindDependency(IStateService.class, StateService.class);
        new ResourceLoader();
    }
    /**
     * This method parses a string appropriately and stores the parsed information in a Config object
     * @param string string to be parsed
     * @return a Config object with the parsed information
     */
    @Override
    public Config loadConfig() {
        
      
        Properties properties = new Properties();
        String pathString = path.toString();// should i use to File?
        
        try (FileInputStream fileInputStream = new FileInputStream(pathString)) {
            properties.load(fileInputStream); // should i use FileReader
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        Config config = new Config(properties);
        
        
        
        // FileService.readFileLines(Path.of("je;"), (line) -> {
        //     line.split("=");
        // });


        return config;

    }

    /**
     * This method parses a string appropriately and stores the parsed information in a Version object
     * @param string string to be parsed
     * @return a Config object with the parsed information
     */
    @Override
    public Version versionFromString(String string) {
        return new Version();
    }

    @Override
    public void saveConfig(Config config, String configPath) {
        Properties properties = config.getProperties();

        try (FileOutputStream fileOutputStream = new FileOutputStream(configPath)) {
            properties.store(fileOutputStream, null); // no comments
        } catch (Exception e) {
            e.printStackTrace();
            //TODO: handle exception
        }
    }

    @Override
    public void saveVersion(Version version) {
        // TODO Auto-generated method stub
        
    }
    
}
