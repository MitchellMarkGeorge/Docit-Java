package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import di.Container;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import models.Controller;
import services.interfaces.ICommandService;
import services.interfaces.IErrorService;
import services.interfaces.IPathService;
import services.interfaces.IStateService;

public class NewProjectController extends Controller {

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

    IStateService stateService = Container.resolveDependency(IStateService.class); // cast it to T
    IErrorService errorService = Container.resolveDependency(IErrorService.class);
    IPathService pathService = Container.resolveDependency(IPathService.class);
    ICommandService commandService = Container.resolveDependency(ICommandService.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        createProjectButton.disableProperty().bind(projectNameTextField.textProperty().isEmpty());

    }

    @Override
    public void onClosing() {

        // resetDialog();

    }

    @Override
    public void onLoading() {

    }

    @FXML
    public void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Word Document");
        fileChooser.setInitialDirectory(new File(pathService.getHomeDir()));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Word Documents", "*.doc", "*.docx"));
        // Stage newProjectStage = (Stage) stateService.get("newProjectStage");
        File result = fileChooser.showOpenDialog(stage);

        if (result != null) {
            this.documentPath = result.getAbsolutePath();
            System.out.println(documentPath);
            selectedPathLabel.setText(documentPath);
        }
        //  else {
        //     errorService.
        // }

    }

    @FXML
    public void createProject() {

        try {

        


            String projectName = projectNameTextField.getText(); // is required
            if (projectName != null && !projectName.isBlank() && this.documentPath != null) { // even if this is not here, it will still be caught in the Command service
                
                String ILLEGAL_CHARACTERS = "!@#$%&*()'+,-./:;<=>?[]^_`{|}";

                if (projectName.contains(ILLEGAL_CHARACTERS)) { // TEST THIS
                     errorService.showErrorDialog("The project name cannot contain any illegal characters: " + ILLEGAL_CHARACTERS);
                     return;
                }
                
                commandService.initProject(this.documentPath, projectName);
                // ig
                @SuppressWarnings("unchecked") // casting to this type gives a warning
                // the state should have some kind of type safety checking
                ObservableList<String> projectList = (ObservableList<String>) stateService.get("projectList");
                // stateService.addProject(projectName);

                projectList.add(projectName);

                // ObservableList<String>

                // resetDialog();
                // Stage newProjectStage = (Stage) stateService.get("newProjectStage");
                // stateService.getNewProjectStage().close();
                // newProjectStage.close();

                stage.close();

            } else {
                errorService.showErrorDialog("No field can be empty. A document path and project name must be chosen.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorService.showErrorDialog(
                    "There was an error in creating the new project. There might already be a project with the same name,.");

        }

    }

    @FXML
    public void closeStage() {
        // resetDialog(); // is this still needed here?
        // // Stage newProjectDialog = stateService.getNewProjectStage();

        // Stage newProjectStage = (Stage) stateService.get("newProjectStage");
        // newProjectStage.close();

        stage.close();
    }

    // private void resetDialog() {
    // this.documentPath = null;
    // projectNameTextField.setText(null);
    // selectedPathLabel.setText("None Selected");
    // }

}
