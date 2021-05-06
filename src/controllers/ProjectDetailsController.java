package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import di.Container;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Config;
import models.Controller;
import services.interfaces.IStateService;
/**
 * ProjectVersionsController
 */
public class ProjectDetailsController implements Controller {

    @FXML
    private Label documentPathLabel;

    @FXML
    private Label currentVersionLabel;

    @FXML
    private Label latestVersionLabel;

    IStateService stateService = (IStateService) Container.resolveDependency(IStateService.class);
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoad() {
        // TODO Auto-generated method stub

     Config config = stateService.getCurrentProject().getConfig();

     documentPathLabel.setText(config.get("DOCUMENT_PATH"));
     currentVersionLabel.setText(config.get("CURRENT_VERSION"));
     latestVersionLabel.setText(config.get("LATEST_VERSION"));

    }

    @Override
    public void onClosing() {
        // TODO Auto-generated method stub

    }
}