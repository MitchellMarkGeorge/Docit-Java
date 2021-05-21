/**
 * This class is responsible for tesing the StateService class and all its methods.
 * It extends the Testable abstract class to ihertit is default behaviour 
 * 
 * @author Mitchell Mark-George
 */ 

package tests.services;

import org.junit.Test;

import di.Container;
import org.junit.Assert;
import services.interfaces.IStateService;
import tests.Testable;

public class StateServiceTest extends Testable {

    /**
     * This method tests getting an item that is not in the state
     */
    @Test
    public void testGetEmpty() {
        // Injecting the needed services from the dependency injection container
        IStateService stateService = Container.resolveDependency(IStateService.class);

        
        //If null, the surrounding code will throw error (as the object would be required)
        Object result = stateService.get("projectName");
        // the result should be null
        Assert.assertNull(result);

    }

    /**
     * This method tests storing and retreving information from the state
     */
    @Test 
    public void testGetAndSet() {
        // Injecting the needed services from the dependency injection container
        IStateService stateService = Container.resolveDependency(IStateService.class);
        // the expected result
        String expected = "test";
        // store the item in the state
        stateService.set("projectName", "test");
        // get the expected result from the state
        String result = (String) stateService.get("projectName");
        // the result cant be null
        Assert.assertNotNull(result);
        // the expected and result should be the same
        Assert.assertEquals(expected, result);

    }
    
}
