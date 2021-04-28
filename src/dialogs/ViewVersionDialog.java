package dialogs;

import java.io.IOException;

import controllers.ViewVersionController;
import di.Container;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.interfaces.IStateService;

public class ViewVersionDialog extends Stage {
    
    IStateService stateService = (IStateService) Container.resolveDependency(IStateService.class);

    public ViewVersionDialog() throws IOException {
        super();

        initOwner(stateService.getMainStage());

        this.setTitle("View Version");
        this.setHeight(300);
        this.setWidth(400);
        this.setResizable(false);

        FXMLLoader loader = new FXMLLoader();
        
        // System.out.println(getClass().getResource("/resources/fxml/newproject.fxml"));
        loader.setLocation(getClass().getResource("/resources/fxml/viewversion.fxml"));
        // // loader.setController(new MainController());
        Parent root = loader.load();

      

        this.setScene(new Scene(root));

        // very hacky
        // use this to set up lifecycle hooks!
        this.setOnShowing(event -> {
            ViewVersionController controller = (ViewVersionController)  loader.getController();
            // need to tell the controller to load the current version and update its element
            controller.onLoad();
          });
    }

    
}
