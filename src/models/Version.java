package models;

// import org.reflections.Store;
public class Version {
    private String versionNumber;
    private String fileHash;
    private String comments;
    private String date;

    // this constructor is too big
    public Version(String versionNumber, String fileHash, String date, String comments) {
        this.versionNumber = versionNumber;
        this.fileHash = fileHash;
        this.date = date;
        this.comments = comments;
        
    }

    

    public String getComments() {
        return comments;
    }

    public String getDate() {
        return date;
    }

    public String getFileHash() {
        return fileHash;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("javafx.version")); 
       
    }

    @Override
    public String toString() {
        
        return versionNumber + " " + fileHash + " " + date + " " + comments;
    }

    

}
