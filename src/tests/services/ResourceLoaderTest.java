/**
 * This class is responsible for tesing the ResourceLoader class and all its methods.
 * It extends the Testable abstract class to ihertit is default behaviour 
 * 
 * @author Mitchell Mark-George
 */ 

package tests.services;
// nesseccary imports
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import di.Container;
import models.Config;
import models.Project;
import models.Version;
import services.interfaces.ICommandService;
import services.interfaces.IResourceLoader;
import tests.Testable;

public class ResourceLoaderTest extends Testable {

    /**
     * This method tests writing the config to the filesystem
     */
    @Test
    public void testSaveConfig() {
        // Injecting the needed services from the dependency injection container
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);

        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // test config path to save the config to 
        String testConfigPath = Paths.get(rootPath, "config").toString(); // must exist
        // path to the "document"
        String testDocumentPath = Paths.get(rootPath, "test.docx").toString();

        boolean hasError = false;
        try {
            // creates the config file
            tempFolder.newFile("config");
            // creates a new Config object 
            Config testConfig = new Config();
            // populates the config
            testConfig.set("DOCUEMENT_PATH", testDocumentPath);
            testConfig.set("CURRENT_VERSION", "0");
            testConfig.set("LATEST_VERSION", "0");
            // try to save the config
            resourceLoader.saveConfig(testConfig, testConfigPath);

        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occurred

        }
        // no exception chould be thrown
        Assert.assertFalse(hasError);
        // the config file must exist
        Assert.assertTrue(Files.exists(Paths.get(testConfigPath)));

    }

    /**
     * This method tests witing a config to a nonexistent path
     */
    @Test
    public void testSaveConfigNoFile() {
        // Injecting the needed services from the dependency injection container
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);

        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // path to the non existet config path
        String testConfigPath = Paths.get(rootPath, "test", "config").toString(); // must exist
        // path to document
        String testDocumentPath = Paths.get(rootPath, "test.docx").toString();

        boolean hasError = false;
        try {
            // create a new Config object
            Config testConfig = new Config();
            // populate the config
            testConfig.set("DOCUEMENT_PATH", testDocumentPath);
            testConfig.set("CURRENT_VERSION", "0");
            testConfig.set("LATEST_VERSION", "0");
            // try and save the config
            resourceLoader.saveConfig(testConfig, testConfigPath);

        } catch (Exception e) {
            hasError = true;
            // error occured
        }
         
        // an error should be thrown
        Assert.assertTrue(hasError);
        // Assert.assertTrue(Files.exists(Paths.get(testConfigPath)));

    }

    /**
    * This method test saving a version object to a non existent file
    */
    @Test
    public void testSaveVersionNoFile() {
        // Injecting the needed services from the dependency injection container
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);

        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // path to the non existet versions file
        String testVersionPath = Paths.get(rootPath, "test", "versions").toString(); // must exist
        // error flag
        boolean hasError = false;
        try {
            // create all the needed infomation to create a Version
            String testFileName = UUID.randomUUID().toString();
            String testVersionNumber = "1";
            String testDate = "5/18/2021";
            String testComments = "No Comment".replaceAll(" ", "_");
            // create a Version object with the provided information
            Version testVersion = new Version(testVersionNumber, testFileName, testDate, testComments);
            // try to save the version to the path
            resourceLoader.saveVersion(testVersion, testVersionPath);

        } catch (Exception e) {
            hasError = true;
            // error occurred

        }

        // an exception should be thrown
        Assert.assertTrue(hasError);
        

    }

    
    /**
     * This methos tests saving a Version  to the versions path
     */
    @Test
    public void testSaveVersion() {
        // Injecting the needed services from the dependency injection container
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);

        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // path to the versions file
        String testVersionPath = Paths.get(rootPath, "versions").toString(); // must exist

        boolean hasError = false;
        try {
            // create the versions file
            tempFolder.newFile("versions");
            // create the needed information to create a Version object
            String testFileName = UUID.randomUUID().toString();
            String testVersionNumber = "1";
            String testDate = "5/18/2021";
            String testComments = "No Comment".replaceAll(" ", "_");
            // create a Version object based on the provided information
            Version testVersion = new Version(testVersionNumber, testFileName, testDate, testComments);
            // save the version object to the versions file
            resourceLoader.saveVersion(testVersion, testVersionPath);

        } catch (Exception e) {
            hasError = true;
            // error occured

        }
        
        // no exception should be thrown
        Assert.assertFalse(hasError);
        // the versions file must exist
        Assert.assertTrue(Files.exists(Paths.get(testVersionPath)));

    }


    /**
     * This method tests loading a project
     */
    @Test
    public void testLoadProject() {
        // Injecting the needed services from the dependency injection container
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        ICommandService commandService = Container.resolveDependency(ICommandService.class);

        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // path to a test document
        String testDocumentPath = Paths.get(rootPath, "test.docx").toString(); // must exist
        String testProjectName = "Test";
        // error flag
        boolean hasError = false;
        Project loadedTestProject = null;
        try {
            // create the test document and a test project
            tempFolder.newFile("test.docx");
            commandService.initProject(testDocumentPath, testProjectName);
            // try and load the project given the project name
            loadedTestProject = resourceLoader.loadProject(testProjectName);

        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;    
            // error occured

        }
        
        // no error should be thrown
        Assert.assertFalse(hasError);
        // non of the loaded information should be null
        Assert.assertNotNull(loadedTestProject);
        Assert.assertNotNull(loadedTestProject.getConfig());
        Assert.assertNotNull(loadedTestProject.getVersions());
        
    }

    /**
     * This method test loading a non existent project
     */
    @Test
    public void testLoadProjectNoProject() {
        // Injecting the needed services from the dependency injection container
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        // test project name
        String testProjectName = "Test";
        // error flag
        boolean hasError = false;
        Project loadedTestProject = null;
        try {
            // try and load the non existent based on the project name
            loadedTestProject = resourceLoader.loadProject(testProjectName); 
            // this is handled by the UI (can only choose from a list of existing projects)

        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occurred

        }
        
        // an exception should be thrown
        Assert.assertTrue(hasError);
        // the variable should be null
        Assert.assertNull(loadedTestProject);

        
    }

}
