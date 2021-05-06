
// import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
// import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
// import javafx.scene.control.Label;
// import javafx.scene.control.ListView;
// import javafx.scene.layout.HBox;
import javafx.stage.Stage;


public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static void configureContainer() {

    }

    @Override
    public void start(Stage mainStage) throws Exception {
        // have this in sperate "module"

        // THE ORDER OF THE DEPENDENCIES IS IMPORTANT
        // The services that have the most dependeces should be put near the bottom
        // StateService stateService = new StateService(); // should be configured sperately
        // Container.bindDependency(IStateService.class, stateService); // the state service MUST be avalible before the
        //                                                              // controller is loaded by FXML loader
        // Container.bindDependency(IPathService.class, new PathService());
        // Container.bindDependency(IFileService.class, new FileService());
        // Container.bindDependency(IResourceLoader.class, new ResourceLoader());
        // Container.bindDependency(IHashService.class, new HashService());
        // Container.bindDependency(ICommandService.class, new CommandService());

        // stateService.setMainStage(mainStage);
        // Stage newProjectStage = new DialogStage("Create Project", "/resources/fxml/newproject.fxml");
        // Stage viewVersionStage = new DialogStage("View Version", "/resources/fxml/viewversion.fxml");
        // stateService.setNewProjectStage(newProjectStage);
        // stateService.setViewVersionStage(viewVersionStage);

        ContainerModule.bootstrap(mainStage);

        mainStage.setTitle("Docit");
        mainStage.setWidth(800);
        mainStage.setHeight(600);
        mainStage.setMinHeight(600);
        mainStage.setMinWidth(800);

        // new HBox().set
        // new ListView().setW

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/fxml/new.fxml"));
        // loader.setController(new MainController()); // should i do this for the main
        // controller?
        Parent root = loader.load();

        // FXMLLoader loader = new FXMLLoader();
        // // URL fxmlUrl = getClass().getResource("/main.fxml");
        // // System.out.println(fxmlUrl);
        // loader.setLocation(new
        // URL("file://C:/Users/monir/docit-java/src/main.fxml"));
        // loader.setController(new MainController()); // put in fxml
        // Parent root = loader.load();

        // Label helloWorldLabel = new Label("Hello world!");
        // helloWorldLabel.setAlignment(Pos.CENTER);
        // Scene scene = new Scene(new MainController() );

        mainStage.setScene(new Scene(root));
        // stage.setScene(scene);
        mainStage.show();

    }
}
