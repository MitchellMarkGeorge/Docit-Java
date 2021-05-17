package tests.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// import org.junit.;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
// import org.junit.rules.TemporaryFolder;
import org.junit.rules.TemporaryFolder;

import di.Container;
import di.ContainerModule;
import javafx.collections.ObservableList;
import models.Config;
import models.Project;
import models.Version;
import services.interfaces.ICommandService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;

public class CommandServiceTest {
    // Turn this into class to tbe extended
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void configureContainer() {
        File root = tempFolder.getRoot();

        Container.resetDependencies();
        ContainerModule.bootstrap(root);
    }

    @Test
    public void testGetProjectsTestEmpty() {

        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        boolean hasError = false;
        ObservableList<String> result = null;
        try {
            result = commandService.getProjects();
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }

        Assert.assertFalse(hasError);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.isEmpty());

    }

    @Test
    public void testGetProjects() {

        IPathService pathService = Container.resolveDependency(IPathService.class);
        ICommandService commandService = Container.resolveDependency(ICommandService.class);

        boolean hasError = false;
        ObservableList<String> result = null;

        String[] testProjects = { "Chem Lab", "English Paper", "Student Application" };

        try {
            // creates some test projects
            for (int i = 0; i < testProjects.length; i++) {
                String testProjectName = testProjects[i];
                Path testProjectPath = Paths.get(pathService.getDocitPath(), testProjectName);
                Files.createDirectories(testProjectPath);
            }

            result = commandService.getProjects();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }

        Assert.assertFalse(hasError);
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        Assert.assertEquals(result.size(), 3);

    }

    @Test
    public void testInitProjectNoDocument() {
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);

        boolean hasError = false;
        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();
        // non existent document
        String testProjectName = "Test";
        try {
            commandService.initProject(testDocumentPath, testProjectName);
        } catch (Exception e) {
            // e.printStackTrace();
            hasError = true;
        }

        Path docitPath = Paths.get(pathService.getDocitPath()); // because the temp directory is deleted everytime the
                                                                // tests are run, it should not exist
        Path configPath = Paths.get(pathService.getConfigPath(testProjectName));
        Path versionsPath = Paths.get(pathService.getVersionsPath(testProjectName));
        Path versionFilesPath = Paths.get(pathService.getVersionFilesPath(testProjectName));
        Path projectPath = Paths.get(pathService.getProjectPath(testProjectName));

        Assert.assertTrue(hasError);
        // As this error is thrown, no project files should be made
        Assert.assertFalse(Files.exists(docitPath));
        Assert.assertFalse(Files.exists(projectPath));
        Assert.assertFalse(Files.exists(configPath));
        Assert.assertFalse(Files.exists(versionsPath));
        Assert.assertFalse(Files.exists(versionFilesPath));

    }

    @Test
    public void testInitProjectExistent() {
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);

        boolean hasError = false;
        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();

        String testProjectName = "Test";
        try {
            tempFolder.newFile("test.docx");
            commandService.initProject(testDocumentPath, testProjectName);
        } catch (Exception e) {
            // e.printStackTrace();
            hasError = true;
        }

        String configPathString = pathService.getConfigPath(testProjectName);
        Path docitPath = Paths.get(pathService.getDocitPath()); // because the temp directory is deleted everytime the
                                                                // tests are run, it should not exist
        Path configPath = Paths.get(configPathString);
        Path versionsPath = Paths.get(pathService.getVersionsPath(testProjectName));
        Path versionFilesPath = Paths.get(pathService.getVersionFilesPath(testProjectName));
        Path projectPath = Paths.get(pathService.getProjectPath(testProjectName));

        File configFile = new File(configPathString);

        Assert.assertFalse(hasError);
        // As this error is thrown, no project files should be made
        Assert.assertTrue(Files.exists(docitPath));
        Assert.assertTrue(Files.exists(projectPath));
        Assert.assertTrue(Files.exists(configPath));
        Assert.assertTrue(Files.exists(versionsPath));
        Assert.assertTrue(Files.exists(versionFilesPath)); // should i test if the other files are empty

        Assert.assertFalse(configFile.length() == 0); // config file cannot be empty
        // Files.readAllLines(configPath).

    }

    @Test
    public void testNewVersionNoProjectFiles() { // this test is technically not needed
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        String testComments = "Test Comment";
        // the comment will also never be null - the ui handles this
        boolean hasError = false;
        // The button responsible for this command is automatically disabled when there
        // is no project
        // there would be no current project in state
        // this will never happen because of the UI, but it should still be tested as it
        // is written as a singular unit

        try {
            commandService.newVersion(testComments);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }

        Assert.assertTrue(hasError);
    }

    @Test
    public void testNewVersion() { 
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        IStateService stateService = Container.resolveDependency(IStateService.class);
        String testComments = "Test Comment";
        testComments = testComments.replaceAll(" ", "_");
        
        boolean hasError = false;
        // The button responsible for this command is automatically disabled when there
        // is no project
        // there would be no current project in state
        // this will never happen because of the UI, but it should still be tested as it
        // is written as a singular unit

        // Create a tempoary project
        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();

        String testProjectName = "Test";
        Version newVersion = null;
        ObservableList<Version> versions = null;
        Config projeConfig = null;
        Version versionFromFile = null;
        
        
        // tEST BY:
        // Looking at the config
        //Loading the version file and chacking the version
        //seeing if the version file exists
        try {
            tempFolder.newFile("test.docx");
            commandService.initProject(testDocumentPath, testProjectName);
            Project currentProject = resourceLoader.loadProject(testProjectName);
            projeConfig = currentProject.getConfig();
            

            // Simulating that happens in the contreoller
            stateService.set("currentProject", currentProject);
            stateService.set("projectName", testProjectName);

            newVersion = commandService.newVersion(testComments);

            
            // versions = resourceLoader.loadVersions(versionsPath);

            // adding the version to the list (handled by the controller)
            versions = currentProject.getVersions();
            versions.add(newVersion);


            // reading the version file to make sure it was added in file system
            String versionsPath = pathService.getVersionsPath(testProjectName);

            ObservableList<Version> versionsListFromFile = resourceLoader.loadVersions(versionsPath);

            versionFromFile = versionsListFromFile.get(0);

            
            
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }


        System.out.println(newVersion);
        System.out.println(versionFromFile);
        

        Assert.assertFalse(hasError);
        Assert.assertNotNull(newVersion);
        Assert.assertNotNull(versions);
        Assert.assertNotNull(projeConfig);

        Assert.assertNotNull(versionFromFile);

        Assert.assertFalse(versions.isEmpty());

        Assert.assertEquals(projeConfig.get("CURRENT_VERSION"), "1");
        Assert.assertEquals(projeConfig.get("LATEST_VERSION"), "1");

        Assert.assertEquals(newVersion.getComments(), versionFromFile.getComments());
        Assert.assertEquals(newVersion.getDate(), versionFromFile.getDate());
        Assert.assertEquals(newVersion.getFileName(), versionFromFile.getFileName());
        Assert.assertEquals(newVersion.getVersionNumber(), versionFromFile.getVersionNumber());

        // makes sure the actual version file for that version exists
        Path newVersionFilePath = Paths.get(pathService.getVersionFilesPath(testProjectName), newVersion.getFileName());

        Assert.assertTrue(Files.exists(newVersionFilePath));
    }

    // STILL DO UNCECCESARRY TEST TO SHOW THAT PROGRAM IS FULLY BULETPROOF
    // NO RANDO OR HACKER CAN TRY AND JUST RUN CODE AND IT WORKS


    //Rollback tests

    // going forward
    // going back

} //DOUBLE LAYER SECUIRITY
// CASES ARE HANDLED IN THE UI ANDDD THE SERVICES/BACKEND THEMSELVES
