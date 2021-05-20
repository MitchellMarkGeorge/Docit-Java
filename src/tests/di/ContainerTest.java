package tests.di;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import di.Container;
import services.StateService;
import services.interfaces.IStateService;

public class ContainerTest {


    // GO THROUGH THIS AGAIN!!!!



    @Before
    public void emptyContainer() {
        Container.resetDependencies();
    }
    
    @Test
    public void testBindDependencyRebind() {

        boolean hasError = false;

        try {
            Container.bindDependency(IStateService.class, new StateService());
            Container.bindDependency(IStateService.class, new StateService());
        } catch (Error e) {
            e.printStackTrace();
            hasError = true;
           
        }

        Assert.assertTrue(hasError);

    }

    @Test
    public void testBindDependencyNullInterfacetype() {

        boolean hasError = false;

        try {
            Container.bindDependency(null, new StateService());
            // Container.bindDependency(IStateService.class, new StateService());
        } catch (Error e) {
            e.printStackTrace();
            hasError = true;
           
        }

        Assert.assertTrue(hasError);

    }

    @Test
    public void testBindDependencyNullInstance() {

        boolean hasError = false;

        try {
            Container.bindDependency(IStateService.class, null);
            
        } catch (Error e) {
            e.printStackTrace();
            hasError = true;
           
        }

        Assert.assertTrue(hasError);

    }

    @Test
    public void testBindDependencyNullBoth() {

        boolean hasError = false;

        try {
            Container.bindDependency(null, null);
            
        } catch (Error e) {
            e.printStackTrace();
            hasError = true;
           
        }

        Assert.assertTrue(hasError);

    }



    @Test
    public void testResolveDependencyEmpty() {

        boolean hasError = false;
        IStateService stateService = null;
        try {
            // Container.bindDependency(IStateService.class, new StateService());
            // Container.bindDependency(IStateService.class, new StateService());
            stateService = Container.resolveDependency(IStateService.class);
        } catch (Error e) {
            e.printStackTrace();
            hasError = true;
           
        }

        Assert.assertTrue(hasError);
        Assert.assertNull(stateService);

        

    }

    @Test
    public void testResolveDependencyNull() {

        boolean hasError = false;
        IStateService stateService = null;
        try {
            Container.bindDependency(IStateService.class, new StateService());
            // Container.bindDependency(IStateService.class, new StateService());
            stateService = Container.resolveDependency(null);
        } catch (Error e) {
            e.printStackTrace();
            hasError = true;
           
        }

        Assert.assertTrue(hasError);
        Assert.assertNull(stateService);

        

    }

    @Test
    public void testBindandResolveDependency() {

        boolean hasError = false;
        IStateService stateService = null;
        try {
            Container.bindDependency(IStateService.class, new StateService());
            // Container.bindDependency(IStateService.class, new StateService());
            stateService = Container.resolveDependency(IStateService.class);
        } catch (Error e) {
            e.printStackTrace();
            hasError = true;
           
        }

        Assert.assertFalse(hasError);
        Assert.assertNotNull(stateService);

        

    }

    @Test
    public void testResetDependecy() {

        boolean hasError = false;
        IStateService stateService = null;
        try {
            Container.resetDependencies();

            stateService = Container.resolveDependency(IStateService.class);
        } catch (Error e) {
            e.printStackTrace();
            hasError = true;
           
        }

        Assert.assertTrue(hasError);
        Assert.assertNull(stateService);

    }


}
