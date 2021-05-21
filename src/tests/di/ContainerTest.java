/**
 * This class is responsible for tesing the Container class and all its methods.
 * 
 * @author Mitchell Mark-George
 */

package tests.di;
// Nesseccary imports
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import di.Container;
import services.StateService;
import services.interfaces.IStateService;

// Does not extend the Testable class as some funcionaluty has to be tested that the Testable class' behaviour prohibits
public class ContainerTest {


    

    /**
     * This method is responsible for clearing the Container of all dependecies before and individual test is run.
     * This makes sure each test stars with a clean and removes any unwanted side-effects, making the tests more truthful and realistic
     *  
     */ 
    @Before
    public void emptyContainer() {
        Container.resetDependencies();
    }
    
    /**
     * This method is responsible for testing the Continer's rule of not binding a dependecy twice
     */
    @Test
    public void testBindDependencyRebind() {
        // error flag
        boolean hasError = false;

        try {
            // trying to bind the same dependency twicw
            Container.bindDependency(IStateService.class, new StateService());
            Container.bindDependency(IStateService.class, new StateService());
        } catch (Error e) {
            e.printStackTrace();
            // error occured
            hasError = true;
           
        }
        // An error should be thrown if there is an attempt to bin twice
        Assert.assertTrue(hasError);

    }

    /**
     * This method is resonsible for tesing is the Container can reciev an interfaceType of null (should not)
     */
    @Test
    public void testBindDependencyNullInterfacetype() {
        // error flag
        boolean hasError = false;

        try {
            // tries to bind an interfaceType of null to a dependecy
            Container.bindDependency(null, new StateService());
            
        } catch (Error e) {
            e.printStackTrace();
            // error occured
            hasError = true;
           
        }
        // an error must  be trown if interfaceType is null
        Assert.assertTrue(hasError);

    }

    /**
     * This method tests if a classInstance (dependecy) is null
     */
    @Test
    public void testBindDependencyNullInstance() {

        // error flag
        boolean hasError = false;

        try {
            // tries to bind an interfaceType to null
            Container.bindDependency(IStateService.class, null);
            
        } catch (Error e) {
            e.printStackTrace();
            //error occured
            hasError = true;
           
        }
        // error must be thrown if classInstace is null
        Assert.assertTrue(hasError);

    }

    /**
     * This method is responsible for tesing if both classInstace and interfaceType are null
     */
    @Test
    public void testBindDependencyNullBoth() {
        // error flag
        boolean hasError = false;

        try {
            // tries to bind null to null
            Container.bindDependency(null, null);
            
        } catch (Error e) {
            e.printStackTrace();
            // error occured
            hasError = true;
           
        }

        // error must be trown if interfaceType or classInstace is null
        Assert.assertTrue(hasError);

    }



    /**
     * This method test resoplving a dependecy from an empty Container
     */
    @Test
    public void testResolveDependencyEmpty() {
        // error flag
        boolean hasError = false;
        // dependency to be retreived from the Container
        IStateService stateService = null;
        try {
            // tries to get the service from the empty Container
            stateService = Container.resolveDependency(IStateService.class);
        } catch (Error e) {
            e.printStackTrace();
            // error occured
            hasError = true;
           
        }

        // an error must be thrown if there is attempt to get a dependency that is not in the Contaier (or if it is emptu)
        Assert.assertTrue(hasError);
        // the dependency should be null
        Assert.assertNull(stateService);

        

    }

    /**
     * This method test resolving a dependency of tupe null
     */
    @Test
    public void testResolveDependencyNull() {
        // error flag
        boolean hasError = false;
        // dependency to be retreived from the the Contaienr
        IStateService stateService = null;
        try {
            // bind an interfaceType to a dependecy (class instace)
            Container.bindDependency(IStateService.class, new StateService());
            // try and retreive a dependecy bout to thhe interfaceType of null
            stateService = Container.resolveDependency(null);
        } catch (Error e) {
            e.printStackTrace();
            // error occured
            hasError = true;
           
        }
        // exception should be thrown if an attempt to resolve an interfaceType of null
        Assert.assertTrue(hasError);
        // the dependency should be null
        Assert.assertNull(stateService);

        

    }

    /**
     * This method should test the binding and resolving of a dependency
     */
    @Test
    public void testBindandResolveDependency() {
        // error flag
        boolean hasError = false;
        // the dependecy should be resolved and stored to this variable
        IStateService stateService = null;
        try {
            // bind a class instace (dependency) to an interfaceType
            Container.bindDependency(IStateService.class, new StateService());
            // resolve the dependecy based on the interfaceType and save it to a variable
            stateService = Container.resolveDependency(IStateService.class);
        } catch (Error e) {
            e.printStackTrace();
            // error occured
            hasError = true;
           
        }

        // an error should not occur when resolving or binding the dependency
        Assert.assertFalse(hasError);
        // the variable should not be null after resolving the dependency
        Assert.assertNotNull(stateService);

        

    }

    /**
     * This method is responsible for tesing reseting the Container
     */
    @Test
    public void testResetDependecy() {
        // error flag
        boolean hasError = false;
        // the dependecy should be resolved and stored to this variable
        IStateService stateService = null;
        try {
            // add a dependency to the Container
            Container.bindDependency(IStateService.class, new StateService());
            // reset and clear all dependencies from the Contaiern
            Container.resetDependencies();
            // try to resolve to a dependecy
            stateService = Container.resolveDependency(IStateService.class);
        } catch (Error e) {
            e.printStackTrace();
            // error occured
            hasError = true;
           
        }

        // an error shoudld occur is an attempt is made to resolve adependecny after the Container has been reset
        Assert.assertTrue(hasError);
        // the variable storing the resolved result should remain null
        Assert.assertNull(stateService);

    }


}
