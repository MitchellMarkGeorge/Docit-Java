package controllers;

import java.util.Optional;

import di.Container;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.ListView;
// import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
// import javafx.scene.control.SplitPane.Divider;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Controller;
import models.Project;
import models.Version;
import services.interfaces.ICommandService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;

//should show the current and latest version version SOMEHOW

public class MainController implements Controller { // need to fix this

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
    IStateService stateService = (IStateService) Container.resolveDependency(IStateService.class);
    ICommandService commandService = (ICommandService) Container.resolveDependency(ICommandService.class);
    IResourceLoader resouceService = (IResourceLoader) Container.resolveDependency(IResourceLoader.class);
    IPathService pathService = (IPathService) Container.resolveDependency(IPathService.class);

    // Initializable

    @Override
    public void initialize() {

        
        // new TextField().repl
        // TODO Auto-generated method stub
        // System.out.println(listView.getItems());
        // listView.setPlaceholder(new Label("No Projects"));

        versionColumn.setCellValueFactory(new PropertyValueFactory<>("versionNumber"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        commentsColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));

        // tableView.getItems().add(new Version("0", "00908jy", "Today", "No
        // Comments"));
        // tableView.getItems().add(new Version("1", "fjdofjdo232", "Today", "No
        // Comments"));
        // test.getScene().getWindow();

        // tableView.getSelectionModel().getSelec
        // for (int i = 0; i <= 6; i++) {
        // listView.getItems().add("Hello " + i);
        // }

        // figure this out!!!!
        ObservableList<String> projects = commandService.getProjects();
        stateService.setProjectList(projects);
        listView.setItems(projects);

        if (projects.isEmpty()) {
            listView.setPlaceholder(new Label("No Projects")); // use button
        }
        // projects.addListener((ListChangeListener<String>) change -> {

        // while (change.next()) {
        // if (change.wasAdded()) {

        // String newItem = change.getAddedSubList().get(0);
        // System.out.println(newItem);
        // listView.getItems().add(newItem);

        // // listView.rem
        // // System.out.println(stateService.getProjectList());

        // }
        // }
        // });
        // even if it is empty
        // if (projects.size() > 0) {
        // listView.setItems(projects);

        // // do they all have the same reference? They do! This means if i add a
        // project to the state service, it will reflect in the local project valie

        // } else {
        // listView.setPlaceholder(new Label("No Projects"));

        // }

        tableView.getSelectionModel().selectedItemProperty().addListener((ov, oldO, newO) -> {
            // this event is called when a project is seleted and a row was
            // previously selected, leading to it being null
            if (newO != null) {
                System.out.println("Table selected");
                System.out.println(newO);

                stateService.setCurrentVersion(newO);
                stateService.getViewVersionStage().show();
            }

        });

        // PropertyValueFactory

        // listView.setOnMouseClicked(e -> {
        // System.out.println(listView.getSelectionModel().getSelectedItem());
        // });

        listView.getSelectionModel().selectedItemProperty().addListener((ov, oldS, newS) -> {
            // System.out.println("Old " + oldS + " New " + newS);
            System.out.println("List selected");

            // stateService.setProjectName(newS);

            Project selectedProject = resouceService.loadProject(newS);

            stateService.setProjectName(newS);
            stateService.setCurrentProject(selectedProject);

            projectLabel.setText(newS);
            ObservableList<Version> projectVersions = selectedProject.getVersions();
            tableView.setItems(projectVersions);

        });

        // listView.getSelectionModel().getSelectedIndex()
        // test.children
        // listView.getItems().add("Hello");
        // listView.setItems(value);

        // tableView.setPlaceholder(new Label("No version"));

    }

    @Override
    public void onLoad() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClosing() {
        // TODO Auto-generated method stub

    }

    @FXML
    public void createNewVersion(ActionEvent event) {
        // System.out.println("Hello");

        if (stateService.getProjectName() != null) {
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Create Version");
            dialog.setContentText("Enter comments:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent() && result.get() != null) {
                String comments = result.get();

                commandService.newVersion(comments);
            }
            System.out.println(result.isPresent());

        }
    }

    public void peekVersion(ActionEvent event) {

    }

    public void rollbackVersion(ActionEvent event) {

    }

    public void viewVersion(ActionEvent event) {

    }

    public void showNewProjectDialog(ActionEvent event) {
        Stage newProjectDialog = this.stateService.getNewProjectStage();

        newProjectDialog.show();
    }

}
