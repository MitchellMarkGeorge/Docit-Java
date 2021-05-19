package tests.services;

import org.junit.Assert;
import org.junit.Test;

import di.Container;
import services.interfaces.IErrorService;
import tests.Testable;

public class ErrorServiceTest extends Testable {
    


    @Test
    public void testShowshowErrorDialog() {
        IErrorService errorService = Container.resolveDependency(IErrorService.class);
        // the Alert requores the application to be started to work, so it will throw an Error instead
        boolean hasError = false;

        try {
            errorService.showErrorDialog("There was an error in the test");
        } catch (Exception | Error e) {
            e.printStackTrace();
            hasError = true;
        }

        Assert.assertTrue(hasError);
        
    }
}
