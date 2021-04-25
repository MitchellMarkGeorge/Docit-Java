package services;

import javafx.stage.Stage;
import services.interfaces.IStateService;

public class StateService implements IStateService {



    private String projectName;
    private Stage mainStage;
    private Stage newProjectStage;
    public PathService pathService = new PathService(); // dont need this
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
        this.pathService.updateProjectName(projectName);
         // it would be

    }

    
}
