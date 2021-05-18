package tests.services;

import org.junit.Test;

import di.Container;
import org.junit.Assert;
import services.interfaces.IStateService;
import tests.Testable;

public class StateServiceTest extends Testable {

    @Test
    public void testGetEmpty() {
        IStateService stateService = Container.resolveDependency(IStateService.class);

        // should not happen
        //If null, the surrounding code will throw error (as the object would be required)
        Object result = stateService.get("projectName");

        Assert.assertNull(result);

    }

    @Test // Dont have any other way to test
    public void testGetAndSet() {
        IStateService stateService = Container.resolveDependency(IStateService.class);

        // should not happen
        //If null, the surrounding code will throw error (as the object would be required)
        String expected = "test";
        stateService.set("projectName", "test");
        String result = (String) stateService.get("projectName");

        Assert.assertNotNull(result);
        Assert.assertEquals(expected, result);

    }
    
}
