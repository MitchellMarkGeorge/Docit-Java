package services;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import services.interfaces.IErrorService;

public class ErrorService implements IErrorService {
    

    @Override
    public void showErrorDialog(String text) {
        // Cant test this service as the application must be running for this to work
        Alert alert  = new Alert(AlertType.ERROR);
        alert.setTitle("Error Alert");
        alert.setHeaderText("An Error Occured");
        alert.setContentText(text);

        alert.show(); // showAndWait()?

        // throw new Error(text);
        
    }
}
