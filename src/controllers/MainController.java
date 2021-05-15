package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import di.Container;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Project;
import models.Version;
import services.interfaces.ICommandService;
import services.interfaces.IErrorService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;

import cache.Cache;




//should show the current and latest version version SOMEHOW

public class MainController implements Initializable { // need to fix this

    @FXML
    private Label projectLabel;
    @FXML
    private ListView<String> listView;
    @FXML
    private Button newVersionButton;
    @FXML
    private TableView<Version> tableView;

    // @FXML private String hello;
    @FXML
    private TableColumn<Version, String> versionColumn;
    @FXML
    private TableColumn<Version, String> dateColumn;
    @FXML
    private TableColumn<Version, String> commentsColumn;

    @FXML private Button projectDetailsButton;

    IStateService stateService = (IStateService) Container.resolveDependency(IStateService.class);
    IErrorService errorService = (IErrorService) Container.resolveDependency(IErrorService.class);
    ICommandService commandService = (ICommandService) Container.resolveDependency(ICommandService.class);
    IResourceLoader resouceService = (IResourceLoader) Container.resolveDependency(IResourceLoader.class);
    IPathService pathService = (IPathService) Container.resolveDependency(IPathService.class);

    // Initializable

    Cache<Project> cache = new Cache<Project>(5); 
    // only 5 projects in the cache



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


        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldProjectName, newProjectName) -> {
            this.onListClick(newProjectName);
        });

        


    }

    public void loadProjects() {

        try {
            ObservableList<String> projects = commandService.getProjects();
            stateService.setProjectList(projects);
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
            System.out.println("Table selected");
            System.out.println(version);

            stateService.setCurrentVersion(version);
            
            

            stateService.getViewVersionStage().showAndWait(); // could have used a custome method here

            // I nned to de select the selection as if the window 

            // throws an errpr
            // int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
            // tableView.getSelectionModel().clearSelection(selectedIndex);  
        }
    }

    public void onListClick(String projectName) {

        Project selectedProject;
        if (cache.has(projectName)) {
            System.out.println("in cache");
            selectedProject = cache.get(projectName);
        } else {
            System.out.println("not in cache");
            selectedProject = resouceService.loadProject(projectName);
            cache.set(projectName, selectedProject);
        }   

        if (projectDetailsButton.isDisabled() || newVersionButton.isDisabled()) { // should iot be and
            projectDetailsButton.setDisable(false);
            newVersionButton.setDisable(false);
        }
         

            stateService.setProjectName(projectName);
            stateService.setCurrentProject(selectedProject);

            projectLabel.setText(projectName);
            ObservableList<Version> projectVersions = selectedProject.getVersions();
            // System.out.println(projectVersions);
            tableView.setItems(projectVersions);
    }

    @FXML
    public void createNewVersion() {
        // System.out.println("Hello");

        if (stateService.getProjectName() != null) {
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

                    Project currentProject = stateService.getCurrentProject();

                    ObservableList<Version> projectVersions = currentProject.getVersions();

                    projectVersions.add(newVersion);

                }

            }
            // System.out.println(result.isPresent());

        }
    }

    public void showNewProjectDialog() {
        Stage newProjectDialog = this.stateService.getNewProjectStage();

        newProjectDialog.show();
    }

    @FXML
    public void showProjectDetails() {
        // there must be a selsect project to show the details stage
        if (stateService.getProjectName() != null) {
            stateService.getProjectDetailsStage().show();
        }

    }

}
