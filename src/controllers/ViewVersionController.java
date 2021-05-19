/**
 * The controller class for the ViewVersion dialog. 
 * This dialog shows information about the current selected version, 
 * and it gives the the user the option to peek this version 
 * (create a new file with the state of that version) or rollback (change the state of the document to the state of the given version).  
 * It implements the Controller interface and its methods. 
 * @author Mitchell Mark-George
 */


package controllers;
// Nesseccary imports
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

    // JavaFX emelemts loaded from the fxml file

    /** label to show the version number */
    @FXML
    public Label versionNumberLabel;

    /** label to show the date */
    @FXML
    public Label dateLabel;

    /** label to show the comments */
    @FXML
    public Label commentsLabel;

    /** button to trigger a version rollback */
    @FXML
    public Button rollbackVersionButton;

    /** button to trigger a version peek */
    @FXML
    public Button peekVersionButton;

    /**labvel to show a warning to users */
    @FXML
    public Label warningLabel;

    // Injecting the needed services from the dependency injection container
    IErrorService errorService = Container.resolveDependency(IErrorService.class);
    IStateService stateService = Container.resolveDependency(IStateService.class);
    ICommandService commandService = Container.resolveDependency(ICommandService.class);


    /**
     * This method is called when the controller class is created. All inital set up
     * is done here. 
     * precondition: the application is running and the main stage is showing
     * 
     * posetcondition: the dialog stage has finished loading
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) { // current version should be set as a local field
       
        // the current selected version if retreived from the state service
        Version currentVersion = (Version) stateService.get("currentVersion");

        // the c urrent selected project object is retreived from the the state service
        Project currentProject = (Project) stateService.get("currentProject");
        Config config = currentProject.getConfig();
        // get the current version number
        String currentProjectVersion = config.get("CURRENT_VERSION");
        // if the current version number is the same as version number of the selected version
        // (basically the current version is the latest version),
        // disable the rollbackVersion and peekVersion buttons and hide the warning label
        if (currentProjectVersion.equals(currentVersion.getVersionNumber())) {
            warningLabel.setVisible(false);
            rollbackVersionButton.setDisable(true); // This is because you can't "rollback" to current version (nothing would change)
            peekVersionButton.setDisable(true); // shoudnt be able to create a seperate file of the current state (peek the version). This would basicly mean cloning the current document
        } // if the currrently viewed version is the current version in the project, you should not be able to peek or rollback to this version
       
       
       
       
        
        // populate the labels with their appropriate details
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

    /**
     * This method is triggered when the peekVersionButton is pressed.
     * This method is responsible for executing the peek command and then closing the dialog
     * 
     * precondition: the dialog should be open
     * postcondition: the peek command should  be executed successfuly and the dialog should close
     */
    @FXML
    public void peekVersion() {

        try {
            
            // executes the rollback command and closes the dialog
            commandService.peekVersion();
            stage.close();
        } catch (Exception e) {
            // If there is an exception, the errror dialog is shown.
            e.printStackTrace();
            errorService.showErrorDialog("There was an error in peeking the version.");
        }

    }

    /**
     * This method is triggered when the rollbackVersionButton is pressed.
     * This method is responsible for executing the rollback command and then closing the dialog
     * 
     * precondition: the dialog should be open
     * postcondition: the rollback command should  e executed successfuly and the dialog should close
     */
    @FXML
    public void rollbackVersion() {

        try {
            
            // executes the rollback command and closes the dialog
            commandService.rollbackVersion();
            stage.close();
        } catch (Exception e) {
            // If there is an exception, the errror dialog is shown.
            e.printStackTrace();
            errorService.showErrorDialog("There was an error in rolling back the version. Make sure the file is closed");
        }

    }

    
}
