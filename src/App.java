/**
 * This class is the entrypint of the JavaFX application.
 * The mainstage is created here and its fxml is loaded. 
 * The ContainerModule bootraps and binds all dependencies (services)
 * for global use as well.
 */



import di.ContainerModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

import javafx.stage.Stage;
import models.Controller;

public class App extends Application {
    
    public static void main(String[] args) {
        // launches the application -> handled by JavaFX
        launch(args);
    }

  

    /**
     * Main entry of the app
     */
    @Override
    public void start(Stage mainStage) throws Exception {
        // bootstras the dependency injection container and binds all dependencies for global use
        ContainerModule.bootstrap(null); // the root will be retrived from system (home direcotu).

        // sets up the stage
        mainStage.setTitle("Docit");
        mainStage.setWidth(800);
        mainStage.setHeight(600);
        mainStage.setMinHeight(600);
        mainStage.setMinWidth(800);

        // loads the JavaFx elements from the FXML file
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/resources/fxml/new.fxml"));
        Parent root = loader.load(); // gets the root JavaFX element of the FXML
        // sets the icon
        mainStage.getIcons().add(new Image("/resources/icons/Docit Logo.png"));

        // sets the the scene based on the root
        mainStage.setScene(new Scene(root));
        
        // gets the controller and gives it the instace of the stage
        Controller controller = loader.getController();
        controller.setStage(mainStage);
        mainStage.show();

    }
}
