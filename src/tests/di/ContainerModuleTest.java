/**
 * This class is responsible for tesing the ContainerModule class and all its methods.
 * It extends the TestableAbstract class to ihertit is default behaviour 
 */

package tests.di;
// Nesseccary imports
import org.junit.Assert;
import org.junit.Test;

import di.Container;

import services.interfaces.ICommandService;
import services.interfaces.IErrorService;
import services.interfaces.IFileService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;
import tests.Testable;


public class ContainerModuleTest extends Testable{


    /**
     * This method is responisble for testing the ContainerModule's bootsrap() method
     * Due to the inherited behaviour of the Testable class, the ContainerModule.bootstrap() method
     * is already called before the rest is runn with a tempoary folder as its root
     */
    @Test
    public void testContainerModuleBootstrap() {
        // After ContainerModule.bootstrap() is called

        // All the services should be avalible after the ContainerModule bootstraps
        Assert.assertNotNull(Container.resolveDependency(ICommandService.class));
        Assert.assertNotNull(Container.resolveDependency(IErrorService.class));
        Assert.assertNotNull(Container.resolveDependency(IFileService.class));
        Assert.assertNotNull(Container.resolveDependency(IPathService.class));
        Assert.assertNotNull(Container.resolveDependency(IResourceLoader.class));
        Assert.assertNotNull(Container.resolveDependency(IStateService.class));
    } 
    
}
