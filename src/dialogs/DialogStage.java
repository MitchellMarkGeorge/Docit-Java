package dialogs;

import javafx.stage.Stage;
import models.Controller;

import java.io.IOException;

import di.Container;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.interfaces.IStateService;

public class DialogStage extends Stage {// better name

    private IStateService stateService = (IStateService) Container.resolveDependency(IStateService.class);
    // private Controller controller;

    public DialogStage(String title, String fxmlPath) throws IOException { // do this for the MainController
        super();
        // this.controller = controller;
        this.initOwner(stateService.getMainStage());
        this.setTitle(title);
        this.setHeight(300);
        this.setWidth(400);
        this.setResizable(false);
        

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource(fxmlPath));
        // loader.setController(controller); // this.controller
        Parent root = loader.load();

        // lifecycle hooks
        Controller controller = loader.getController();
        this.setOnShowing(event -> {
            controller.onLoad(); // can change name
        });

        this.setOnHiding(event -> {
            controller.onClosing(); // can change name
        });

        this.setScene(new Scene(root));


        
    }
}
