package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import di.Container;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Controller;
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

    IErrorService errorService = (IErrorService) Container.resolveDependency(IErrorService.class);
    IStateService stateService = (IStateService) Container.resolveDependency(IStateService.class);
    ICommandService commandService = (ICommandService) Container.resolveDependency(ICommandService.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) { // current version should be set as a local field
        Version currentVersion = (Version) stateService.get("currentVersion");

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
            Version version = (Version) stateService.get("currentVersion");
            commandService.peekVersion(version);
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
            Version version = (Version) stateService.get("currentVersion");
            commandService.rollbackVersion(version);
            stage.close();
        } catch (Exception e) {
            errorService.showErrorDialog("There was an error in rolling back the version. Make sure the file is closed");
        }

    }

    public void close() {
        // Stage viewVersionStage = stateService.getViewVersionStage();
        // Stage viewVersionStage = (Stage) stateService.get("viewVersionStage");
        // viewVersionStage.close();
    }
}
