package services.interfaces;

import models.Version;
import java.util.List;

import javafx.collections.ObservableList;

public interface ICommandService {

    void initProject(String documentPath, String alias); // save the new config config in the cache

    // void newVersion(String comments);

    ObservableList<String> getProjects();

    void newVersion(String comments);



    void peekVersion(Version version); // pass verion?

    void rollbackVersion(Version version); // pass verion?
}
