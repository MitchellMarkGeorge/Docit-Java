
package models;


import java.util.Properties;

/**
 * This class repreents a Projects config. This class stores infomation like the
 * current and latest document version, and the path to the document
 * 
 */
public class Config {

    
    private Properties properties;

    public Config() {
        this.properties = new Properties();
    }

    public Config(Properties properties) { 
        this.properties = properties;
    }
    
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
