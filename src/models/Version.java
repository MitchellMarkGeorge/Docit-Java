/**
 * This class is the model of a project version. 
 * It stores information like the date it was created, comments for that version (if any), version number, and the name of the associated version file that stores the state of the document at that version in the “timeline”
 * 
 * @author Mitchell Mark-George
 * 
 */
package models;


public class Version {

    /** version numebr */
    private String versionNumber;
    /** name of the associated version file */
    private String fileName ;
    /** comments for that version (or the default "No Comments") */
    private String comments;
    /** date the version was created */
    private String date;

    /**
     * This contrcutor creates a new Version object based on the provided information
     * @param versionNumber version number
     * @param fileName comments for that version (or the default) 
     * @param date date the version was created
     * @param comments
     */
    public Version(String versionNumber, String fileName, String date, String comments) {
        this.versionNumber = versionNumber;
        this.fileName = fileName;
        this.date = date;
        this.comments = comments;
        
    }

    
    /**
     * getter for version comments
     * @return comments of the version
     */
    public String getComments() {
        // this is done for the view
        return comments.replaceAll("_", " ");
    }

    /**
     * getter for the version date
     * @return date version was created
     */
    public String getDate() {
        return date;
    }

    /**
     * getter for the version file name
     * @return name of the associated version file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * getter for the version numer
     * @return version number
     */
    public String getVersionNumber() {
        return versionNumber;
    }

    /**
     * This method is responsible for concatenating all of the version onformation into a single string.
     * This method is used when trying to append a version to a project's versions file
     */
    @Override
    public String toString() {
        
        return versionNumber + " " + fileName + " " + date + " " + comments;
    }

    

}
