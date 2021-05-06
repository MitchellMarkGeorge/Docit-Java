import di.Container;
import dialogs.DialogStage;
import javafx.stage.Stage;
import services.CommandService;
import services.ErrorService;
import services.FileService;
import services.PathService;
import services.ResourceLoader;
import services.StateService;
import services.interfaces.ICommandService;
import services.interfaces.IErrorService;
import services.interfaces.IFileService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;

public class ContainerModule {
    

    public static void bootstrap(Stage mainStage) throws Exception {
        // THE ORDER OF THE DEPENDENCIES IS IMPORTANT
        // The services that have the most dependeces should be put near the bottom
        StateService stateService = new StateService(); // should be configured sperately
        Container.bindDependency(IStateService.class, stateService); // the state service MUST be avalible before the
                                                                     // controller is loaded by FXML loader
        Container.bindDependency(IErrorService.class, new ErrorService());
        Container.bindDependency(IPathService.class, new PathService());
        Container.bindDependency(IFileService.class, new FileService());
        Container.bindDependency(IResourceLoader.class, new ResourceLoader());
        // Container.bindDependency(IHashService.class, new HashService()); // no longer needed
        Container.bindDependency(ICommandService.class, new CommandService());

        stateService.setMainStage(mainStage);
        Stage newProjectStage = new DialogStage("Create Project", "/resources/fxml/newproject.fxml");
        Stage viewVersionStage = new DialogStage("View Version", "/resources/fxml/viewversion.fxml");
        Stage projectDetailStage = new DialogStage("Project Details", "/resources/fxml/projectdetails.fxml");
        stateService.setNewProjectStage(newProjectStage);
        stateService.setViewVersionStage(viewVersionStage);
        stateService.setProjectDetailsStage(projectDetailStage);
    }
}
