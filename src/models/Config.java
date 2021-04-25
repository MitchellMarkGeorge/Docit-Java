
package models;


import java.util.Properties;

/**
 * This class repreents a Projects config. This class stores infomation like the
 * current and latest document version, and the path to the document
 * 
 */
public class Config {

    
    private Properties properties;

    public Config(Properties properties) {
        this.properties = properties;
    }
    // use a map instead

    /**
     * A constructor that sets the values of the fields
     * 
     * @param documentPath   The path of the document that the project manages
     * @param currentVersion The current version of the document that the user works
     *                       on
     * @param latestVersion  The latest version that was created
     */

    public void set(String key, String value) {
     
        this.properties.setProperty(key, value);
    }

    public String get(String key) {
        return this.properties.getProperty(key);
    }

    public Properties getProperties() {
        return properties;
    }


}
