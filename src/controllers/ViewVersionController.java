package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import di.Container;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Controller;
import models.Version;
import services.CommandService;
import services.interfaces.ICommandService;
import services.interfaces.IStateService;

public class ViewVersionController implements Controller {

    @FXML
    public Label versionNumberLabel;
    @FXML
    public Label dateLabel;
    
    @FXML
    public Label commentsLabel;

    IStateService stateService = (IStateService) Container.resolveDependency(IStateService.class);
    ICommandService commandService = (ICommandService) Container.resolveDependency(ICommandService.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Version currentVersion = stateService.getCurrentVersion();
        // versionNumberLabel.textProperty().b
        // versionNumberLabel.setText(currentVersion.getVersionNumber());
        // dateLabel.setText(currentVersion.getDate());
        // fileHashLabel.setText(currentVersion.getFileHash());
        // commentsLabel.setText(currentVersion.getComments());

    }

    @Override
    public void onLoad() {
        Version currentVersion = stateService.getCurrentVersion();

        versionNumberLabel.setText(currentVersion.getVersionNumber());
        dateLabel.setText(currentVersion.getDate());
        commentsLabel.setText(currentVersion.getComments());
    }

    @Override
    public void onClosing() {
        // TODO Auto-generated method stub

    }

    @FXML
    public void peekVersion() {
        Version version = stateService.getCurrentVersion();
        commandService.peekVersion(version);
    }

    @FXML
    public void rollbackVersion() {
        Version version = stateService.getCurrentVersion();
        commandService.rollbackVersion(version);
    }
}
