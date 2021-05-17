package services.interfaces;


public interface IStateService { // abstract class
    

    public <T> Object  get(String key);

    public <T> void set(String key, T value);
   
}
