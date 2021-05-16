package services;



// import java.nio.file.Path;
import java.nio.file.Paths;

import services.interfaces.IPathService;

public class PathService implements IPathService { // use abstract class
    // public String HOME_DIR = System.getProperty("user.home");
    // public String DOCIT_PATH = Paths.get(HOME_DIR, ".docit").toString();
   
    @Override
    public String basename(String filePath) {

        // cna have file.service.docx

        // int lastDotIndex = filePath.lastIndexOf('.');
        int lastDotIndex = filePath.indexOf('.'); // should i do this
        if (lastDotIndex == -1) { // no dot
            return filePath;
        } else {
            return filePath.substring(0, lastDotIndex);
        }
   
    }

    @Override
    public String getHomeDir() {
        return System.getProperty("user.home");
    }

    @Override
    public String getDocitPath() {
        return Paths.get(getHomeDir(), ".docit").toString();
    }
    

    @Override
    public String getVersionFilesPath(String projectName) {
        return Paths.get(getProjectPath(projectName), "version_files").toString();
    }

    @Override
    public String getProjectPath(String projectName) {
        return Paths.get(getDocitPath(), projectName).toString();
    }

    @Override
    public String getVersionsPath(String projectName) {
        return Paths.get(this.getProjectPath(projectName), "versions").toString();
    }

    @Override
    public String getConfigPath(String projectName) {
        return Paths.get(this.getProjectPath(projectName), "config").toString();
    }

}
