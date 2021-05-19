/**
 * The controller class for the ProjectDetails Stage. 
 * This stage is responsible for showing the details of the current selected project, like the current version, latest version, and the document path.  
 * It implements the Controller interface and its methods.
 * 
 * @author Mitchell Mark-George
 */

package controllers;
//Neccessary importts
import java.net.URL;
import java.util.ResourceBundle;

import di.Container;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Config;
import models.Controller;
import models.Project;
import services.interfaces.IStateService;


public class ProjectDetailsController extends Controller {

    // JavaFX emelemts loaded from the fxml file
    /** label that shows the document path */
    @FXML
    private Label documentPathLabel;

    /** label that shows rthe current version number */
    @FXML
    private Label currentVersionLabel;

    /** label that shows the latest version number */
    @FXML
    private Label latestVersionLabel;

    // Inject the stateService from the dependecy injection container
    IStateService stateService = Container.resolveDependency(IStateService.class);

    /**
     * This method is called when the controller class is created. All inital set up
     * is done here. 
     * precondition: the application is running and the main stage is showing
     * 
     * posetcondition: the dialog stage has finished loading
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // the curent project object is retreived from the stateService
        Project currentProject = (Project) stateService.get("currentProject");

        // the project config
        Config config = currentProject.getConfig();
        // sets the label text to the document path
        documentPathLabel.setText(config.get("DOCUMENT_PATH"));

        String currentVersion = config.get("CURRENT_VERSION");
        String latestVersion = config.get("LATEST_VERSION");
        
        // when a fresh project is created, they will both keys will be 0. There is also
        // no other time that one will be zero as soon as a version is created. The least
        // version it can EVER go back to is 1. a version number of 0 is basically null
        if (currentVersion.equals("0") && latestVersion.equals("0")) {
           currentVersion += " (No Versions created)";
            latestVersion += " (No Versions created)";
        }

        // sets the text for the two labels
        currentVersionLabel.setText(currentVersion);
        latestVersionLabel.setText(latestVersion);

    }

    @Override
    public void onLoading() {
        
       

    }

    @Override
    public void onClosing() {
        // TODO Auto-generated method stub

    }
}