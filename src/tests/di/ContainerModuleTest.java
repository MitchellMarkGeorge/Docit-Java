package tests.di;

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

// does not textend Testabe to actually test the container
public class ContainerModuleTest extends Testable{


    //Cannot test is th
    @Test
    public void testContainerModule() {
        // ContainerModule.bootstrap(null);


        Assert.assertNotNull(Container.resolveDependency(ICommandService.class));
        Assert.assertNotNull(Container.resolveDependency(IErrorService.class));
        Assert.assertNotNull(Container.resolveDependency(IFileService.class));
        Assert.assertNotNull(Container.resolveDependency(IPathService.class));
        Assert.assertNotNull(Container.resolveDependency(IResourceLoader.class));
        Assert.assertNotNull(Container.resolveDependency(IStateService.class));
        
        

        

    } // test a non existent root File object
    
}
