package services;

import java.io.File;
import java.io.IOException;
import models.Version;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import di.Container;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Config;
import models.Project;
import services.interfaces.ICommandService;
import services.interfaces.IErrorService;
import services.interfaces.IFileService;
// import services.interfaces.IHashService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;

import static com.google.common.io.Files.asByteSource;

import com.google.common.hash.Hashing;

public class CommandService implements ICommandService {

    IStateService stateService = (StateService) Container.resolveDependency(IStateService.class);
    IPathService pathService = (IPathService) Container.resolveDependency(IPathService.class);
    IResourceLoader resourceLoader = (IResourceLoader) Container.resolveDependency(IResourceLoader.class);
    IFileService fileServce = (IFileService) Container.resolveDependency(IFileService.class);
    IErrorService errorService = (IErrorService) Container.resolveDependency(IErrorService.class);
    // public static void main(String[] args) {
    // System.out.println(Paths.get("C://meme/me.png").getFileName().toString());
    // }

    @Override
    public ObservableList<String> getProjects() {

        try {
            ObservableList<String> projects = FXCollections.observableArrayList();
            // return null;
            Path docitPath = Path.of(pathService.getDocitPath());
            // could have some files
            Files.list(docitPath).map(path -> path.toFile()).filter(file -> file.isDirectory())
                    .forEachOrdered(file -> projects.add(file.getName()));

            return projects; // set to state
        } catch (IOException e) {
            e.printStackTrace();
            errorService.showErrorDialog("Error in geting projects");
            throw new Error("Error in reading the file");
        } // should only be

    }

    @Override
    public void initProject(String documentPath, String projectName) {
        // TODO Auto-generated method stub

        Path newProjectPath = Paths.get(pathService.getDocitPath(), projectName);
        System.out.println(newProjectPath);
        if (Files.exists(newProjectPath)) {
            // throw new Error("Project " + projectName + " already exists");
            errorService.showErrorDialog("Project " + projectName + " already exists");
            return;
        }

        Config newConfig = new Config();
        String newConfigPath = Paths.get(newProjectPath.toString(), "config").toString();
        System.out.println(newConfigPath);
        newConfig.set("DOCUMENT_PATH", documentPath);
        newConfig.set("CURRENT_VERSION", "0"); // this chages based on rollbacks
        newConfig.set("LATEST_VERSION", "0"); // this follows linear history (dosent change with rollbacks)
        makeParentFolders(newConfigPath);
        resourceLoader.saveConfig(newConfig, newConfigPath);

        stateService.addProject(projectName); // this in turn should update the
        // listview in the main conrtroller using Observable lists

    }

    // this method is meant to create the parent folders given a filepath (like)
    // should probably be in the FileService
    private void makeParentFolders(String filePath) {
        File file = new File(filePath);

        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
    }

    private Version getCurrentVersion(ObservableList<Version> versions, String versionNumber) {
        for (Version version : versions) {
            if (version.getVersionNumber().equals(versionNumber)) {
                return version;
            }
        }

        return null;
    }

    @Override
    public void newVersion(String comments) {
        // TODO Auto-generated method stub

        try {
            Project currentProject = stateService.getCurrentProject();
            Config projectConfig = currentProject.getConfig();
            ObservableList<Version> projectVersions = currentProject.getVersions();

            int previousVersionNumber = Integer.parseInt(projectConfig.get("LATEST_VERSION"));
            int newVersionNumber = previousVersionNumber + 1;

            String documentPath = projectConfig.get("DOCUMENT_PATH");
            String currentVersionNumber = projectConfig.get("CURRENT_VERSION");
            String fileHash = asByteSource(new File(documentPath)).hash(Hashing.sha256()).toString();

            System.out.println(currentVersionNumber);

            // System.out.println(getCurrentVersion(projectVersions, currentVersionNumber));
            // get current version
            // Version currentVersion = getCurrentVersion(projectVersions,
            // currentVersionNumber);

            // // // Dosen't let the user create a new version of the file has the same
            // content (what the hashes are made from)
            // if (currentVersion.getFileHash().equals(fileHash)) {
            // return; // show error dialog
            // }

            String targetPath = Paths.get(pathService.getVersionFilesPath(), fileHash).toString();
            makeParentFolders(targetPath);

            fileServce.compressFile(documentPath, targetPath);

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String stringDate = formatter.format(date);

            String newVersionNumberString = Integer.toString(newVersionNumber);

            Version newVersion = new Version(newVersionNumberString, fileHash, stringDate, comments);

            projectConfig.set("LATEST_VERSION", newVersionNumberString);
            projectConfig.set("CURRENT_VERSION", newVersionNumberString);

            resourceLoader.saveConfig(projectConfig, pathService.getConfigPath());
            resourceLoader.saveVersion(newVersion, pathService.getVersionsPath());

            projectVersions.add(newVersion);

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }

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
    public void rollbackVersion(Version version) {
        Project currentProject = stateService.getCurrentProject();
        Config projectConfig = currentProject.getConfig();
        // ObservableList<Version> projectVersions = currentProject.getVersions();
        String fileHash = version.getFileHash();

        String versionFilePath = Paths.get(pathService.getVersionFilesPath(), fileHash).toString();

        fileServce.decompressFile(versionFilePath, projectConfig.get("DOCUMENT_PATH"));

        projectConfig.set("CURRENT_VERSION", version.getVersionNumber());

        resourceLoader.saveConfig(projectConfig, pathService.getConfigPath());
    }

    @Override
    public void peekVersion(Version version) {

        Project currentProject = stateService.getCurrentProject();
        Config projectConfig = currentProject.getConfig();
        String documentPath = projectConfig.get("DOCUMENT_PATH");
        String fileHash = version.getFileHash();

        String parentDirname = Paths.get(documentPath).getParent().toString();

        // rename
        Path fileName = Paths.get(documentPath).getFileName();
        String documentFileName = pathService.basename(fileName.toString());

        String versionNumber = version.getVersionNumber();

        String peekedFilePath = Paths.get(parentDirname, documentFileName + " v" + versionNumber + ".docx").toString();

        String versionFilePath = Paths.get(pathService.getVersionFilesPath(), fileHash).toString();
        fileServce.decompressFile(versionFilePath, peekedFilePath);

    }
}
