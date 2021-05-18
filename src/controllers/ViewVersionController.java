package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import di.Container;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.Config;
import models.Controller;
import models.Project;
import models.Version;
import services.interfaces.ICommandService;
import services.interfaces.IErrorService;
import services.interfaces.IStateService;

public class ViewVersionController extends Controller {

    @FXML
    public Label versionNumberLabel;
    @FXML
    public Label dateLabel;

    @FXML
    public Label commentsLabel;

    @FXML
    public Button rollbackVersionButton;

    @FXML
    public Button peekVersionButton;

    @FXML
    public Label warningLabel;

    IErrorService errorService = Container.resolveDependency(IErrorService.class);
    IStateService stateService = Container.resolveDependency(IStateService.class);
    ICommandService commandService = Container.resolveDependency(ICommandService.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) { // current version should be set as a local field
       
        Version currentVersion = (Version) stateService.get("currentVersion");


        Project currentProject = (Project) stateService.get("currentProject");
        Config config = currentProject.getConfig();
        String currentProjectVersion = config.get("CURRENT_VERSION");
        if (currentProjectVersion.equals(currentVersion.getVersionNumber())) {
            warningLabel.setVisible(false);
            rollbackVersionButton.setDisable(true); // csnt "rollback" to current version (nothing would change)
            peekVersionButton.setDisable(true); // shoudnt be able to create a seperate file of the current state. Thsi would basicly mean cloning the current document
        } // if the currrently viewed version if the current version in the project, you should not be able to peek or rollback to this version
       
       
       
       
        

        versionNumberLabel.setText(currentVersion.getVersionNumber());
        dateLabel.setText(currentVersion.getDate());
        commentsLabel.setText(currentVersion.getComments());

    }

    @Override
    public void onLoading() {
        // Version currentVersion = stateService.getCurrentVersion();
        // Version currentVersion = (Version) stateService.get("currentVersion");

        // versionNumberLabel.setText(currentVersion.getVersionNumber());
        // dateLabel.setText(currentVersion.getDate());
        // commentsLabel.setText(currentVersion.getComments());
    }

    @Override
    public void onClosing() {
        

    }

    @FXML
    public void peekVersion() {

        try {
            // Version version = stateService.getCurrentVersion();
            
            commandService.peekVersion(); // should i just access the state service in the actual method
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            errorService.showErrorDialog("There was an error in peeking the version.");
        }

    }

    @FXML
    public void rollbackVersion() {

        try {
            // Version version = stateService.getCurrentVersion();
            
            commandService.rollbackVersion();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
            errorService.showErrorDialog("There was an error in rolling back the version. Make sure the file is closed");
        }

    }

    public void close() {
        // Stage viewVersionStage = stateService.getViewVersionStage();
        // Stage viewVersionStage = (Stage) stateService.get("viewVersionStage");
        // viewVersionStage.close();
    }
}
