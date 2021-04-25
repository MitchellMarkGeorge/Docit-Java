package services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import di.Container;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Config;
import services.interfaces.ICommandService;
import services.interfaces.IPathService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;

public class CommandService implements ICommandService {

    IStateService stateService = (StateService) Container.resolveDependency(IStateService.class);
    IPathService pathService = (IPathService) Container.resolveDependency(IPathService.class);
    IResourceLoader resourceLoader = (IResourceLoader) Container.resolveDependency(IResourceLoader.class);

    @Override
    public ObservableList<String> getProjects() {
        

        try {
            ObservableList<String> projects = FXCollections.observableArrayList();
            // return null;
            Path docitPath = Path.of(pathService.getDocitPath());
            // could have some files
            Files.list(docitPath).map(path -> path.toFile()).filter(file -> file.isDirectory())
                    .forEachOrdered(file -> projects.add(file.getName()));

            return projects;
        } catch (IOException e) {
            
            throw new Error("Error in reading the file");
        } // should only be

    }

    @Override
    public void initProject(String documentPath, String projectName) {
        // TODO Auto-generated method stub

        Path newProjectPath = Paths.get(pathService.getDocitPath(), projectName);
        System.out.println(newProjectPath);
        if (Files.exists(newProjectPath)) {
            throw new Error("Project " + projectName + " already exists");
        }

        Config newConfig = new Config();
        String newConfigPath = Paths.get(newProjectPath.toString(), "config").toString();
        System.out.println(newConfigPath);
        newConfig.set("DOCUMENT_PATH", documentPath);
        newConfig.set("CURRENT_VERSION", "0");
        newConfig.set("LATEST_VERSION", "0");
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
}
