package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import di.Container;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Controller;
import services.interfaces.ICommandService;
import services.interfaces.IErrorService;
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
    IErrorService errorService = (IErrorService) Container.resolveDependency(IErrorService.class);
    IPathService pathService = (IPathService) Container.resolveDependency(IPathService.class);
    ICommandService commandService = (ICommandService) Container.resolveDependency(ICommandService.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        System.out.println("Loaded");

        createProjectButton.disableProperty().bind(projectNameTextField.textProperty().isEmpty());

    }

    @Override
    public void onClosing() {
        // TODO Auto-generated method stub
        resetDialog();

    }

    @Override
    public void onLoad() {
        // TODO Auto-generated method stub

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

        try {
            String projectName = projectNameTextField.getText(); // is required
            if (projectName != null && !projectName.isBlank()) {
                commandService.initProject(this.documentPath, projectName);

                ObservableList<String> projectList = stateService.getProjectList();
                // stateService.addProject(projectName);

                projectList.add(projectName);

                // resetDialog();

                stateService.getNewProjectStage().close();

            }
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
            errorService.showErrorDialog("There was an error in creating the new project.");

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
