package services.interfaces;

import javafx.collections.ObservableList;
import javafx.stage.Stage;

public interface IStateService {
    // private String projectName;
    // private PathService pathService;
   

    public String getProjectName();

    public void setProjectName(String projectName);

    public Stage getMainStage();

    public void setMainStage(Stage stage);

    public Stage getNewProjectStage();

    public void setNewProjectStage(Stage stage);

    public void addProject(String projectName);

    public void setProjectList(ObservableList<String> projectList);

    public ObservableList<String> getProjectList();
}
