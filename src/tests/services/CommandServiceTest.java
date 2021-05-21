/**
 * This class is responsible for tesing the CommandService class and all its methods.
 * It extends the Testable abstract class to ihertit is default behaviour 
 * 
 * @author Mitchell Mark-George
 */

package tests.services;
// Nesseccary imports
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

import di.Container;
import javafx.collections.ObservableList;
import models.Config;
import models.Project;
import models.Version;
import services.interfaces.ICommandService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;
import tests.Testable;

public class CommandServiceTest extends Testable {

    /**
     * This method tests geting a list of project when there are no files
     */
    @Test
    public void testGetProjectsTestEmpty() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        // error flag
        boolean hasError = false;
        // resulting list of projects
        ObservableList<String> result = null;
        try {
            // try and load projects and save to results
            result = commandService.getProjects();
        } catch (Exception e) {
            e.printStackTrace();
            // error occurred
            hasError = true;
        }

        // an error should not occurr
        Assert.assertFalse(hasError);
        // the resilt should not be null (empty list)
        Assert.assertNotNull(result);
        // the resulting list should be empty (no project with no files)
        Assert.assertTrue(result.isEmpty());

    }

    /**
     * This method tests geting a list of project when there is an empty docit folder
     */
    @Test
    public void testGetProjectsTestEmptyDocitFolder() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        // error flag
        boolean hasError = false;
        // resulting list of projects
        ObservableList<String> result = null;
        try {
            // create empty docit folder
            tempFolder.newFolder(".docit");
            // try and load projects and save to results
            result = commandService.getProjects();
        } catch (Exception e) {
            // error occured
            e.printStackTrace();
            hasError = true;
        }

        // an error should not occurr
        Assert.assertFalse(hasError);
        // the resilt should not be null (empty list)
        Assert.assertNotNull(result);
        // the resulting list should be empty (no project with no files)
        Assert.assertTrue(result.isEmpty());

    }

    /**
     * This method thests getting projects from a present docit folder that is not empty
     */
    @Test
    public void testGetProjects() {
        // Injecting the needed services from the dependency injection container
        IPathService pathService = Container.resolveDependency(IPathService.class);
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        //error flag
        boolean hasError = false;
        // resulting list of projects
        ObservableList<String> result = null;
        // name of test projects
        String[] testProjects = { "Chem Lab", "English Paper", "Student Application" };

        try {
            // creates some test projects (folders) 
            for (int i = 0; i < testProjects.length; i++) {
                String testProjectName = testProjects[i];
                Path testProjectPath = Paths.get(pathService.getDocitPath(), testProjectName);
                Files.createDirectories(testProjectPath);
            }
            // try and load projects and save to results
            result = commandService.getProjects();
           
        } catch (Exception e) {
            // error occurred
            e.printStackTrace();
            hasError = true;
        }

        // error should not occur
        Assert.assertFalse(hasError);
        // result variable list should not be null or empty
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
        // the result list should have a length of 3
        Assert.assertEquals(result.size(), 3);
        // the result list should have the same content as the testProject array
        Assert.assertArrayEquals(result.toArray(), testProjects);

    }

    /**
     * This method tests creating a project when the document does not exist
     */
    @Test
    public void testInitProjectNoDocument() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);
        // error flag
        boolean hasError = false;
        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();
        // non existent document
        String testProjectName = "Test";
        try {
            // try creating the project
            commandService.initProject(testDocumentPath, testProjectName);
        } catch (Exception e) {
            // error occured
            hasError = true;
        }

        // get trhe paths of the file and folderstthat would have been made if the command was successful
        Path docitPath = Paths.get(pathService.getDocitPath()); 
        Path configPath = Paths.get(pathService.getConfigPath(testProjectName));
        Path versionsPath = Paths.get(pathService.getVersionsPath(testProjectName));
        Path versionFilesPath = Paths.get(pathService.getVersionFilesPath(testProjectName));
        Path projectPath = Paths.get(pathService.getProjectPath(testProjectName));

        // an error should occur
        Assert.assertTrue(hasError);
        // As this error is thrown, no project files should be made
        Assert.assertFalse(Files.exists(docitPath));
        Assert.assertFalse(Files.exists(projectPath));
        Assert.assertFalse(Files.exists(configPath));
        Assert.assertFalse(Files.exists(versionsPath));
        Assert.assertFalse(Files.exists(versionFilesPath));

    }

    /**
     * This method test creating a project when the document exists (as it should)
     */
    @Test
    public void testInitProjectExistent() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);
        // error flag
        boolean hasError = false;
        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();

        String testProjectName = "Test";
        try {
            // create a test document
            tempFolder.newFile("test.docx");
            // try and create a project
            commandService.initProject(testDocumentPath, testProjectName);
        } catch (Exception e) {
            // error occured
            hasError = true;
             
        }

        // paths of the files and folders that should be made when the command successfuly runs
        String configPathString = pathService.getConfigPath(testProjectName);
        Path docitPath = Paths.get(pathService.getDocitPath()); 
        Path configPath = Paths.get(configPathString);
        Path versionsPath = Paths.get(pathService.getVersionsPath(testProjectName));
        Path versionFilesPath = Paths.get(pathService.getVersionFilesPath(testProjectName));
        Path projectPath = Paths.get(pathService.getProjectPath(testProjectName));

        File configFile = new File(configPathString);

        Assert.assertFalse(hasError);
        // As this error should not be thrown, project files should be made
        Assert.assertTrue(Files.exists(docitPath));
        Assert.assertTrue(Files.exists(projectPath));
        Assert.assertTrue(Files.exists(configPath));
        Assert.assertTrue(Files.exists(versionsPath));
        Assert.assertTrue(Files.exists(versionFilesPath));
        Assert.assertFalse(configFile.length() == 0); // config file cannot be empty
        
    }

    /**
     * This method test cereating a version with no project files or state
     */
    @Test
    public void testNewVersionNoProjectFiles() { // this test is technically not needed
        //  Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        String testComments = "Test Comment";
        // error flag
        boolean hasError = false;
        // The button responsible for this command is automatically disabled when there
        // is no project
        // there would be no current project in state
        // this will never happen because of the UI, but it should still be tested as it
        // is written as a singular unit

        try {
            // try and create version
            commandService.newVersion(testComments);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }
        // exception must be thrown 
        Assert.assertTrue(hasError);
    }

    /**
     * This method tests creating a new version
     */
    @Test
    public void testNewVersion() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        IStateService stateService = Container.resolveDependency(IStateService.class);
        String testComments = "Test Comment";
        testComments = testComments.replaceAll(" ", "_");
        // error flag
        boolean hasError = false;
        

        // Create a tempoary project
        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();

        String testProjectName = "Test";
        Version newVersion = null;
        ObservableList<Version> versions = null;
        Config projeConfig = null;
        

        
        try {
            // create a test dociment
            tempFolder.newFile("test.docx");
            // create a test docment
            commandService.initProject(testDocumentPath, testProjectName);
            // load that project from the file system
            Project currentProject = resourceLoader.loadProject(testProjectName);
            projeConfig = currentProject.getConfig();

            // Simulating that happens in the contreoller
            // update the state service with the approproriate values
            stateService.set("currentProject", currentProject);
            stateService.set("projectName", testProjectName);
            // create a new version
            newVersion = commandService.newVersion(testComments);

            // adding the version to the versions list list (handled by the controller)
            versions = currentProject.getVersions();
            versions.add(newVersion);

           

        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }

        
        // no error should occur
        Assert.assertFalse(hasError);
        // the information loaded from the project cant be null
        // the returned new versino should not be null
        Assert.assertNotNull(newVersion);
        Assert.assertNotNull(versions);
        Assert.assertNotNull(projeConfig);

        
        // the versions list canbt be empty after adding the new version
        Assert.assertFalse(versions.isEmpty());
        // the prokectr config should be updated
        // both current and latest version pointers should point at the new version
        Assert.assertEquals(projeConfig.get("CURRENT_VERSION"), "1");
        Assert.assertEquals(projeConfig.get("LATEST_VERSION"), "1");

        
        // makes sure the actual version file for that version exists
        Path newVersionFilePath = Paths.get(pathService.getVersionFilesPath(testProjectName), newVersion.getFileName());

        Assert.assertTrue(Files.exists(newVersionFilePath));
    }

 

    /**
     * This method is responsible for testing rolingback with no project files or state
     */
    @Test
    public void testRollbackVesionNoProjectFiles() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);

        
        // the provided version will also always be a version from the current project,
        // and not a randomly created one.

        // error flag
        boolean hasError = false;

        try {   
            // try and rollback
            commandService.rollbackVersion();
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }

        // exception should be thrown
        Assert.assertTrue(hasError);

    }

    /**
     * This method tests rolicng back to the current version
     */
    @Test
    public void testRollbackVersionCurrentVersion() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        IStateService stateService = Container.resolveDependency(IStateService.class);

        String testComments = "Test Comment";
        testComments = testComments.replaceAll(" ", "_");
        String testProjectName = "Test";

        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();

        // error flag
        boolean hasError = false;

        try {
            // creates test docunmetn and test project
            tempFolder.newFile("test.docx");
            commandService.initProject(testDocumentPath, testProjectName);
            // load that project
            Project currentProject = resourceLoader.loadProject(testProjectName);

            // Simulating that happens in the contreoller
            // update state
            stateService.set("currentProject", currentProject);
            stateService.set("projectName", testProjectName);
            // create a new service
            Version newVersion = commandService.newVersion(testComments);
            // set the current version in state
            stateService.set("currentVersion", newVersion); 
            // try and rollback version
            commandService.rollbackVersion();
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }

        // exception should be thrown
        Assert.assertTrue(hasError);
    }

    /**
     * This method tests rolling back to a previous version
     */
    @Test
    public void testRollbackVersionBackwards() { 
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        IStateService stateService = Container.resolveDependency(IStateService.class);

        String testComments = "Test Comment";
        testComments = testComments.replaceAll(" ", "_");
        String testProjectName = "Test";

        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();
        // error flag
        boolean hasError = false;
        Config projectConfig = null;
        Version firstVersion = null;
        Version secondVersion = null;

        try {
            // creates a test document and test project
            tempFolder.newFile("test.docx");
            commandService.initProject(testDocumentPath, testProjectName);
            // loads the project
            Project currentProject = resourceLoader.loadProject(testProjectName);

            // Simulating that happens in the contreoller
            // updates state
            stateService.set("currentProject", currentProject);
            stateService.set("projectName", testProjectName);
            // creates multiple version
            firstVersion = commandService.newVersion(testComments);
            secondVersion = commandService.newVersion(testComments);
            
            // change key to SELECTED VERSION
            stateService.set("currentVersion", firstVersion); // simulating selecting thsat version in the table. The
                                                              // version to rollback to
            // rollback to the selected version
            commandService.rollbackVersion();

            projectConfig = currentProject.getConfig();
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }
        // no error should occurr
        Assert.assertFalse(hasError);
        // the loaded objects and version objects cannot be null
        Assert.assertNotNull(projectConfig);
        Assert.assertNotNull(firstVersion);
        Assert.assertNotNull(secondVersion);
        // the config should reflect the rollback
        // the latest version pointer should still point to the most recent version
        // the currenbt version should point to the rolledback version
        Assert.assertEquals(projectConfig.get("LATEST_VERSION"), "2");
        Assert.assertEquals(projectConfig.get("CURRENT_VERSION"), "1");
    }

    /**
     * This method thests rollingback to a version ahead of the current version (essentially two rollbacks)
     */
    @Test
    public void testRollbackVersionForwards() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        IStateService stateService = Container.resolveDependency(IStateService.class);

        String testComments = "Test Comment";
        testComments = testComments.replaceAll(" ", "_");
        String testProjectName = "Test";

        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();
        // error flag
        boolean hasError = false;
        Config projectConfig = null;
        Version firstVersion = null;
        Version secondVersion = null;

        try {
            // create a test document and a test project
            tempFolder.newFile("test.docx");
            commandService.initProject(testDocumentPath, testProjectName);
            // load the project object
            Project currentProject = resourceLoader.loadProject(testProjectName);
            projectConfig = currentProject.getConfig();

            // Simulating that happens in the contreoller
            // update the state
            stateService.set("currentProject", currentProject);
            stateService.set("projectName", testProjectName);

            firstVersion = commandService.newVersion(testComments);
            secondVersion = commandService.newVersion(testComments);
            // creating multiple versions

            // update the state with the current version (to rollback to)
            stateService.set("currentVersion", firstVersion);
            // rollback to the first version
            commandService.rollbackVersion();

            Assert.assertEquals(projectConfig.get("LATEST_VERSION"), "2"); // makes sure the config was updated
            Assert.assertEquals(projectConfig.get("CURRENT_VERSION"), "1");

            stateService.set("currentVersion", secondVersion);
            commandService.rollbackVersion();
            // rollback tothe second version

            projectConfig = currentProject.getConfig();
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }
        // error should not occur
        Assert.assertFalse(hasError);
        // loaded project and version pbjects can ot be null
        Assert.assertNotNull(projectConfig);
        Assert.assertNotNull(firstVersion);
        Assert.assertNotNull(secondVersion);
        // the latert  version pointer should be pointing to the latest version
        // the current version pointer should be pointing to the roledback version (the second version in this case) 
        Assert.assertEquals(projectConfig.get("LATEST_VERSION"), "2");
        Assert.assertEquals(projectConfig.get("CURRENT_VERSION"), "2");
    }

    /**
     * This method is test peeking a version with no project files or state
     */
    @Test
    public void testPeekVesionNoProjectFiles() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);

        // error flag
        boolean hasError = false;

        try {
            // try to peek the version
            commandService.peekVersion();
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }
        // an exception should be thrown
        Assert.assertTrue(hasError);

    }

    /**
     * This method tests peeking a verion with no versions in the project (UI handles this)
     */
    @Test
    public void testPeekVesionNoVersion() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);

        String testComments = "Test Comment";
        testComments = testComments.replaceAll(" ", "_");
        String testProjectName = "Test";

        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();

        // error flag
        boolean hasError = false;

        try {
            // creates a project
            tempFolder.newFile("test.docx");
            commandService.initProject(testDocumentPath, testProjectName);
            // try to peek a version
            commandService.peekVersion();
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }
        // exception should be thrown
        Assert.assertTrue(hasError);

    }

    /**
     * This method tests peeking the current version (UI handles this)
     */
    @Test
    public void testPeekVersionCurrentVersion() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        IStateService stateService = Container.resolveDependency(IStateService.class);

        String testComments = "Test Comment";
        testComments = testComments.replaceAll(" ", "_");
        String testProjectName = "Test";

        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();

        boolean hasError = false;

        try {
            // create a test project
            tempFolder.newFile("test.docx");
            commandService.initProject(testDocumentPath, testProjectName);
            // load the project
            Project currentProject = resourceLoader.loadProject(testProjectName);

            // Simulating that happens in the contreoller
            // update the state
            stateService.set("currentProject", currentProject);
            stateService.set("projectName", testProjectName);
            // create a version
            Version newVersion = commandService.newVersion(testComments);
            // update the state with the version (version to be peeked)
            stateService.set("currentVersion", newVersion); 
            // try and peek the version
            commandService.peekVersion();
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }
        // exception should be thrown
        Assert.assertTrue(hasError);
    }

    /**
     * This method is test peeking a version (under normal circumstances)
     */
    @Test
    public void testPeekVersion() {
        // Injecting the needed services from the dependency injection container
        ICommandService commandService = Container.resolveDependency(ICommandService.class);
        IPathService pathService = Container.resolveDependency(IPathService.class);
        IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
        IStateService stateService = Container.resolveDependency(IStateService.class);

        String testComments = "Test Comment";
        testComments = testComments.replaceAll(" ", "_");
        String testProjectName = "Test";

        String testDocumentPath = Paths.get(pathService.getHomeDir(), "test.docx").toString();
        Config projectConfig = null;
        // path of the peeked path
        String peekedFilePath = null;
        //error flag
        boolean hasError = false;

        try {
            // create a new project
            tempFolder.newFile("test.docx");
            commandService.initProject(testDocumentPath, testProjectName);

            Project currentProject = resourceLoader.loadProject(testProjectName);
            projectConfig = currentProject.getConfig();
            // Simulating that happens in the contreoller
            // update state
            stateService.set("currentProject", currentProject);
            stateService.set("projectName", testProjectName);

            // create multiple versions
            Version firstVersion = commandService.newVersion(testComments);
            // second version
            commandService.newVersion(testComments);  // (cant peek this version as the current version points to this one)
            
            // set version to be peeked in state
            stateService.set("currentVersion", firstVersion); 
            // peek selected version
            commandService.peekVersion();


            // get the path to peeked file
            String documentPath = projectConfig.get("DOCUMENT_PATH");
            String parentDirname = Paths.get(documentPath).getParent().toString();

            
            Path fileName = Paths.get(documentPath).getFileName();
            String documentFileName = pathService.basename(fileName.toString());

            String versionNumber = firstVersion.getVersionNumber();

            peekedFilePath = Paths.get(parentDirname, documentFileName + " v" + versionNumber + ".docx")
                    .toString();
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }

        // no error should be thrown
        Assert.assertFalse(hasError);
        // loaded config cannot be nill
        Assert.assertNotNull(projectConfig);
        // path to the peeked file cannot be null
        Assert.assertNotNull(peekedFilePath);
        // the peeked file should exist
        Assert.assertTrue(Files.exists(Paths.get(peekedFilePath)));
    }

} 
