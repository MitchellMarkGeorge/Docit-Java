package services.interfaces;

import models.Version;

import java.io.IOException;

import javafx.collections.ObservableList;

public interface ICommandService {

    void initProject(String documentPath, String alias) throws IOException, Exception;  // save the new config config in the cache

    // void newVersion(String comments);

    ObservableList<String> getProjects() throws IOException;

    // void newVersion(String comments);

    Version newVersion(String comments) throws Exception;

    void peekVersion(Version version) throws Exception; // pass verion?

    void rollbackVersion(Version version) throws Exception; // pass verion?
}
