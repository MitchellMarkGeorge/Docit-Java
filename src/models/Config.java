/**
 * This class is the model of a project config. 
 * Internally, it uses Java Properties so all the config items can be written to a file easily.
 * It stores information like the latest version number, the current version number, and the path to the document.
 * 
 * @author Mitchell Mark-George
 */


package models;


import java.util.Properties;


public class Config {

    /** internal properties map that stores the information */
    private Properties properties;

    /**
     * This contructor creates a Config object and the needed Properties object to store the config infromation.
     */
    public Config() {
        this.properties = new Properties();
    }

    /**
     * This constructor creates a Config object using the provided property object
     * @param properties the property object used to store the config information
     * 
     * precondition: the property object cannot be null
     * postcondition: a Config object is creates
     */
    public Config(Properties properties) { 
        
        if (properties == null) {
            throw new Error("Properties cannot be null");
        }
        this.properties = properties;
    }
    
    /**
     * This method is responsible for storing an item in the properties map
     * @param key the key of the item stored
     * @param value the item to be stored
     */
    public void set(String key, String value) {
     
        this.properties.setProperty(key, value);
    }

    /**
     * This method is responsible for geting an item from the properties map
     * @param key the key of the requested item
     * @return the requested item
     */
    public String get(String key) {
        return this.properties.getProperty(key);
    }

    /**
     * getter method for properties object
     * @return the properties object
     */
    public Properties getProperties() {
        return properties;
    }


}
