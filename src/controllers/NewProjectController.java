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
import services.interfaces.ICommandService;
import services.interfaces.IPathService;
import services.interfaces.IStateService;

public class NewProjectController implements Controller {

    @FXML
    TextField projectNameTextField;

    @FXML
    Button selectFileButton;

    @FXML
    Button createProjectButton;

    @FXML
    Button closeButton;

    @FXML
    Label selectedPathLabel;

    private String documentPath;

    IStateService stateService = (IStateService) Container.resolveDependency(IStateService.class); // cast it to T
    IPathService pathService = (IPathService) Container.resolveDependency(IPathService.class);
    ICommandService commandService = (ICommandService) Container.resolveDependency(ICommandService.class);

    @Override
    public void initialize() {
        // TODO Auto-generated method stub
        System.out.println("Loaded");

    }

    @FXML
    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Word Document");
        fileChooser.setInitialDirectory(new File(pathService.getHomeDir()));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Word Documents", "*.doc", "*.docx"));
        File result = fileChooser.showOpenDialog(stateService.getNewProjectStage());

        if (result != null) {
            this.documentPath = result.getAbsolutePath();
            System.out.println(documentPath);
            selectedPathLabel.setText(documentPath);
        }

    }

    @FXML
    public void createProject() {
        String projectName = projectNameTextField.getText(); // is required
        if (projectName != null && !projectName.isBlank()) {
            commandService.initProject(this.documentPath, projectName);

            resetDialog();

            stateService.getNewProjectStage().close();
           
        }
    }

    @FXML
    public void closeStage() {
        resetDialog();
        Stage newProjectDialog = stateService.getNewProjectStage();
        newProjectDialog.close();
    }

    private void resetDialog() {
        this.documentPath = null;
        projectNameTextField.setText(null);
        selectedPathLabel.setText("None Selected");
    }

}
