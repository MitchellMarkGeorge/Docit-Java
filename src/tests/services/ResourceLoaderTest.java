package tests.services;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import di.Container;
import models.Config;
import services.interfaces.IResourceLoader;
import tests.Testable;

public class ResourceLoaderTest extends Testable {
    


    @Test
    public void testSaveConfigEmpty() { // WILL NEVER HAPPEN
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        
        
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testConfigPath = Paths.get(rootPath, "config").toString(); // must exist

        boolean hasError = false;
        try {

            Config testConfig = new Config(); 
            // testConfig.getProperties().isEmpty()
            resourceLoader.saveConfig(testConfig, testConfigPath);

        } catch (Exception e) {
            hasError = true;
          
        }
        // resourceLoader.

        Assert.assertFalse(hasError);
        Assert.assertTrue(Files.exists(Paths.get(testConfigPath)));


    }

    @Test
    public void testSaveConfigFull() {
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        
        
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testConfigPath = Paths.get(rootPath, "config").toString(); // must exist
        String testDocumentPath = Paths.get(rootPath, "test.docx").toString();

        boolean hasError = false;
        try {

            Config testConfig = new Config(); 
            
            testConfig.set("DOCUEMENT_PATH", testDocumentPath);
            testConfig.set("CURRENT_VERSION", "0");
            testConfig.set("LATEST_VERSION", "0");
            // System.out.println(testConfig.getProperties().toString());
            // testConfig.getProperties().isEmpty()
            resourceLoader.saveConfig(testConfig, testConfigPath);

        } catch (Exception e) {
            hasError = true;
          
        }
        // resourceLoader.

        Assert.assertFalse(hasError);
        Assert.assertTrue(Files.exists(Paths.get(testConfigPath)));


    }

    @Test
    public void testSaveConfigBadPath() {
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        
        
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testConfigPath = Paths.get(rootPath, "test", "config").toString(); // must exist
        String testDocumentPath = Paths.get(rootPath, "test.docx").toString();

        boolean hasError = false;
        try {

            Config testConfig = new Config(); 
            
            testConfig.set("DOCUEMENT_PATH", testDocumentPath);
            testConfig.set("CURRENT_VERSION", "0");
            testConfig.set("LATEST_VERSION", "0");
            // System.out.println(testConfig.getProperties().toString());
            // testConfig.getProperties().isEmpty()
            resourceLoader.saveConfig(testConfig, testConfigPath);

        } catch (Exception e) {
            hasError = true;
          
        }
        // resourceLoader.

        Assert.assertTrue(hasError);
        // Assert.assertTrue(Files.exists(Paths.get(testConfigPath)));


    }


}
