/**
 * This interface defines what a StateService class should do (how it behaves) and the methods it should implement. 
 * 
 * @author Mitchell Mark-George
 */

package services.interfaces;


public interface IStateService {
    

    /**
     * This method is responsible for retreving an item from the state using a viven
     * @param key the key of the requested item
     * @return the requested item
     */
    public Object get(String key);

    /**
     * This method is responsible for storing an item in the state using a key
     * @param key a key to reference the item in state
     * @param value the item to be soredd in state
     */
    public void set(String key, Object value);
   
}
