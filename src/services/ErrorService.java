/**
 * This service is used to show an Alert Dialog every time an error occurs in the application. 
 * A message is passed in depending on the specified error.
 * This service implements the IErrorService interface and all of its methods.
 * 
 * @author Mitchell Mark-George
 */

package services;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import services.interfaces.IErrorService;

public class ErrorService implements IErrorService {
    

    /**
     * This inherted method is responsible for showing an error dialog with the given text
     * @param text text to show in the error dialog
     * 
     * precondition: an error/ exception has been thrown in the application
     * postcondition: the error dialog is shown
     */
    @Override
    public void showErrorDialog(String text) {
        // create the alert with the neccessary infomation and text
        Alert alert  = new Alert(AlertType.ERROR);
        alert.setTitle("Error Alert");
        alert.setHeaderText("An Error Occured");
        alert.setContentText(text);
        // show the alert
        alert.show(); 
        
    }
}
