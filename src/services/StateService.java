package services;



import javafx.stage.Stage;
import models.Project;
import models.Version;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;
import services.interfaces.IStateService;

public class StateService implements IStateService {

    // just use a map?


    private Map<String, Object> stateMap = new HashMap<String, Object>();



    
    @Override
    public void set(String key, Object value) {
        stateMap.put(key, value);
        
    }
    // shuld i use enums for key names??
    @Override
    public Object get(String key) {
        // can be null
        return stateMap.get(key);
    }    
}
