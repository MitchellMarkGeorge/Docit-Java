/**
 * This class is responsible for tesing the ErrorService class and all its methods.
 * It extends the Testable abstract class to ihertit is default behaviour 
 * 
 * @author Mitchell Mark-George
 */ 

package tests.services;
// Nesseccary imports
import org.junit.Assert;
import org.junit.Test;

import di.Container;
import services.interfaces.IErrorService;
import tests.Testable;

public class ErrorServiceTest extends Testable {
    

    /**
     * This method tests showing the error dialog
     */
    @Test
    public void testShowErrorDialog() {
        IErrorService errorService = Container.resolveDependency(IErrorService.class);
        // the Alert requores the application to be started to work, so it will throw an Error instead
        // error flag
        boolean hasError = false;

        try {
            // try and show the error dialog
            errorService.showErrorDialog("There was an error in the test");
        } catch (Exception | Error e) {
            e.printStackTrace();
            hasError = true;
            // erroe occured
        }
        // an error should be thrown
        Assert.assertTrue(hasError);
        
    }
}
