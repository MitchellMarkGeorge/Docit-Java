package services;



import java.io.File;

// import java.nio.file.Path;
import java.nio.file.Paths;

import services.interfaces.IPathService;

public class PathService implements IPathService { // use abstract class
    public String HOME_DIR;
    // public String DOCIT_PATH = Paths.get(HOME_DIR, ".docit").toString();

    /**
     * Direectory structure of Docit
     * .docit (folder that stoes all of the projects and their accociated files [folder is stored based on provided root])
     * |-> {projectname} (all projects have their own folders that store their accosiacted files)
     *       |-> config (this file stores the project config information like the current and latest version pointers and the path of the projects document)
     *       |-> versions (this file stores the list of versions of the project [the projects's "timeline"])
     *       |-> version_files (this folder stores all the actual versions of the document)
     */


    /**
     * 
     * @param root
     */
    

    public PathService(File root) {
        if (root != null) {
            HOME_DIR = root.getAbsolutePath();
        } else {
            HOME_DIR = System.getProperty("user.home");
        }
        
    }
   
    @Override
    public String basename(String fileName) {

        // cna have file.service.docx

        // int lastDotIndex = filePath.lastIndexOf('.');
        int lastDotIndex = fileName.indexOf('.'); // should i do this
        if (lastDotIndex == -1) { // no dot
            return fileName;
        } else {
            return fileName.substring(0, lastDotIndex);
        }
   
    }

    @Override
    public String getHomeDir() {
        return HOME_DIR;
    }

    @Override
    public String getDocitPath() {
        return Paths.get(getHomeDir(), ".docit").toString();
    }


    //Because of the UI, the projectName arguments will never be blank or null

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
