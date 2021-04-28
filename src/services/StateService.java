package services;

import java.util.List;

import javafx.stage.Stage;
import models.Project;
import models.Version;
import javafx.collections.ObservableList;
import services.interfaces.IStateService;

public class StateService implements IStateService {

    // just use a map?

    private String projectName;
    private Stage mainStage;
    private Stage newProjectStage;
    private Stage viewVersionStage;
    private ObservableList<String> projectList;
    private Project currentProject;
    private Version currentVersion;
    // public PathService pathService = new PathService(); // dont need this
    // does this need to be in the global state context?

    @Override
    public String getProjectName() {
        return projectName;
    }

    @Override
    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    @Override
    public Stage getMainStage() {
        return this.mainStage;
    }

    @Override
    public void setNewProjectStage(Stage stage) {
        this.newProjectStage = stage;
    }

    @Override
    public Stage getNewProjectStage() {
        
        return this.newProjectStage;
    }

    @Override
    public void setProjectName(String projectName) {
        this.projectName = projectName;
        // this.pathService.updateProjectName(projectName);
         // it would be

    }

    @Override
    public void addProject(String projectName) {
        this.projectList.add(projectName); // observable list
    }

    @Override
    public void setProjectList(ObservableList<String> projectList) {
        this.projectList = projectList;
    }

    @Override
    public ObservableList<String> getProjectList() {
        return projectList;
    }

    @Override
    public Project getCurrentProject() {
        return this.currentProject;
    }

    @Override
    public void setCurrentProject(Project project) {
        this.currentProject = project;
    }

    @Override
    public Stage getViewVersionStage() {
        
        return this.viewVersionStage;
    }

    @Override
    public void setViewVersionStage(Stage stage) {
        this.viewVersionStage = stage;
    }

    @Override
    public Version getCurrentVersion() {
        // TODO Auto-generated method stub
        return this.currentVersion;
    }

    @Override
    public void setCurrentVersion(Version version) {
        // TODO Auto-generated method stub

        this.currentVersion = version;
        
    }

    
}
