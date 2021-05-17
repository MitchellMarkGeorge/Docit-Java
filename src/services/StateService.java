package services;

import java.util.HashMap;
import java.util.Map;

import services.interfaces.IStateService;

public class StateService implements IStateService {

    private Map<String, Object> stateMap = new HashMap<String, Object>();

    @Override
    public <T> void set(String key, T value) { // should the type 
        stateMap.put(key, value);

    }

    // shuld i use enums for key names??
    @Override
    public <T> Object get(String key) { // type security
        // can be null
        return stateMap.get(key);
        
        // return (T) 
    }
}
