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
import services.CommandService;
import services.FileService;
import services.HashService;
import services.PathService;
import services.ResourceLoader;
import services.StateService;
import services.interfaces.ICommandService;
import services.interfaces.IFileService;
import services.interfaces.IHashService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;
import controllers.MainController;
import di.Container;
import dialogs.NewProjectDialog;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static void configureContainer() {
        
    }

    @Override
    public void start(Stage mainStage) throws Exception {
        // THE ORDER OF THE DEPENDENCIES IS IMPORTANT
        // The services that have the most dependeces should be put near the bottom
        StateService stateService = new StateService(); // should be configured sperately
        Container.bindDependency(IStateService.class, stateService); // the state service MUST be avalible before the controller is loaded by FXML loader
        Container.bindDependency(IPathService.class, new PathService());
        Container.bindDependency(IFileService.class, new FileService()); 
        Container.bindDependency(IResourceLoader.class, new ResourceLoader());
        Container.bindDependency(IHashService.class, new HashService());
        Container.bindDependency(ICommandService.class, new CommandService());
        
        stateService.setMainStage(mainStage);
        stateService.setNewProjectStage(new NewProjectDialog());
        
        
        
        



        mainStage.setTitle("Docit");
        mainStage.setWidth(800);
        mainStage.setHeight(600);
        mainStage.setMinHeight(600);
        mainStage.setMinWidth(800);
       
        // new HBox().set
        // new ListView().setW

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/fxml/new.fxml"));
        // loader.setController(new MainController());
        Parent root = loader.load();

        // FXMLLoader loader = new FXMLLoader();
        // // URL fxmlUrl = getClass().getResource("/main.fxml");
        // // System.out.println(fxmlUrl);
        // loader.setLocation(new URL("file://C:/Users/monir/docit-java/src/main.fxml"));
        // loader.setController(new MainController()); // put in fxml
        // Parent root = loader.load();

        // Label helloWorldLabel = new Label("Hello world!");
        // helloWorldLabel.setAlignment(Pos.CENTER);
        // Scene scene = new Scene(new MainController() );

        mainStage.setScene(new Scene(root));
        // stage.setScene(scene);
        mainStage.show();

        // UWarning: Nashorn engine is planned to be removed from a future JDK release
    }
}
