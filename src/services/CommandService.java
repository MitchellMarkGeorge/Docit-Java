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

    IStateService stateService = Container.resolveDependency(IStateService.class);
    IPathService pathService = Container.resolveDependency(IPathService.class);
    IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
    IFileService fileServce = Container.resolveDependency(IFileService.class);
    IErrorService errorService = Container.resolveDependency(IErrorService.class);
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
    public void initProject(String documentPath, String projectName) throws Exception {
        // since the document path is from a file chooder, there is no way the document does not exist

        // Because of UI elements (file chooser and textfield), there is now way that the documentPath or projectName is 
        // try {
            // This is very unlikely to happen, bit it is still worth handleing
            // The user might select a document and then delete the document (unlikely) before creating the project
            if (!Files.exists(Paths.get(documentPath))) {
                throw new Exception("Document does not exist");
            }

            /**
             * Here, i should make: the config file empty versions file empty version_files
             * folder
             * 
             */
            

            Path newProjectPath = Paths.get(pathService.getProjectPath(projectName));

            // String newProjectPath = pathService.getProjectPath();

            // System.out.println(newProjectPath);
            if (Files.exists(newProjectPath)) {
                // throw new Error("Project " + projectName + " already exists");
                // errorService.showErrorDialog("Project " + projectName + " already exists");
                // return;

                throw new Exception("Project already exists");
            }

            Config newConfig = new Config();
            // String newConfigPath = Paths.get(newProjectPath.toString(),
            // "config").toString();
            String newConfigPath = pathService.getConfigPath(projectName);
            // System.out.println(newConfigPath);
            newConfig.set("DOCUMENT_PATH", documentPath);
            newConfig.set("CURRENT_VERSION", "0"); // this chages based on rollbacks
            newConfig.set("LATEST_VERSION", "0"); // this follows linear history (dosent change with rollbacks)
            // makeParentFolders(newConfigPath);
            fileServce.makeFileWithParents(newConfigPath);
            resourceLoader.saveConfig(newConfig, newConfigPath);

            fileServce.makeFileWithParents(pathService.getVersionsPath(projectName));

            Files.createDirectories(Paths.get(pathService.getVersionFilesPath(projectName)));

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
    public Version newVersion(String comments) throws Exception { // does this throw
        
        // never going to happen because of the UI, but still worth handeling
        if (comments == null) {
            comments = "No Comments".replaceAll(" ", "_"); // so it can be stored properly
        }
        // try {
            // Project currentProject = stateService.getCurrentProject();
            Project currentProject = (Project) stateService.get("currentProject"); // can be null
            // this will throw an exception if null
            // if (currentProject == null) { // might not be need
            //     throw new Exception("No current project in state.");
            // }

            Config projectConfig = currentProject.getConfig();
            // String projectName = stateService.getProjectName();
            String projectName = (String) stateService.get("projectName");
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
            
            
            String targetPath = Paths.get(pathService.getVersionFilesPath(projectName), compressedFilename).toString();
           

            fileServce.compressFile(documentPath, targetPath);

            

            String newVersionNumberString = Integer.toString(newVersionNumber);

            Version newVersion = new Version(newVersionNumberString, compressedFilename, stringDate, comments);

            projectConfig.set("LATEST_VERSION", newVersionNumberString);
            projectConfig.set("CURRENT_VERSION", newVersionNumberString);

            resourceLoader.saveConfig(projectConfig, pathService.getConfigPath(projectName));
            resourceLoader.saveVersion(newVersion, pathService.getVersionsPath(projectName));

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
    public void rollbackVersion(Version version) throws Exception { // file must be closed
        // Project currentProject = stateService.getCurrentProject();
        Project currentProject = (Project) stateService.get("currentProject");
        Config projectConfig = currentProject.getConfig();

        String currentVersion =  projectConfig.get("CURRENT_VERSION");

        if (currentVersion.equals(version.getVersionNumber())) {
            throw new Exception()
        }





        // String projectName = stateService.getProjectName();
        String projectName = (String) stateService.get("projectName");
        // ObservableList<Version> projectVersions = currentProject.getVersions();
        String fileName = version.getFileName();

        String versionFilePath = Paths.get(pathService.getVersionFilesPath(projectName), fileName).toString();

        fileServce.decompressFile(versionFilePath, projectConfig.get("DOCUMENT_PATH"));

        projectConfig.set("CURRENT_VERSION", version.getVersionNumber());

        resourceLoader.saveConfig(projectConfig, pathService.getConfigPath(projectName));
    }

    @Override
    public void peekVersion(Version version) throws Exception {

        // Project currentProject = stateService.getCurrentProject();
        Project currentProject = (Project) stateService.get("currentProject");
        Config projectConfig = currentProject.getConfig();
        String documentPath = projectConfig.get("DOCUMENT_PATH");
        String compressedFileName = version.getFileName();
        // String projectName = stateService.getProjectName();
        String projectName = (String) stateService.get("projectName");
        String parentDirname = Paths.get(documentPath).getParent().toString();

        // rename
        Path fileName = Paths.get(documentPath).getFileName();
        String documentFileName = pathService.basename(fileName.toString());

        String versionNumber = version.getVersionNumber();

        String peekedFilePath = Paths.get(parentDirname, documentFileName + " v" + versionNumber + ".docx").toString();

        String versionFilePath = Paths.get(pathService.getVersionFilesPath(projectName), compressedFileName).toString();
        fileServce.decompressFile(versionFilePath, peekedFilePath);

    }
}
