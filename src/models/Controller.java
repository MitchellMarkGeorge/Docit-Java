package models;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

// public interface Controller extends Initializable {
//     // public void initialize();
//     public void onLoading();
//     public void onClosing();
//     public void setStage(Stage stage);
    
// }

public abstract class Controller implements Initializable { // should this be an abstract class

    protected Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public abstract void onLoading(); // might not be needed anymore
    public abstract void onClosing(); // might not be needed anymore
    

}
