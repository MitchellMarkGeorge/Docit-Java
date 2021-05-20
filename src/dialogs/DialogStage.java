/**
 * This is an extended stage that the NewProject, ProjectDetails, and ViewVersion stage are created from. 
 * It accepts the Stage title and FXML file path, and mainStage object. 
 * The purpose of this class is to provide functionality to automatically load FXML and call the controller hooks at the appropriate time. 
 * It also gives the accposiated controller class an instace of itself to use  (for example to close the dialog).
 * @author Mitchell Mark-George
 */

package dialogs;

// Neccessary imports
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Controller;


public class DialogStage extends Stage {

    
    /**
     * This constructor is responsible for creating the stage for the dialog, based on the provided information.
     * It also sets up the controller and gives it eh instace of the stage
     * @param title the tiltle of the dialog
     * @param fxmlPath the path to the c
     * @param mainStage the stage of the main application
     * @throws IOException if there is a oroblem loading the FXML file, an exception is thrown
     */

    public DialogStage(String title, String fxmlPath, Stage mainStage) throws IOException { // do this for the MainController
        super();
        
        // seting up the stage
        this.initOwner(mainStage);
        this.setTitle(title);
        this.setHeight(300);
        this.setWidth(400);
        this.setResizable(false);
        

        FXMLLoader loader = new FXMLLoader();
        // sets the location of the fxml
        loader.setLocation(getClass().getResource(fxmlPath));
        // loads the fxml and gets the root of loaded JavaFx elements
        Parent root = loader.load();

        // gets the controller and gives it the instace of the stage
        Controller controller = loader.getController();
        controller.setStage(this);
        this.setOnShowing(event -> {
            controller.onLoading(); // can change name

        });

        this.setOnHiding(event -> {
            controller.onClosing(); // can change name

            
        });

        
        // sets the scene of the stage based on the loaded root
        this.setScene(new Scene(root));


        
    }
}
