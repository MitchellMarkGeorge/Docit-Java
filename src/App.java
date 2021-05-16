
// import java.net.URL;
import di.ContainerModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
// import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
// import javafx.scene.control.Label;
// import javafx.scene.control.ListView;
// import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Controller;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static void configureContainer() {

    }

    @Override
    public void start(Stage mainStage) throws Exception {

        ContainerModule.bootstrap();

        mainStage.setTitle("Docit");
        mainStage.setWidth(800);
        mainStage.setHeight(600);
        mainStage.setMinHeight(600);
        mainStage.setMinWidth(800);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/fxml/new.fxml"));
        // loader.setController(new MainController()); // should i do this for the main
        // controller?
        Parent root = loader.load();

        mainStage.getIcons().add(new Image("/resources/icons/Docit Logo.png"));

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

        Controller controller = loader.getController();
        controller.setStage(mainStage);
        mainStage.show();

    }
}
