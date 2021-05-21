/**
 * This class is responsible for tesing the PathService class and all its methods.
 * It extends the Testable abstract class to ihertit is default behaviour 
 * 
 * @author Mitchell Mark-George
 */ 

package tests.services;
// Nesseccary imports
import org.junit.Test;

import di.Container;

import java.nio.file.Paths;

import org.junit.Assert;
import services.interfaces.IPathService;
import tests.Testable;

public class PathServiceTest extends Testable {

    //PARAMETERS WILL NEVER BE NULL OR BLANK OR HAVE ILLEGAL CHARACTERS BECAUSE OF UI (SLASHES AND STUFF CAN BE BAD FOR FILESYSTEM)

    /**
     * This methods geting the basename of a file name with no extension
     */
    @Test
    public void testBasenameNoDot() {
        // Injecting the needed services from the dependency injection container
        IPathService pathService = Container.resolveDependency(IPathService.class);
        // the test file name
        String testFileName = "test";
        // the resilt
        String result = pathService.basename(testFileName);
        // nothing should change -> both string should be the same
        Assert.assertEquals(result, testFileName);


    }

    /**
     * This method tests getting the basename of a filename with an extension
     */
    @Test
    public void testBasenameOneDot() {
        // Injecting the needed services from the dependency injection container
        IPathService pathService = Container.resolveDependency(IPathService.class);
        // test file name
        String testFileName = "test.docx";
        // get the result fromt the path service
        String result = pathService.basename(testFileName);
        // the expexted result
        String expected = "test";
        // expected and result should be the same
        Assert.assertEquals(result, expected);


    }

    /**
     * This method test getting the basename of a filename with multiple "extensions" (dots)
     */
    @Test
    public void testBasenameMultipleDots() {
        // Injecting the needed services from the dependency injection container
        IPathService pathService = Container.resolveDependency(IPathService.class);
        // test filename 
        String testFileName = "test.example.docx";
        // get the result from the path service
        String result = pathService.basename(testFileName);
        // the expected result
        String expected = "test";
        // the result and expected should be the same
        Assert.assertEquals(result, expected);


    }

    /**
     * This method tests getting a projects project path based on a priject name
     */
    @Test
    public void testGetProjectPath(){
        // Injecting the needed services from the dependency injection container
        IPathService pathService = Container.resolveDependency(IPathService.class);
        String root = tempFolder.getRoot().getAbsolutePath();
        // test project mame
        String testProjectName = "test";
        // get the result from the path service
        String result = pathService.getProjectPath(testProjectName);
        // the expected result
        String expected = Paths.get(root, ".docit", testProjectName).toString();
        // the expected and the result shoudl be the same
        Assert.assertEquals(expected, result);
    }

    /**
     * This method tests gettign a projects versions path based on the project name
     */
    @Test
    public void testGetVersionsPath() {
        // Injecting the needed services from the dependency injection container
        IPathService pathService = Container.resolveDependency(IPathService.class);
        String root = tempFolder.getRoot().getAbsolutePath();
        // test projectr name
        String testProjectName = "test";
        // get the result from the pathService
        String result = pathService.getVersionsPath(testProjectName);
        // the expected result
        String expected = Paths.get(root, ".docit", testProjectName, "versions").toString();
        // the expected should be the same as the result
        Assert.assertEquals(expected, result);
    }

    /**
     * This method tests getting a projects config path based on the project name
     */
    @Test
    public void testGetConfigPath() {
        // Injecting the needed services from the dependency injection container
        IPathService pathService = Container.resolveDependency(IPathService.class);
        String root = tempFolder.getRoot().getAbsolutePath();
        // test project name
        String testProjectName = "test";
        // get the result from the pathService
        String result = pathService.getConfigPath(testProjectName);
        // the expected resul
        String expected = Paths.get(root, ".docit", testProjectName, "config").toString();
        // the expected and the result should be the same
        Assert.assertEquals(expected, result);
    }

    /**
     * This method tests geting the homedir 
     */
    @Test
    public void getHomeDir() {
        // Injecting the needed services from the dependency injection container
        IPathService pathService = Container.resolveDependency(IPathService.class);
        // get the root of the temp directory (expected)
        String root = tempFolder.getRoot().getAbsolutePath();
        // get the result from the pathService
        String result = pathService.getHomeDir();
        // the root and the result should be the same
        Assert.assertEquals(root, result);
    }
    
    /**
     * This method tests getting the docut path
     */
    @Test
    public void testGetDocitPath() {
        // Injecting the needed services from the dependency injection container
        IPathService pathService = Container.resolveDependency(IPathService.class);
        String root = tempFolder.getRoot().getAbsolutePath();
        // get the result from the pathService
        String result = pathService.getDocitPath();
        // the expected result
        String expected = Paths.get(root, ".docit").toString();
        // the expected and the result should be the same
        Assert.assertEquals(expected, result);
    }

    /**
     * This method tests getting the version_files path based on the project name
     */
    @Test
    public void testGetVersionFilesPath() {
        // Injecting the needed services from the dependency injection container
        IPathService pathService = Container.resolveDependency(IPathService.class);
        String root = tempFolder.getRoot().getAbsolutePath();
        // test project name
        String testProjectName = "test";
        // get the result from the pathService
        String result = pathService.getVersionFilesPath(testProjectName);
        // the expected result
        String expected = Paths.get(root, ".docit", testProjectName, "version_files").toString();
        // the expected and the result should be the same
        Assert.assertEquals(expected, result);
    }





}
