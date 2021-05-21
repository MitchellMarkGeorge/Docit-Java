/**
 * This service is responsible for getting all the necessary file paths related to the current project (like the config path and the versions path).
 * This service implements the IPathService interface and all of its methods.
 * 
 * @author Mitchell Mark-George
 */

package services;



import java.io.File;


import java.nio.file.Paths;

import services.interfaces.IPathService;

public class PathService implements IPathService { 
    /** variable that stores the path to the home directory, based on the execution enviroment */
    private String HOME_DIR;
    

    /**
     * Directory structure of Docit
     * .docit (folder that stores all of the projects and their accociated files [folder is stored based on provided root])
     * |-> {projectname} (all projects have their own folders that store their accosiacted files)
     *       |-> config (this file stores the project config information like the current and latest version pointers and the path of the projects document)
     *       |-> versions (this file stores the list of versions of the project [the projects's "timeline"])
     *       |-> version_files (this folder stores all the actual versions of the document)
     */


    
    
    /**
     * This constructor takes a File object and sets the HOME_DIR variable based on it
     * @param root the root folder. if null, get the homedir from the system
     */
    public PathService(File root) {
        // if the root is null, get the home directory from the user's system
        // otherwise, get the absolute path of the root foler and set it as the home directory (used in a testing enviroment)
        if (root != null) {
            HOME_DIR = root.getAbsolutePath();
        } else {
            HOME_DIR = System.getProperty("user.home");
        }
        
    }
   
     /**
     * This inherited method returns the basename (name of the file without any file extensions) of a file
     * @param fileName name of the file (with file extensions)
     * @return basename of the file
     */
    @Override
    public String basename(String fileName) {

        // gets index of the first dot
        // this is important as you could have a file with the name exanple.service.abc
        int firstDotIndex = fileName.indexOf('.');
        // if there is no dot, just return the filename
        if (firstDotIndex == -1) { // no dot
            return fileName;
        } else {
            // return a substring from the begining of the file name to the first dot 
            return fileName.substring(0, firstDotIndex);
        }
   
    }

    /**
     * This inherited method returns the path to a given "home directory", either from the users system or from a provided root folder
     * 
     * @return path to a "home directorty"
     */
    @Override
    public String getHomeDir() {
        return HOME_DIR;
    }

    /**
     * This inherited method resturns the path to the root folder (.docit folder)
     * @return the path to the root docit folder
     */
    @Override
    public String getDocitPath() {
        return Paths.get(getHomeDir(), ".docit").toString();
    }


    //Because of the UI, the projectName arguments will never be blank or null


    /**
     * This inherited method returns the path to a project's version_files folder file given the project name
     * @param projectName name of the project
     * @return the path to the project's version_files folder
     */
    @Override
    public String getVersionFilesPath(String projectName) {
        return Paths.get(getProjectPath(projectName), "version_files").toString();
    }

    /**
     * This inherited method returns the path to a project folder given the project name
     * @param projectName name of the project
     * @return the path to the given project folder
     */
    @Override
    public String getProjectPath(String projectName) {
        return Paths.get(getDocitPath(), projectName).toString();
    }

    /**
     * This inherited method returns the path to a project's versions file (the file that stores the "timeline") given the project name
     * @param projectName name of the project
     * @return the path to a project's versions file
     */
    @Override
    public String getVersionsPath(String projectName) {
        return Paths.get(this.getProjectPath(projectName), "versions").toString();
    }

    /**
     * This inherited method returns the path to a project's config file given the project name
     * @param projectName name of the project
     * @return the path to the project's config file
     */
    @Override
    public String getConfigPath(String projectName) {
        return Paths.get(this.getProjectPath(projectName), "config").toString();
    }

}
