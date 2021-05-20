/**
 * This interface defines what a PathService class should do (how it behaves) and the methods it should implement. 
 * 
 * @author Mitchell Mark-George
 */

package services.interfaces;

public interface IPathService {

   

    
    /**
     * This method returns the path to a project folder given the project name
     * @param projectName name of the project
     * @return the path to the given project folder
     */
    public String getProjectPath(String projectName);

    /**
     * This method returns the path to a project's versions file (the file that stores the "timeline") given the project name
     * @param projectName name of the project
     * @return the path to a project's versions file
     */
    public String getVersionsPath(String projectName);

    /**
     * This method returns the path to a project's config file given the project name
     * @param projectName name of the project
     * @return the path to the project's config file
     */
    public String getConfigPath(String projectName);

    /**
     * This method returns the path to a given "home directory", either from the users system or from a provided root folder
     * 
     * @return path to a "home directorty"
     */
    public String getHomeDir();
    
    /**
     * This method resturns the path to the root folder (.docit folder)
     * @return the path to the root docit folder
     */
    public String getDocitPath();

    /**
     * This method returns the basename (name of the file without any file extensions) of a file
     * @param fileName name of the file (with file extensions)
     * @return basename of the file
     */
    public String basename(String fileName);

    /**
     * This method returns the path to a project's version_files folder file given the project name
     * @param projectName name of the project
     * @return the path to the project's version_files folder
     */
    public String getVersionFilesPath(String projectName);
}
