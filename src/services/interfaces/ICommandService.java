/**
 * This interface defines what a CommandService class should do (how it behaves) and the methods it should implement. 
 * 
 * @author Mitchell Mark-George
 */

package services.interfaces;

import models.Version;

import java.io.IOException;

import javafx.collections.ObservableList;

public interface ICommandService {

    /**
     * This method is responsible for creating a project based on the provided document path and projectName.
     * @param documentPath the path of the projects associated document
     * @param projectName the name of new project
     * 
     */
    void initProject(String documentPath, String projectName) throws IOException, Exception;  // save the new config config in the cache

    // void newVersion(String comments);

    /**
     * This method is responisble for getting a list of project names
     * @return list of project names
     * 
     */
    ObservableList<String> getProjects() throws IOException;

    // void newVersion(String comments);

    /**
     * This method is responisble for creating a new version of the currently selected project.
     * @param comments comments for the new version
     * @return the new version object
     * 
     */
    Version newVersion(String comments) throws Exception;

    /**
     * This methood is resposible for peeking the currently selected version (create a new file with the state of that version)
     * 
     */
    void peekVersion() throws Exception; // pass verion?

    /**
     * This method is responsible for rolling back to the currently selected version
     * (change the state of the document to the state of the given version)
     * 
     */
    void rollbackVersion() throws Exception; // pass verion?
}
