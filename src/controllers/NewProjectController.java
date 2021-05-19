/**
 * The controller class for the NewProject Stage. 
 * This stage allows the user to create a new project using a project name and the path to the document. 
 * It implements the Controller interface and its methods.
 * 
 * @author Mitchell Mark-George
 */

// Neccessary imports
package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // JavaFX emelemts loaded from the fxml file

    /**test field for project name */
    @FXML
    private TextField projectNameTextField;

    /** button to trigger filechooser for document path */
    @FXML
    private Button selectFileButton;

    /** button that creates the bitton */
    @FXML
    private Button createProjectButton;

    /** button to close the button */
    @FXML
    private Button closeButton;

    /** label that shows the selected document path */
    @FXML
    Label selectedPathLabel;

    /** variable that stores the selected document path for use in other methods */
    private String documentPath;

    
    // Injecting the needed services from the dependency injection container
    IStateService stateService = Container.resolveDependency(IStateService.class); // cast it to T
    IErrorService errorService = Container.resolveDependency(IErrorService.class);
    IPathService pathService = Container.resolveDependency(IPathService.class);
    ICommandService commandService = Container.resolveDependency(ICommandService.class);

    /**
     * This method is called when the controller class is created. All inital set up
     * is done here. 
     * precondition: the application is running and the main stage is showing
     * 
     * posetcondition: the dialog stage has finished loading
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // the createProjectButton will automatically become disabled whenever 
        // the projectNameTextField is empty
        createProjectButton.disableProperty().bind(projectNameTextField.textProperty().isEmpty());

    }

    @Override
    public void onClosing() {

        // resetDialog();

    }

    @Override
    public void onLoading() {

    }

    /**
     * This method is responsible for opening a Word Document file chooser and saving the result (displaying it on the UI)
     *  This method is triggered when the selectFileButton is pressed
     * 
     * precondition: 
     * postcondition: a Word document file is selected and the path to that file is saved
     */
    @FXML
    public void openFileChooser() {
        // A new file chooser is created
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Word Document");
        // the inital directory is set as the home directory (where the user can see all of their folders and files)
        fileChooser.setInitialDirectory(new File(pathService.getHomeDir()));
        // a filter is used so only Word document files are chosen
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Word Documents", "*.doc", "*.docx"));
        // open the file chooser dialog and get the result
        File result = fileChooser.showOpenDialog(stage);

        if (result != null) {
            // if the result is not null,
            // save the absolute path of the document path in a vaiable
            // and update the selectedPathLabel element with the absolute path 
            this.documentPath = result.getAbsolutePath();
            System.out.println(documentPath);
            selectedPathLabel.setText(documentPath);
        }
        

    }

    /** This method is triggered by the createProjectButton and is responsible for getting all of the neccerary infomation
     * (document path, projectName) and create a new project. It also updates the MainStage UI (the listView) with the name of the new project.
     * 
     * precondition: the user has filled out the form correctly
     * postcondition: a new project is created and the dialog is closed
     */
    @FXML
    public void createProject() {

        try {

        

            // get the project name from the text field
            String projectName = projectNameTextField.getText(); // is required
            // if any of the needed details are not null or blank (length == 0 or just whitespce)
            if (projectName != null && !projectName.isBlank() && this.documentPath != null) { // even if this is not here, it will still be caught in the Command service
                // test if there are any illegal characters in the given project name
                // illegal characters do not do well in the filesystem so this is important
                // A regex macher is used to test the projectname
                Pattern pattern = Pattern.compile("[~!&$#@*+%{}<>\\[\\]|\"\\_^]");
                Matcher matcher = pattern.matcher(projectName);
                
                // if there any illegal characters found, show and error dialog.
                if (matcher.find()) { // TEST THIS
                     errorService.showErrorDialog("The project name cannot contain any illegal characters.");
                     return;
                }
                
                // create a new project using the document path and projectName
                commandService.initProject(this.documentPath, projectName);
                
                @SuppressWarnings("unchecked") // casting to this type gives a warning in my editor
                // the state should have some kind of type safety checking

                // get the list of project names from the stateService
                ObservableList<String> projectList = (ObservableList<String>) stateService.get("projectList");
              
                // add the neew project name to the projectList
                // (because it is observable, it will update in the listview UI element automatically)
                projectList.add(projectName);

                
                // close the dialog when done
                stage.close();

            } else {
                // show trhe error dialog is any of the needed details are invalid
                errorService.showErrorDialog("No field can be empty. A document path and project name must be chosen.");
            }
        } catch (Exception e) {
            // If there is an exception, the errror dialog is shown.
            e.printStackTrace();
            errorService.showErrorDialog("There was an error in creating the new project. There might already be a project with the same name,.");

        }

    }

    /**
     * This method is triggered by the closeButton and is responsible for closing the NewProject dialog stage
     * 
     * precondition: the stage must be open
     * postcondition: the stage must be closed
     */
    @FXML
    public void closeStage() {
        
        stage.close();
    }

    

}
