/**
 * 
 * This service is responsible for all of the actual “commands” that the Docit program would run (getting projects, creating projects, rollback, peek, new version).
 * This is inspired by the inital CLI application 
 * Each command is implemented as a separate method (as defined in the ICommandService interface).
 * This service implements the ICommandService and all of its methods.
 */

package services;

import java.io.File;
// Nesseccary imports
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

    // Injecting the needed services from the dependency injection container
    IStateService stateService = Container.resolveDependency(IStateService.class);
    IPathService pathService = Container.resolveDependency(IPathService.class);
    IResourceLoader resourceLoader = Container.resolveDependency(IResourceLoader.class);
    IFileService fileServce = Container.resolveDependency(IFileService.class);
    IErrorService errorService = Container.resolveDependency(IErrorService.class);
  
    
    /**
     * This method is responisble for getting a list of project names. 
     * It reads the names of the subdirectories of the docit path (if any) and returns a list
     * @return list of project names
     * precondition: the application is running
     * postcondition: a list of project names is returned (can be empty if there are no projects)
     */
    @Override
    public ObservableList<String> getProjects() throws IOException {
        
        // creates an empty list
            ObservableList<String> projects = FXCollections.observableArrayList();
            // gets the docit path from the pathService
            String docitPathString = pathService.getDocitPath();
            Path docitPath = Path.of(docitPathString);
            // if the docit path exists (meaning that a project has been created),
            // get the all the children, filter for only the directories (docit projects),
            // and add their names to the list

            // even if the docit folder if empty (for some reason), the ccode will not fail
            // and the list will still be empty
            if (Files.exists(docitPath)) {
                // could have some files
                Files.list(docitPath)
                .map(path -> path.toFile())
                .filter(file -> file.isDirectory())
                .forEachOrdered(file -> projects.add(file.getName()));
            }

            return projects; // return the lisr

    }

    /**
     * This inherited method is responsible for creating a project based on the provided document path and projectName.
     * All the neccessary project files (like the config file, versions file, and version_files folder) are created
     * (the docit path will also be created as well)
     * @param documentPath the path of the projects associated document
     * @param projectName the name of new project
     *      
     * precondition: a project with the same projectName cannot exist, the documentPath must exist, and they cannot be null (wont be)
     * postcondition: a project is created (with all accocciated files)
     */
    @Override
    public void initProject(String documentPath, String projectName) throws Exception {
        // since the document path is from a file chooder, there is no way the document does not exist
        // Because of UI elements (file chooser and textfield), there is now way that the documentPath or projectName can be null 
        
            // This is very unlikely to happen, bit it is still worth handleing
            // The user might select a document and then delete the document (unlikely) before creating the project
            // or the document they selected is on a storage device and they eject it before creating the project
            
            // if the docuiment path does not exist, throw an exception
            if (!Files.exists(Paths.get(documentPath))) {
                throw new Exception("Document does not exist");
            }

            
            
            // gets the new project path from the pathService
            Path newProjectPath = Paths.get(pathService.getProjectPath(projectName));

            // if the new project path already exists (meaning there is already a project with that name)
            // throw an exception
            if (Files.exists(newProjectPath)) {
                throw new Exception("Project already exists");
            }

            // create a new config object for the project and 
            Config newConfig = new Config();
            // get the config path for the config from the pathService
            String newConfigPath = pathService.getConfigPath(projectName);
            // update the config with the neccessary information
            newConfig.set("DOCUMENT_PATH", documentPath); // path to the document
            newConfig.set("CURRENT_VERSION", "0"); // this chages based on rollbacks - 0 means no version (basically null)
            newConfig.set("LATEST_VERSION", "0"); // this follows linear history (dosent change with rollbacks) - 0 means no version (basically null)
            // makes an empty config file for the new project (creating the .docit folder if it dosent exist)
            fileServce.makeFileWithParents(newConfigPath);
            // saves the new config to the new config file
            resourceLoader.saveConfig(newConfig, newConfigPath);
            // creates an emopty versions file for the new project
            fileServce.makeFileWithParents(pathService.getVersionsPath(projectName));
            // cretes the version_files folder for the project
            Files.createDirectories(Paths.get(pathService.getVersionFilesPath(projectName)));

                    
    }

   /**
     * This inhertited method is responisble for creating a new version of the currently selected project.
     * This method stores a compressed version of the document at its current state in the version_files folder,
     * updates the project config, and creates a new version object of the new version
     * 
     * @param comments comments for the new version
     * @return the new version object
     * 
     * preconditon: there must be a currently selected project
     * postconditon: a new version is crfeated and saved to the file system. A Version object reflecting
     * the change is returned to update the UI in the controller.
     */
    @Override
    public Version newVersion(String comments) throws Exception { 
        
        // never going to happen because of the UI, but still worth handeling
        // if the comments is null, set it as the default
        if (comments == null) {
            comments = "No Comments".replaceAll(" ", "_"); // so it can be stored properly
        }
        // load the currently selected project from the state stateService
            Project currentProject = (Project) stateService.get("currentProject"); // can be null
            // get the project config from the the project object
            Config projectConfig = currentProject.getConfig();
            // get trhe current project name from the stateService
            String projectName = (String) stateService.get("projectName");
            
            // gets the latest version (to become the previous) number fromt the config and makes it a int
            int previousVersionNumber = Integer.parseInt(projectConfig.get("LATEST_VERSION"));
            // creates a new version number from the previos latest version
            int newVersionNumber = previousVersionNumber + 1;
        // get the document path from the config
            String documentPath = projectConfig.get("DOCUMENT_PATH");
           
            // gets the currrent date and formats it (date version was created)
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String stringDate = formatter.format(date);

            // generates a UUID to be the filename of trhe compressed version file
            // this makes it so every created version will never have the same version file name
            // if u get confused -> version file refers to the saved compressed document state at the time a version is made
            // diferent from "versions" file
            String compressedFilename = UUID.randomUUID().toString();
            
            // gets the target path that the new compressed document will be stored
            String targetPath = Paths.get(pathService.getVersionFilesPath(projectName), compressedFilename).toString();
           
            // compressed the document and stores it in the target poath
            fileServce.compressFile(documentPath, targetPath);

            
            // get the new version number as a string
            String newVersionNumberString = Integer.toString(newVersionNumber);

            //creates a new version object from the provided information
            Version newVersion = new Version(newVersionNumberString, compressedFilename, stringDate, comments);

            // updates the latest version and current version pointers in the config
            // with would even update the current version pointer even if it is pointing to a previous version
            // when a new version is created, rhe current version pointer ALWAYS points to the latest version
            projectConfig.set("LATEST_VERSION", newVersionNumberString);
            projectConfig.set("CURRENT_VERSION", newVersionNumberString);
            // saves the updated config to the projects config file
            resourceLoader.saveConfig(projectConfig, pathService.getConfigPath(projectName));
            // saves the new version to the projects versions file
            resourceLoader.saveVersion(newVersion, pathService.getVersionsPath(projectName));

            
            // returns  the new Version object
            return newVersion;
    }
  

    @Override
    public void rollbackVersion() throws Exception { // file must be closed
        // Project currentProject = stateService.getCurrentProject();

        //TODO: Throw custom exception here (No version selected), probably never going to happen
        Version version = (Version) stateService.get("currentVersion");



        Project currentProject = (Project) stateService.get("currentProject");
        Config projectConfig = currentProject.getConfig();

        String currentVersionNumber =  projectConfig.get("CURRENT_VERSION");

        if (currentVersionNumber.equals(version.getVersionNumber())) {
            throw new Exception("Can't rollback to current version");
        }





        // String projectName = stateService.getProjectName();
        String projectName = (String) stateService.get("projectName");
        // ObservableList<Version> projectVersions = currentProject.getVersions();
        String fileName = version.getFileName();

        String versionFilePath = Paths.get(pathService.getVersionFilesPath(projectName), fileName).toString();

        fileServce.decompressFile(versionFilePath, projectConfig.get("DOCUMENT_PATH")); // meant to overwrite the current document

        projectConfig.set("CURRENT_VERSION", version.getVersionNumber());

        resourceLoader.saveConfig(projectConfig, pathService.getConfigPath(projectName));
    }

    @Override
    public void peekVersion() throws Exception {


        Version version = (Version) stateService.get("currentVersion");

        //TODO: Throw custom exception here (No version selected), probably never going to happen
        // Use use exception to show error meesgaes in errorService

        // Project currentProject = stateService.getCurrentProject();
        Project currentProject = (Project) stateService.get("currentProject");
        Config projectConfig = currentProject.getConfig();

        String currentVersionNumber =  projectConfig.get("CURRENT_VERSION");

        if (currentVersionNumber.equals(version.getVersionNumber())) {
            throw new Exception("Can't rollback to current version");
        }



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
