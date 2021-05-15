package services;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import di.Container;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Config;
import models.Project;
import models.Version;
import services.interfaces.ICommandService;
import services.interfaces.IErrorService;
import services.interfaces.IFileService;
// import services.interfaces.IHashService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;



public class CommandService implements ICommandService {

    IStateService stateService = (StateService) Container.resolveDependency(IStateService.class);
    IPathService pathService = (IPathService) Container.resolveDependency(IPathService.class);
    IResourceLoader resourceLoader = (IResourceLoader) Container.resolveDependency(IResourceLoader.class);
    IFileService fileServce = (IFileService) Container.resolveDependency(IFileService.class);
    IErrorService errorService = (IErrorService) Container.resolveDependency(IErrorService.class);
    // public static void main(String[] args) {
    // System.out.println(Paths.get("C://meme/me.png").getFileName().toString());
    // }

    /**
     * Do siagram to showcase how each command affects the timeline/ how the
     * timeline works ingeneral
     * @throws IOException
     */

    @Override
    public ObservableList<String> getProjects() throws IOException {
        // errorService.showErrorDialog("There was an error in loading the projects");
        // try {
            ObservableList<String> projects = FXCollections.observableArrayList();
            // return null;
            Path docitPath = Path.of(pathService.getDocitPath());

            if (Files.exists(docitPath)) {
                // could have some files
                Files.list(docitPath).map(path -> path.toFile()).filter(file -> file.isDirectory())
                        .forEachOrdered(file -> projects.add(file.getName()));
            }

            return projects; // set to state
        // } catch (IOException e) {
        //     e.printStackTrace();
        //     errorService.showErrorDialog("There was an error in loading the projects");
        //     // System.out.println("Hello");
        //     return null;
        //     // throw new Error("Error in reading the file");
        // } // should only be

    }

    @Override
    public void initProject(String documentPath, String projectName) throws IOException {
        // TODO Auto-generated method stub
        // try {

            /**
             * Here, i should make: the config file empty versions file empty version_files
             * folder
             * 
             */
            pathService.updateProjectName(projectName); // for paths to work

            Path newProjectPath = Paths.get(pathService.getProjectPath());

            // String newProjectPath = pathService.getProjectPath();

            System.out.println(newProjectPath);
            if (Files.exists(newProjectPath)) {
                // throw new Error("Project " + projectName + " already exists");
                errorService.showErrorDialog("Project " + projectName + " already exists");
                return;
            }

            Config newConfig = new Config();
            // String newConfigPath = Paths.get(newProjectPath.toString(),
            // "config").toString();
            String newConfigPath = pathService.getConfigPath();
            System.out.println(newConfigPath);
            newConfig.set("DOCUMENT_PATH", documentPath);
            newConfig.set("CURRENT_VERSION", "0"); // this chages based on rollbacks
            newConfig.set("LATEST_VERSION", "0"); // this follows linear history (dosent change with rollbacks)
            // makeParentFolders(newConfigPath);
            fileServce.makeFileWithParents(newConfigPath);
            resourceLoader.saveConfig(newConfig, newConfigPath);

            fileServce.makeFileWithParents(pathService.getVersionsPath());

            Files.createDirectories(Paths.get(pathService.getVersionFilesPath()));

            // return true;
            // do this in the controller
        //    ObservableList<String> projectList = stateService.getProjectList();
        //     //stateService.addProject(projectName); 
            
        //     projectList.add(projectName);  // this in turn should update the
            // listview in the main conrtroller using Observable lists

        
    }

    // this method is meant to create the parent folders given a filepath (like)
    // should probably be in the FileService
   
    @Override
    public Version newVersion(String comments) { // does this throw
        

        // try {
            Project currentProject = stateService.getCurrentProject();
            Config projectConfig = currentProject.getConfig();
            // ObservableList<Version> projectVersions = currentProject.getVersions();

            int previousVersionNumber = Integer.parseInt(projectConfig.get("LATEST_VERSION"));
            int newVersionNumber = previousVersionNumber + 1;

            String documentPath = projectConfig.get("DOCUMENT_PATH");
            // String currentVersionNumber = projectConfig.get("CURRENT_VERSION");
            // String fileHash = asByteSource(new File(documentPath)).hash(Hashing.sha256()).toString(); // hash file

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String stringDate = formatter.format(date);


            String compressedFilename = UUID.randomUUID().toString();
            
            // System.out.println(currentVersionNumber);

            // System.out.println(getCurrentVersion(projectVersions, currentVersionNumber));
            // get current version
            // Version currentVersion = getCurrentVersion(projectVersions,
            // currentVersionNumber);

            // // // Dosen't let the user create a new version of the file has the same
            // content (what the hashes are made from)
            // if (currentVersion.getFileHash().equals(fileHash)) {
            // return; // show error dialog
            // }

            String targetPath = Paths.get(pathService.getVersionFilesPath(), compressedFilename).toString();
            // makeParentFolders(targetPath);

            fileServce.compressFile(documentPath, targetPath);

            

            String newVersionNumberString = Integer.toString(newVersionNumber);

            Version newVersion = new Version(newVersionNumberString, compressedFilename, stringDate, comments);

            projectConfig.set("LATEST_VERSION", newVersionNumberString);
            projectConfig.set("CURRENT_VERSION", newVersionNumberString);

            resourceLoader.saveConfig(projectConfig, pathService.getConfigPath());
            resourceLoader.saveVersion(newVersion, pathService.getVersionsPath());

            // controller
            // projectVersions.add(newVersion);

            return newVersion;

        // } catch (Exception e) {
        //     e.printStackTrace();
        //     // TODO: handle exception
        //     errorService.showErrorDialog("There was an error in creating a new version");

        //     return null;
        // }

    }
    // private void makeFile(String filename) {
    // File f = new File(filename);
    // if (f.getParentFile() != null) {
    // f.getParentFile().mkdirs();
    // }
    // try {
    // f.createNewFile();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }

    @Override
    public void rollbackVersion(Version version) { // file must be closed
        Project currentProject = stateService.getCurrentProject();
        Config projectConfig = currentProject.getConfig();
        // ObservableList<Version> projectVersions = currentProject.getVersions();
        String fileName = version.getFileName();

        String versionFilePath = Paths.get(pathService.getVersionFilesPath(), fileName).toString();

        fileServce.decompressFile(versionFilePath, projectConfig.get("DOCUMENT_PATH"));

        projectConfig.set("CURRENT_VERSION", version.getVersionNumber());

        resourceLoader.saveConfig(projectConfig, pathService.getConfigPath());
    }

    @Override
    public void peekVersion(Version version) {

        Project currentProject = stateService.getCurrentProject();
        Config projectConfig = currentProject.getConfig();
        String documentPath = projectConfig.get("DOCUMENT_PATH");
        String compressedFileName = version.getFileName();

        String parentDirname = Paths.get(documentPath).getParent().toString();

        // rename
        Path fileName = Paths.get(documentPath).getFileName();
        String documentFileName = pathService.basename(fileName.toString());

        String versionNumber = version.getVersionNumber();

        String peekedFilePath = Paths.get(parentDirname, documentFileName + " v" + versionNumber + ".docx").toString();

        String versionFilePath = Paths.get(pathService.getVersionFilesPath(), compressedFileName).toString();
        fileServce.decompressFile(versionFilePath, peekedFilePath);

    }
}
