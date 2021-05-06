package services;


import java.io.File;
// import java.nio.file.Path;
import java.nio.file.Paths;

import services.interfaces.IPathService;

public class PathService implements IPathService { // use abstract class
    // public String HOME_DIR = System.getProperty("user.home");
    // public String DOCIT_PATH = Paths.get(HOME_DIR, ".docit").toString();
    private String projectName; // should it be a local variable??? Might remove and just have
    
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
    public void updateProjectName(String projectName) { // setProjectName
        this.projectName = projectName;
    }

    @Override
    public String getVersionFilesPath() {
        return Paths.get(getProjectPath(), "version_files").toString();
    }

    @Override
    public String getProjectPath() {
        return Paths.get(getDocitPath(), this.projectName).toString();
    }

    @Override
    public String getVersionsPath() {
        return Paths.get(this.getProjectPath(), "versions").toString();
    }

    @Override
    public String getConfigPath() {
        return Paths.get(this.getProjectPath(), "config").toString();
    }

}
