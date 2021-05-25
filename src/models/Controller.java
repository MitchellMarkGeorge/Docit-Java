/**
 * This abstract class represents the base type that controller classes that are loadded from FXML files extend from.
 * The Controller class implements the JavaFX Ininitalizable interface that defines a method that is called when the contreoller is being initalized.
 * The class also defines a method for the controller to receive an instance of their corresponding stage object. 
 */

package models;

import javafx.fxml.Initializable;
import javafx.stage.Stage;



public abstract class Controller implements Initializable { 

    /** the corresponding stage object */
    protected Stage stage;

    /**
     * a setter method to set the coresponding stage
     * @param stage the corresponding stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

       

}
