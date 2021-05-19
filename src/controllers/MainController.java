/**
 * The controller class for the main stage of the application. 
 * This stage has the menu, the ListView for projects, and a TableView to display the current projects versions. 
 * It also implements various methods that are triggered by buttons and menu items. It implements the Controller interface and its methods.
 * 
 * @author Mitchell Mark-George
 */

package controllers;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import cache.Cache;
import di.Container;
import dialogs.DialogStage;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Controller;
import models.Project;
import models.Version;
import services.interfaces.ICommandService;
import services.interfaces.IErrorService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;

public class MainController extends Controller {

    // JavaFX emelemts loaded from the fxml file

    /** label that displays the current project name */
    @FXML
    private Label projectLabel;

    /** show list of project names */
    @FXML
    private ListView<String> listView;

    /** button used to create new version of current project */
    @FXML
    private Button newVersionButton;

    /** shows versions of current project */
    @FXML
    private TableView<Version> tableView;

    /** column of the tableview */
    @FXML
    private TableColumn<Version, String> versionColumn;

    /** column of the tableview */
    @FXML
    private TableColumn<Version, String> dateColumn;

    /** columns of the tableview */
    @FXML
    private TableColumn<Version, String> commentsColumn;

    /** button to show ProjectDetailsDialog for details of the current project */
    @FXML
    private Button projectDetailsButton;

    // Injecting the needed services from the dependency injection container
    IStateService stateService = Container.resolveDependency(IStateService.class);
    IErrorService errorService = Container.resolveDependency(IErrorService.class);
    ICommandService commandService = Container.resolveDependency(ICommandService.class);
    IResourceLoader resouceService = Container.resolveDependency(IResourceLoader.class);
    IPathService pathService = Container.resolveDependency(IPathService.class);

    // creates an instace of the the cache to store the loaded project
    Cache<Project> cache = new Cache<Project>(5);
    // only 5 projects in the cache at a time

    /**
     * This method is called when the controller class is created. All inital set up
     * is done here. 
     * precondition: the stage is loading and the application itself
     * is starting (the conrtoller is being loaded) 
     * 
     * posetcondition: the stage and the application has finished loading
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // The table columns are setup to show details from a given Version object
        versionColumn.setCellValueFactory(new PropertyValueFactory<>("versionNumber"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));

        // disables the buttons as there is no selected project on loading
        // this prevents the attached event listeners from being called
        projectDetailsButton.setDisable(true);
        newVersionButton.setDisable(true);

        // load the list of projects from the filesystem
        this.loadProjects();

        // Adds event listener for when item in tableview is selected
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldVersion, newVersion) -> {
            this.onTableClick(newVersion);

        });

        // Adds event listener for when item in listview is selected
        listView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldProjectName, newProjectName) -> {
                    this.onListClick(newProjectName);
                });

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onClosing() {
        // TODO Auto-generated method stub

    }

    /**
     * This method is responsible for loading the project names from the file
     * systrem. It updates the state service and the listview with the received
     * items
     * 
     * precondition: the applicationa and stage are still loading postcondition: the
     * state is updated with the list of project names. If there are any projects, a
     * palaceholder is set in the listview. Otheriese, the list of project name is
     * displayed
     */
    public void loadProjects() {

        try {
            // get the list of project names from the commandService
            ObservableList<String> projects = commandService.getProjects();
            // save the returned result to the stateservice
            stateService.set("projectList", projects);
            // set the the returned result to the listview
            listView.setItems(projects);

            // if there are no projects, set a placeholder
            if (projects.isEmpty()) {
                listView.setPlaceholder(new Label("No Projects"));
            }
        } catch (Exception e) {
            // If there is an exception, an errror dialog is shown.
            e.printStackTrace();
            errorService.showErrorDialog("There was an error trying to load projects");

        }

    }

    /**
     * This method is called when an item in the tableview is selected This in turn
     * updates the state service, and opens the ViewVersion DialogStage, with
     * information of that version.
     * 
     * @param version the version to view
     * 
     * precondition: an item in the tableview is selected (there are
     * items/version in the tableview in the first place) the version
     * object should be of type Version and can be null (when swiching between projects)
     *  
     * postcondition: the version is set in the state service and a ViewVersion dialog should be 
     * showing with information of that version (filename of the version file, date created, any comments, version number)
     */
    public void onTableClick(Version version) {

        if (version != null) {
            try {
                // if the version object is not null,
                // set the given version as the currently select version inthe stateService
                stateService.set("currentVersion", version);
                // create a new DialogStage to show the information of the selected version and
                // show the dialog
                Stage viewVersionStage = new DialogStage("View Version", "/resources/fxml/viewversion.fxml", stage);
                viewVersionStage.show();
            } catch (Exception e) {
                // If there is an exception, an errror dialog is shown.
                e.printStackTrace();
                errorService.showErrorDialog("There was a problem viewing the version.");

            }

        }
    }

