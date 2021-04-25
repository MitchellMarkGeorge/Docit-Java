package services.interfaces;

import java.lang.module.ModuleDescriptor.Version;
import java.util.List;

import javafx.collections.ObservableList;

public interface ICommandService {

    void initProject(String documentPath, String alias); // save the new config config in the cache

    // void newVersion(String comments);

    ObservableList<String> getProjects();

    // void peek(Version version); // pass verion?
}
