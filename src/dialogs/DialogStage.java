package dialogs;

import javafx.stage.Stage;
import models.Controller;

import java.io.IOException;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class DialogStage extends Stage {// better name

    
    // private Controller controller;

    public DialogStage(String title, String fxmlPath, Stage mainStage) throws IOException { // do this for the MainController
        super();
        // this.controller = controller;
        this.initOwner(mainStage);// should be passed in from controller
        // this.initModality(modality);
        this.setTitle(title);
        this.setHeight(300);
        this.setWidth(400);
        this.setResizable(false);
        

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource(fxmlPath));
        // loader.setController(controller); // this.controller
        Parent root = loader.load();


        // shoudl i be able to get the controller??????


        // lifecycle hooks
        Controller controller = loader.getController();
        controller.setStage(this);
        this.setOnShowing(event -> {
            controller.onLoading(); // can change name

        });

        this.setOnHiding(event -> {
            controller.onClosing(); // can change name

            System.out.println("Closing...");
        });

        

        this.setScene(new Scene(root));


        
    }
}
