/**
 * The controller class for the main stage of the application. T
 * his stage has the menu, the ListView for projects, and a TableView to display the current projects versions. 
 * It also implements various methods that are triggered by buttons and menu items.
 * 
 * @author Mitchell Mark-George
 */

package controllers;

//Nesseccary imports
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
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

import cache.Cache;

public class MainController extends Controller {

    // JavaFX emelemts loaded from the fxml file
    @FXML
    private Label projectLabel;
    @FXML
    private ListView<String> listView;
    @FXML
    private Button newVersionButton;
    @FXML
    private TableView<Version> tableView;
    @FXML
    private TableColumn<Version, String> versionColumn;
    @FXML
    private TableColumn<Version, String> dateColumn;
    @FXML
    private TableColumn<Version, String> commentsColumn;
    @FXML
    private Button projectDetailsButton;

    //Injecting the needed services from the dependency injection container
    IStateService stateService = Container.resolveDependency(IStateService.class);
    IErrorService errorService = Container.resolveDependency(IErrorService.class);
    ICommandService commandService = Container.resolveDependency(ICommandService.class);
    IResourceLoader resouceService = Container.resolveDependency(IResourceLoader.class);
    IPathService pathService = Container.resolveDependency(IPathService.class);

    // Initializable

    Cache<Project> cache = new Cache<Project>(5);
    // only 5 projects in the cache at a time

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        versionColumn.setCellValueFactory(new PropertyValueFactory<>("versionNumber"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));

        // should they be disabled???
        projectDetailsButton.setDisable(true); //
        newVersionButton.setDisable(true);

        this.loadProjects();

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldVersion, newVersion) -> {
            this.onTableClick(newVersion);

        });

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

    public void loadProjects() {

        try {
            ObservableList<String> projects = commandService.getProjects();
            // stateService.setProjectList(projects);
            stateService.set("projectList", projects);
            listView.setItems(projects);

            if (projects.isEmpty()) {
                listView.setPlaceholder(new Label("No Projects"));
            }
        } catch (IOException e) {
            e.printStackTrace();
            errorService.showErrorDialog("There was an error trying to load projects");

        }

    }

    public void onTableClick(Version version) {
        if (version != null) {
            try {
                System.out.println("Table selected");
                System.out.println(version);

                // stateService.setCurrentVersion(version);

                stateService.set("currentVersion", version);

                // stateService.getViewVersionStage().showAndWait(); // could have used a
                // custome method here

                Stage viewVersionStage = new DialogStage("View Version", "/resources/fxml/viewversion.fxml", stage); // should
                                                                                                                     // this
                                                                                                                     // be
                                                                                                                     // saved
                viewVersionStage.show();
            } catch (Exception e) {
                e.printStackTrace();
                errorService.showErrorDialog("There was a problem viewing the version.");

            }

        }
    }

    public void onListClick(String projectName) {

        try {
            Project selectedProject;
            if (cache.has(projectName)) {
                System.out.println("in cache");
                selectedProject = cache.get(projectName);
            } else {
                System.out.println("not in cache");
                selectedProject = resouceService.loadProject(projectName);
                cache.set(projectName, selectedProject);
            }

            if (projectDetailsButton.isDisabled() || newVersionButton.isDisabled()) { // should it be and
                projectDetailsButton.setDisable(false);
                newVersionButton.setDisable(false);
            }

            // stateService.setProjectName(projectName);
            stateService.set("projectName", projectName);
            // stateService.setCurrentProject(selectedProject);
            stateService.set("currentProject", selectedProject);

            projectLabel.setText(projectName);
            ObservableList<Version> projectVersions = selectedProject.getVersions();
            // System.out.println(projectVersions);
            tableView.setItems(projectVersions);
        } catch (Exception e) {
            e.printStackTrace();

            errorService.showErrorDialog("There was an error in loading the projects.");
        }

    }

    @FXML
    public void createNewVersion() {
        // System.out.println("Hello");

        String projectName = (String) stateService.get("projectName");

        try {
            if (projectName != null) {
                TextInputDialog dialog = new TextInputDialog();

                dialog.setTitle("Create Version");
                dialog.setContentText("Enter comments:");

                Optional<String> result = dialog.showAndWait();
                // System.out.println(result.get());
                if (result.isPresent()) {
                    String comments = result.get();

                    if (comments.isBlank()) {
                        comments = "No Comment";
                    }

                    comments = comments.replaceAll(" ", "_");

                    Version newVersion = commandService.newVersion(comments);

                    if (newVersion != null) {

                        // Project currentProject = stateService.getCurrentProject();

                        Project currentProject = (Project) stateService.get("currentProject");

                        ObservableList<Version> projectVersions = currentProject.getVersions();

                        projectVersions.add(newVersion);

                    }

                }
                // System.out.println(result.isPresent());

            }
        } catch (Exception e) {
            e.printStackTrace();

            errorService.showErrorDialog("There was an error creating a new version of the document.");
        }

    }

    public void showNewProjectDialog() {
        // Stage newProjectDialog = stateService.getNewProjectStage();

        // Stage newProjectDialog = (Stage) stateService.get("newProjectStage");

        try {
            Stage newProjectDialog = new DialogStage("Create Project", "/resources/fxml/newproject.fxml", stage);
            newProjectDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            errorService.showErrorDialog("There was an error in opening the dialog.");// think of better message
        }

    }

    @FXML
    public void showProjectDetails() {
        // there must be a selsect project to show the details
        String projectName = (String) stateService.get("projectName");

        if (projectName != null) {
            // Stage projectDetailsStage = (Stage) stateService.get("projectDetailsStage");
            // stateService.getProjectDetailsStage().show();
            try {
                Stage projectDetailsStage = new DialogStage("Project Details", "/resources/fxml/projectdetails.fxml",
                        stage);
                projectDetailsStage.show();
            } catch (Exception e) {
                e.printStackTrace();
                errorService.showErrorDialog("There was an error in displaying the project details.");
                // think of better message
            }

        }

    }

}
