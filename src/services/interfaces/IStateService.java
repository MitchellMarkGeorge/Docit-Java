package services.interfaces;

import javafx.collections.ObservableList;
import javafx.stage.Stage;
import models.Project;
import models.Version;

public interface IStateService {
    // private String projectName;
    // private PathService pathService;
   

    public String getProjectName();

    public void setProjectName(String projectName);

    public Stage getMainStage();

    public void setMainStage(Stage stage);

    public Stage getNewProjectStage();

    public void setNewProjectStage(Stage stage);

    public Stage getViewVersionStage();

    public void setViewVersionStage(Stage stage);

    public void addProject(String projectName);

    public void setProjectList(ObservableList<String> projectList);

    public void setCurrentProject(Project project);

    public Project getCurrentProject();

    public Version getCurrentVersion();

    public void setCurrentVersion(Version version);

    public ObservableList<String> getProjectList();
}
