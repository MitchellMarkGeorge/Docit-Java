/**
 * This class is the model of a Project. It stores a project’s versions list and associated Config object.
 * @author Mitchell Mark-George
 */

package models;

import javafx.collections.ObservableList;

public class Project {
    /** the Config object for the project */
    private Config config;
    /** the list of versions for the project */
    private ObservableList<Version> versions;

    /**
     * This constructor creates a new Project object based on the project config and 
     * @param config Config object for the project
     * @param versions list of versions for the project
     */
    public Project(Config config, ObservableList<Version> versions) {
        this.config = config;
        this.versions = versions;
    }

    /**
     * getter method for the Config object
     * @return Config object of project
     */
    public Config getConfig() {
        return config;    
    }

    /**
     * getter method for list of versions
     * @return list of versions of the project
     */
    public ObservableList<Version> getVersions() {
        return versions;
    }

}

    