    /**
     * This method is called when an item in the listview is selected. This in turn
     * gets the project with that project name (either from the cache or the
     * filesystem), and saves it to the stateservice. It also updates some of the UI
     * elements (like the tableview and projectLabel) with the neccessary data
     * 
     * @param projectName the name of the requested project
     * 
     *                    precondition: projectName is a string and projectName is
     *                    the name of an existing project postcondition: the project
     *                    name and the loaded project assocciated with it are saved
     *                    in the sate service and the UI is updated with the
     *                    neccessary information
     */
    public void onListClick(String projectName) {

        try {
            // if the project is in the cache (meaning it has been selected before)
            // get it from the cache
            Project selectedProject;
            if (cache.has(projectName)) {
                System.out.println("in cache");
                selectedProject = cache.get(projectName);
            } else {
                // otherwise, load the project from the filesysem and add it to the cache
                System.out.println("not in cache");
                selectedProject = resouceService.loadProject(projectName);
                cache.set(projectName, selectedProject);
            }

            // if the projectDetails and newVersion buttons are diabled
            // (they would be if there is no selected project), enable them
            if (projectDetailsButton.isDisabled() || newVersionButton.isDisabled()) {
                projectDetailsButton.setDisable(false);
                newVersionButton.setDisable(false);
            }

            // update the projectName and current project object in the stateService
            stateService.set("projectName", projectName);
            stateService.set("currentProject", selectedProject);

            projectLabel.setText(projectName);
            // get the file versions from the project object
            ObservableList<Version> projectVersions = selectedProject.getVersions();
            // update the tableView with the list of versions
            tableView.setItems(projectVersions);
        } catch (Exception e) {
            // If there is an exception, an errror dialog is shown.
            e.printStackTrace();

            errorService.showErrorDialog("There was an error in loading the projects.");
        }

    }

    /**
     * This method is called when the newVersionButton is pressed. It is responsible
     * for creating a TextInputDialog for any comments (optional) and recording a
     * new version in the project (reflecting in the UI)
     * 
     * precondition: there must be a current project selected postcondition: a new
     * version of the current project is created and saved to the filesystem
     */
    @FXML
    public void createNewVersion() {
        // System.out.println("Hello");

        // gets the projectName from the stateService
        String projectName = (String) stateService.get("projectName");

        try {
            if (projectName != null) {
                // if the the returned value is not null,
                // create a TextInputDialog to enter any comments
                TextInputDialog dialog = new TextInputDialog();

                dialog.setTitle("Create Version");
                dialog.setContentText("Enter comments:");

                // show the dialog and get the reseult of the text
                Optional<String> result = dialog.showAndWait();
                // if the result is present (meaning the the dialog was not dismissed)
                if (result.isPresent()) {
                    // get the string result
                    String comments = result.get();
                    // if the result is a balcnk string (has a length of 0 or is just whitespace),
                    // set the default comment
                    if (comments.isBlank()) {
                        comments = "No Comment";
                    }

                    // replace all whitespace (if any) with underscores
                    // this is neccessary to be able to parse the version later properly from the
                    // version file.
                    comments = comments.replaceAll(" ", "_");

                    // create a new version using the given comment
                    Version newVersion = commandService.newVersion(comments);

                    // (should not be null but it is still good to check)
                    if (newVersion != null) {

                        // get the currentProject from the stateService

                        Project currentProject = (Project) stateService.get("currentProject");
                        // het the list of versions from the project object
                        ObservableList<Version> projectVersions = currentProject.getVersions();
                        // add the new version to the list of version
                        // since the list is observable, the tableView will automatically update to show
                        // this new verion
                        projectVersions.add(newVersion);

                    }

                }

            }
        } catch (Exception e) {
            // If there is an exception, an errror dialog is shown.
            e.printStackTrace();

            errorService.showErrorDialog("There was an error creating a new version of the document.");
        }

    }

    /**
     * This methd is triggered by the Project menu item. As the name suggests, it
     * simply creates a new DialogStage to show the NewProject dialog, which
     * allows users to pick a project name and document path for their new project.
     * 
     * precondition: there are no specific condidtions postcondition: the NewProject
     * dialog is shown
     */
    @FXML
    public void showNewProjectDialog() {

        try {
            // creates a new NewProject dialog to show a form for the user to create a new
            // project and displays the dialog
            Stage newProjectDialog = new DialogStage("Create Project", "/resources/fxml/newproject.fxml", stage);
            newProjectDialog.show();
        } catch (Exception e) {
            // If there is an exception, an errror dialog is shown.
            e.printStackTrace();
            errorService.showErrorDialog("There was an error in opening the dialog.");// think of better message
        }

    }

    /**
     * This method is triggered when the showProjectDetailsButton is pressed.
     * This method creates a enw ProjectDetails dialog that diaplays information about the current project
     * (current version number, latest version number, document path)
     * 
     * preconditions: there must be a current project (projectName in state);
     * postcondition: the ProjectDetails dialog is shown
     */
    @FXML
    public void showProjectDetails() {
        // get the current projectName from the state service 
    
        String projectName = (String) stateService.get("projectName");
        
        if (projectName != null) {
            // if the retreived result is not null (meaning there is a project selected)
            // create a ProjectDetails diolog to show the details of the current project and show it.
            try {
                Stage projectDetailsStage = new DialogStage("Project Details", "/resources/fxml/projectdetails.fxml",
                        stage);
                projectDetailsStage.show();
            } catch (Exception e) {
                // If there is an exception, an errror dialog is shown.
                e.printStackTrace();
                errorService.showErrorDialog("There was an error in displaying the project details.");

            }

        }

    }

}
