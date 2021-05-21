/**
 * 
 * This service is responsible for all of the actual “commands” that the Docit program would run (getting projects, creating projects, rollback, peek, new version).
 * This is inspired by the inital CLI application 
 * Each command is implemented as a separate method (as defined in the ICommandService interface).
 * This service implements the ICommandService interface and all of its methods.
 */

package services;


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

            return projects; // return the list

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
  

    /**
     * This inherited method is responsible for rolling back to the currently selected version
     * (change the state of the document to the state of the given version).
     * This method gets the curretlu selected version, restores the state of the document at that version,
     *  and changes the current version pointer to point at that version
     *  precondition: there is a currently selected version and that version is not the current version of the project
     *  postcondition: a rollback is done correctly and the accosiated files are updated
     */
    @Override
    public void rollbackVersion() throws Exception { 

        // gets the selected version from the state service
        Version version = (Version) stateService.get("currentVersion");
        // gets the currently selected project from the state service
        Project currentProject = (Project) stateService.get("currentProject");
        // get the config from the project object
        Config projectConfig = currentProject.getConfig();
        //get the current version number
        String currentVersionNumber =  projectConfig.get("CURRENT_VERSION");
        // if the version number from the version object and the current version number are the same,
        // then throw an excetion
        // this is because you cannot rollback to the current version -> nothing will change
        // this is handled by the UI by disabling the butttons
        if (currentVersionNumber.equals(version.getVersionNumber())) {
            throw new Exception("Can't rollback to current version");
        }
        // get the project name fom the stateService
        String projectName = (String) stateService.get("projectName");
        // get the name of the version file of the version that will be rolled back to
        String fileName = version.getFileName();
        // get the path of the version file
        String versionFilePath = Paths.get(pathService.getVersionFilesPath(projectName), fileName).toString();
        // decompress the version file and witre it to the document path
        // this essentionly resportes the state of the document at that given version
        // the "rollback" has officially happened
        fileServce.decompressFile(versionFilePath, projectConfig.get("DOCUMENT_PATH")); // meant to overwrite the current document
        // sets the current version pointer to the version that was just rolled back to
        projectConfig.set("CURRENT_VERSION", version.getVersionNumber());
        // save the updated config
        resourceLoader.saveConfig(projectConfig, pathService.getConfigPath(projectName));
    }

    /**
     * This inherited methood is resposible for peeking the currently selected version 
     * (create a new file with the state of a given version)
     * 
     * precondition: there is a currently selected version and that version is not the current version of the project
     *  postcondition: a "peek" is done correctly and the accosiated files are updated (just the documnent path)
     */
    @Override
    public void peekVersion() throws Exception {

        // gets the selected version from the stateService
        Version version = (Version) stateService.get("currentVersion");

   

        // gets the currently selected project from the state service
        Project currentProject = (Project) stateService.get("currentProject");
        // get the config from the project object
        Config projectConfig = currentProject.getConfig();
        // get the current version number from the config
        String currentVersionNumber =  projectConfig.get("CURRENT_VERSION");
          // if the version number from the version object and the current version number are the same,
        // then throw an excetion
        // this is because you cannot peek to the current version -> you would be creating a clone of the document
        // this is handled by the UI by disabling the butttons
        if (currentVersionNumber.equals(version.getVersionNumber())) {
            throw new Exception("Can't rollback to current version");
        }

        // get the documentpath from the config
        String documentPath = projectConfig.get("DOCUMENT_PATH");
        // get the name of he version file of that version
        String compressedFileName = version.getFileName();
        // get the project name from the stateService
        String projectName = (String) stateService.get("projectName");
        // get the parent directory of the document (the peeked file should be written in the same directodyu as the document)
        String parentDirname = Paths.get(documentPath).getParent().toString();

        // get the filename of rhe documentt (with extension)
        Path fileName = Paths.get(documentPath).getFileName();
        // remove extensions (and any facy dots) from the document file name
        String documentFileName = pathService.basename(fileName.toString());

        String versionNumber = version.getVersionNumber();
        // creates the path that the peeked path will be written to
        String peekedFilePath = Paths.get(parentDirname, documentFileName + " v" + versionNumber + ".docx").toString();
        // gvets the path of the accosited version file of the given version
        String versionFilePath = Paths.get(pathService.getVersionFilesPath(projectName), compressedFileName).toString();
        // decomreesseds the version file into the peek  file path
        fileServce.decompressFile(versionFilePath, peekedFilePath);

    }
}
