/**
 * This class is responsible for setting up the Container for global use in the application. 
 * All of the binding happens in this class.
 * A File object is passed in to determine the file root (nessecarry for testing)
 * 
 * @author Mitchell Mark-George
 */

package di;
//Nesseccary imports
import java.io.File;

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
    

    /**
     * This method is responsible for binding all of the nesseccary services and their types in the Container.
     * It passes the root folder of the current
     * @param root the root of the given execution enviroment (production or testing). 
     * If provided, it is assumeed to be in a testing enviroment (root would probably be a temproary folder). 
     * Otherwise, the root is set as the user's home directory (production enviroment)
     * 
     * precondition: the Contaier must be empty and of the root is provided, it must be a File object (and must exist)
     */
    public static void bootstrap(File root) {
        // the provided root (if any) must exist
        if ( root != null && !root.exists()) {
            throw new Error("Provided root must exist");
        }
        
        // THE ORDER OF THE DEPENDENCIES IS IMPORTANT
        // The services that have the most dependeces should be put near the bottom
        

        // binds service instaces to their types for global use
        Container.bindDependency(IStateService.class, new StateService());
        Container.bindDependency(IErrorService.class, new ErrorService());
        Container.bindDependency(IPathService.class, new PathService(root));
        Container.bindDependency(IFileService.class, new FileService());
        Container.bindDependency(IResourceLoader.class, new ResourceLoader());
        Container.bindDependency(ICommandService.class, new CommandService());

        

     
    }
}
