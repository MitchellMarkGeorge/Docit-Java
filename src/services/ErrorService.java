package services;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import services.interfaces.IErrorService;

public class ErrorService implements IErrorService {
    

    @Override
    public void showErrorDialog(String text) {
        Alert alert  = new Alert(AlertType.ERROR);
        alert.setTitle("Error Alert");
        alert.setHeaderText("An Error Occured");
        alert.setContentText(text);

        alert.show(); // showAndWait()?
        
    }
}
