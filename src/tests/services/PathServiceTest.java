package tests.services;

import org.junit.Test;

import di.Container;

import java.nio.file.Paths;

import org.junit.Assert;
import services.interfaces.IPathService;
import tests.Testable;

public class PathServiceTest extends Testable {

    //PARAMETERS WILL NEVER BE NULL OR BLANK OR HAVE ILLEGAL CHARACTERS BECAUSE OF UI (SLASHES AND STUFF CAN BE BAD FOR FILESYSTEM)

    @Test
    public void testBasenameNoDot() {
        IPathService pathService = Container.resolveDependency(IPathService.class);

        String testFileName = "test";

        String result = pathService.basename(testFileName);

        Assert.assertEquals(result, testFileName);


    }

    @Test
    public void testBasenameOneDot() {
        IPathService pathService = Container.resolveDependency(IPathService.class);

        String testFileName = "test.docx";

        String result = pathService.basename(testFileName);

        String expected = "test";
        Assert.assertEquals(result, expected);


    }

    @Test
    public void testBasenameMultipleDots() {
        IPathService pathService = Container.resolveDependency(IPathService.class);

        String testFileName = "test.example.docx";

        String result = pathService.basename(testFileName);

        String expected = "test";
        Assert.assertEquals(result, expected);


    }

    @Test
    public void testGetProjectPath(){

        IPathService pathService = Container.resolveDependency(IPathService.class);
        String root = tempFolder.getRoot().getAbsolutePath();
        String testProjectName = "test";

        String result = pathService.getProjectPath(testProjectName);

        String expected = Paths.get(root, ".docit", testProjectName).toString();

        Assert.assertEquals(expected, result);




    }

    @Test
    public void testGetVersionsPath() {
        IPathService pathService = Container.resolveDependency(IPathService.class);
        String root = tempFolder.getRoot().getAbsolutePath();
        String testProjectName = "test";

        String result = pathService.getVersionsPath(testProjectName);

        String expected = Paths.get(root, ".docit", testProjectName, "versions").toString();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetConfigPath() {
        IPathService pathService = Container.resolveDependency(IPathService.class);
        String root = tempFolder.getRoot().getAbsolutePath();
        String testProjectName = "test";

        String result = pathService.getConfigPath(testProjectName);

        String expected = Paths.get(root, ".docit", testProjectName, "config").toString();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void getHomeDir() {
        IPathService pathService = Container.resolveDependency(IPathService.class);
        String root = tempFolder.getRoot().getAbsolutePath();
        

        String result = pathService.getHomeDir();

        // String expected = Paths.get(root, ".docit", testProjectName).toString();

        Assert.assertEquals(root, result);
    }
    
    @Test
    public void testGetDocitPath() {
        IPathService pathService = Container.resolveDependency(IPathService.class);
        String root = tempFolder.getRoot().getAbsolutePath();
        // String testProjectName = "test";

        String result = pathService.getDocitPath();

        String expected = Paths.get(root, ".docit").toString();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetVersionFilesPath() {

        IPathService pathService = Container.resolveDependency(IPathService.class);
        String root = tempFolder.getRoot().getAbsolutePath();
        String testProjectName = "test";

        String result = pathService.getVersionFilesPath(testProjectName);

        String expected = Paths.get(root, ".docit", testProjectName, "version_files").toString();

        Assert.assertEquals(expected, result);
    }





}
