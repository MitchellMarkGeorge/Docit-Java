/**
 * This interface defines what an ErrorService class should do (how it behaves) and the methods it should implement. 
 * 
 * @author Mitchell Mark-George
 */

package services.interfaces;

public interface IErrorService {
    
    /**
     * This method is responsible for showing an error dialog with the given text
     * @param text text to show in the error dialog
     */
    void showErrorDialog(String text);
}
