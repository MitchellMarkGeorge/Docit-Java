package controllers;

import java.io.File;

import di.Container;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Controller;
import services.PathService;
import services.interfaces.IPathService;
import services.interfaces.IStateService;

public class NewProjectController implements Controller {

    @FXML TextField aliasTextField;

    @FXML Button selectFileButton;

    @FXML Button createProjectButton;

    @FXML Button closeButton;

    @FXML Label selectedPathLabel;

    private String documentPath;

    IStateService stateService = (IStateService) Container.resolveDependency(IStateService.class); // cast it to T
    IPathService pathService = (IPathService) Container.resolveDependency(IPathService.class);
    
    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        
    }

    @FXML
    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Word Document");
        fileChooser.setInitialDirectory(new File(pathService.getHomeDir()));
        File result = fileChooser.showOpenDialog(stateService.getNewProjectStage());

        if (result != null) {
            this.documentPath = result.getAbsolutePath();
            System.out.println(documentPath);
            selectedPathLabel.setText(documentPath);
        }

    }

    @FXML
    public void createProject() {
        
    }

    @FXML
    public void closeStage() {
       Stage newProjectDialog =  stateService.getNewProjectStage();
       newProjectDialog.close();
    }

}

