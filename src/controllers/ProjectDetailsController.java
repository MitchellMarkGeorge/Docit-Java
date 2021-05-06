package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Controller;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLoad() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClosing() {
        // TODO Auto-generated method stub

    }
}