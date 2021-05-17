package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import di.Container;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Config;
import models.Controller;
import models.Project;
import services.interfaces.IStateService;

/**
 * ProjectVersionsController
 */
public class ProjectDetailsController extends Controller {

    @FXML
    private Label documentPathLabel;

    @FXML
    private Label currentVersionLabel;

    @FXML
    private Label latestVersionLabel;

    IStateService stateService = Container.resolveDependency(IStateService.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Project currentProject = (Project) stateService.get("currentProject");

        Config config = currentProject.getConfig();

        documentPathLabel.setText(config.get("DOCUMENT_PATH"));

        String currentVersion = config.get("CURRENT_VERSION");
        String latestVersion = config.get("LATEST_VERSION");
        // System.out.println(currentVersion.equals("0"));
        // System.out.println(latestVersion.equals("0"));
        // when a fresh project is created, they will both keys will be 0. There is also
        // no other time that one will be zero as soon as a version is created. The least
        // version it can EVER go back to is 1. a version number of 0 is basically null
        if (currentVersion.equals("0") && latestVersion.equals("0")) {
           currentVersion += " (No Versions created)";
            latestVersion += " (No Versions created)";
        }

        currentVersionLabel.setText(currentVersion);
        latestVersionLabel.setText(latestVersion);

    }

    @Override
    public void onLoading() {
        
        // Project currentProject = (Project) stateService.get("currentProject");

        // Config config = currentProject.getConfig();

        // documentPathLabel.setText(config.get("DOCUMENT_PATH"));

        // String currentVersion = config.get("CURRENT_VERSION");
        // String latestVersion = config.get("LATEST_VERSION");
        // System.out.println(currentVersion.equals("0"));
        // System.out.println(latestVersion.equals("0"));
        // // when a fresh project is created, they will both keys will be 0. There is also
        // // no other time that one will be zero as soon as a version is created. The least
        // // version it can EVER go back to is 1. a version 0 is basically null
        // if (currentVersion.equals("0") && latestVersion.equals("0")) {
        //    currentVersion += " (No Versions created)";
        //     latestVersion += " (No Versions created)";
        // }

        // currentVersionLabel.setText(currentVersion);
        // latestVersionLabel.setText(latestVersion);

    }

    @Override
    public void onClosing() {
        // TODO Auto-generated method stub

    }
}