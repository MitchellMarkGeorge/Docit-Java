package services.interfaces;

public interface IPathService {

   

    public void updateProjectName(String projectName);
    
    public String getProjectPath();

    public String getVersionsPath();

    public String getConfigPath();

    public String getHomeDir();
    
    public String getDocitPath();

    public String basename(String filePath);
}
