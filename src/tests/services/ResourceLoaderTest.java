package tests.services;

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

    @Test
    public void testSaveConfigEmpty() { // WILL NEVER HAPPEN
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);

        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testConfigPath = Paths.get(rootPath, "config").toString(); // must exist

        boolean hasError = false;
        try {
            tempFolder.newFile("config");
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
            tempFolder.newFile("config");
            Config testConfig = new Config();

            testConfig.set("DOCUEMENT_PATH", testDocumentPath);
            testConfig.set("CURRENT_VERSION", "0");
            testConfig.set("LATEST_VERSION", "0");
            // System.out.println(testConfig.getProperties().toString());
            // testConfig.getProperties().isEmpty()
            resourceLoader.saveConfig(testConfig, testConfigPath);

        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;

        }
        // resourceLoader.

        Assert.assertFalse(hasError);
        Assert.assertTrue(Files.exists(Paths.get(testConfigPath)));

    }

    @Test
    public void testSaveConfigNoFile() {
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

            resourceLoader.saveConfig(testConfig, testConfigPath);

        } catch (Exception e) {
            hasError = true;

        }
        // resourceLoader.

        Assert.assertTrue(hasError);
        // Assert.assertTrue(Files.exists(Paths.get(testConfigPath)));

    }

    @Test
    public void testSaveVersionNoFile() {
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);

        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testVersionPath = Paths.get(rootPath, "test", "versions").toString(); // must exist

        boolean hasError = false;
        try {

            String testFileName = UUID.randomUUID().toString();
            String testVersionNumber = "1";
            String testDate = "5/18/2021";
            String testComments = "No Comment".replaceAll(" ", "_");

            Version testVersion = new Version(testVersionNumber, testFileName, testDate, testComments);

            resourceLoader.saveVersion(testVersion, testVersionPath);

        } catch (Exception e) {
            hasError = true;

        }
        // resourceLoader.

        Assert.assertTrue(hasError);
        // Assert.assertTrue(Files.exists(Paths.get(testVersionPath)));

    }

    // Can make it into methods (a save version test that takes a path)

    @Test
    public void testSaveVersion() {
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);

        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testVersionPath = Paths.get(rootPath, "versions").toString(); // must exist

        boolean hasError = false;
        try {
            tempFolder.newFile("versions");
            String testFileName = UUID.randomUUID().toString();
            String testVersionNumber = "1";
            String testDate = "5/18/2021";
            String testComments = "No Comment".replaceAll(" ", "_");

            Version testVersion = new Version(testVersionNumber, testFileName, testDate, testComments);

            resourceLoader.saveVersion(testVersion, testVersionPath);

        } catch (Exception e) {
            hasError = true;

        }
        // resourceLoader.

        // System.out.println(Files.exists(Paths.get(testVersionPath)));

        Assert.assertFalse(hasError);
        Assert.assertTrue(Files.exists(Paths.get(testVersionPath)));

    }

    @Test
    public void testLoadProject() {
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testDocumentPath = Paths.get(rootPath, "test.docx").toString(); // must exist
        String testProjectName = "Test";
        boolean hasError = false;
        Project loadedTestProject = null;
        try {
            tempFolder.newFile("test.docx");
            commandService.initProject(testDocumentPath, testProjectName);

            loadedTestProject = resourceLoader.loadProject(testProjectName);

        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;

        }
        // resourceLoader.

        Assert.assertFalse(hasError);
        Assert.assertNotNull(loadedTestProject);
        Assert.assertNotNull(loadedTestProject.getConfig());
        Assert.assertNotNull(loadedTestProject.getVersions());
        // Assert.assertTrue(Files.exists(Paths.get(testConfigPath)));
    }

    @Test
    public void testLoadProjectNoProject() {
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        String testProjectName = "Test";
        boolean hasError = false;
        Project loadedTestProject = null;
        try {

            loadedTestProject = resourceLoader.loadProject(testProjectName); // the project name will also never be null

        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;

        }
        

        Assert.assertTrue(hasError);
        Assert.assertNull(loadedTestProject);

        
    }

}
