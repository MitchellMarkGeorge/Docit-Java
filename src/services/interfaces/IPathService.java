package services.interfaces;

public interface IPathService {

   

    // public void updateProjectName(String projectName);
    
    public String getProjectPath(String projectName);

    public String getVersionsPath(String projectName);

    public String getConfigPath(String projectName);

    public String getHomeDir();
    
    public String getDocitPath();

    public String basename(String filePath);

    public String getVersionFilesPath(String projectName);
}
