package controllers;

import models.Controller;
import models.Version;
import services.interfaces.ICommandService;
import services.interfaces.IStateService;
import di.Container;
import dialogs.NewProjectDialog;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
// import javafx.scene.control.Label;
// import javafx.scene.control.ListView;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;
// import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
// import javafx.scene.control.SplitPane.Divider;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController implements Controller {

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

        if (projects.size() > 0) {
            listView.setItems(projects);

            stateService.setProjectList(projects); // do they all have the same reference? They do! This means if i add a project to the state service, it will reflect in the local project valie

            projects.addListener((ListChangeListener<String>) change -> {

                while (change.next()) {
                    if (change.wasAdded()) {
                        System.out.println(change.getAddedSubList().get(0));
                        // System.out.println(stateService.getProjectList());
                                
                    } 
                }
            });

        } else {
            listView.setPlaceholder(new Label("No Projects"));
        }

        tableView.getSelectionModel().selectedItemProperty().addListener((ov, oldO, newO) -> {
            System.out.println(newO);
        });

        // PropertyValueFactory

        // listView.setOnMouseClicked(e -> {
        // System.out.println(listView.getSelectionModel().getSelectedItem());
        // });

        listView.getSelectionModel().selectedItemProperty().addListener((ov, oldS, newS) -> {
            System.out.println("Old " + oldS + " New " + newS);
            projectLabel.setText(newS);
        });

        // listView.getSelectionModel().getSelectedIndex()
        // test.children
        // listView.getItems().add("Hello");
        // listView.setItems(value);

        // tableView.setPlaceholder(new Label("No version"));

    }

    @FXML
    public void createNewVersion(ActionEvent event) {
        System.out.println("Hello");
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
