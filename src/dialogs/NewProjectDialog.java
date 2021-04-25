package dialogs;

import java.io.IOException;
import java.net.URL;

import di.Container;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.interfaces.IStateService;

public class NewProjectDialog extends Stage { // use as sependency

    public NewProjectDialog() throws Exception {
        super();
        // should i do this now?
        IStateService stateService = (IStateService) Container.resolveDependency(IStateService.class);
        initOwner(stateService.getMainStage());

        
        this.setTitle("Create Title");
        this.setHeight(300);
        this.setWidth(400);
        this.setResizable(false);

        FXMLLoader loader = new FXMLLoader();
        
        // System.out.println(getClass().getResource("/resources/fxml/newproject.fxml"));
        loader.setLocation(getClass().getResource("/resources/fxml/newproject.fxml"));
        // // loader.setController(new MainController());
        Parent root = loader.load();

      

        this.setScene(new Scene(root));
        // stage.setScene(scene);
        // this.show();


    }

    
}
