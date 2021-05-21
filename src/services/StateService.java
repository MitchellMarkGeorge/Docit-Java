/**
 * This service is responsible for maintaining the “state” (needed information) that the entire application would need. 
 * This includes the current Project, the current project name, and if a version was selected on the version table, it would store the current selected version object.
 * 
 * This service implements the IStateService interface and all of its methods.
 * 
 * @author Mitchell Mark-George
 */

package services;
//Nesseccary imports
import java.util.HashMap;
import java.util.Map;

import services.interfaces.IStateService;

public class StateService implements IStateService {
    /** a map used to store all the information in the state */
    private Map<String, Object> stateMap = new HashMap<String, Object>();

    /**
     * This inherited method is responsible for storing an item in the state using a key
     * @param key a key to reference the item in state
     * @param value the item to be soredd in state
     */
    @Override
    public void set(String key, Object value) {
        // if the item is already in state, it will override the previous value
        stateMap.put(key, value);

    }

    /**
     * This method is responsible for retreving an item from the state using a viven
     * @param key the key of the requested item
     * @return the requested item
     */
    @Override
    public Object get(String key) {
        // if the requested item is not in the map, it will return null
        return stateMap.get(key);
        
        
    }
}
