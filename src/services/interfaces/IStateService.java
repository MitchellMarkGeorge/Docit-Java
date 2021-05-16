package services.interfaces;

import javafx.collections.ObservableList;
import javafx.stage.Stage;
import models.Project;
import models.Version;

public interface IStateService { // abstract class
    

    public Object get(String key);

    public void set(String key, Object value);
   
}
