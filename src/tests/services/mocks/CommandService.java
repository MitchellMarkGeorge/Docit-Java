package tests.services.mocks;

import java.io.IOException;

import javax.print.DocFlavor.STRING;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Version;
import models.Config;
import services.interfaces.ICommandService;
import services.interfaces.IFileService;
import services.interfaces.IResourceLoader;
import services.interfaces.IStateService;

public class CommandService implements ICommandService {

    IStateService stateService;
    IResourceLoader resourceLoader;
    IFileService fileService;


    @Override
    public ObservableList<String> getProjects() throws IOException {
        ObservableList<String> projects = FXCollections.observableArrayList();
        
        projects.addAll("Chemistry Lab", "Short Story", "English Paper", "Prefect Application", "Notes");

        return projects;

        
    }

    @Override
    public void initProject(String documentPath, String alias) throws IOException {
        // TODO Auto-generated method stub

        

        Config newConfig = new Config();
        // String testDocumentPath
        newConfig.set("DOCUMENT_PATH", documentPath);
        newConfig.set("CURRENT_VERSION", "0");
        newConfig.set("LATEST_VERSION", "0");


        // resourceLoader.saveConfig(config, configPath);

        stateService.addProject(alias); // technicall

    }

    @Override
    public Version newVersion(String comments) {
        // TODO Auto-generated method stub

        

        Version newVersion = new Version()
        return null;
    }

    @Override
    public void peekVersion(Version version) {
        // TODO Auto-generated method stub

    }

    @Override
    public void rollbackVersion(Version version) {
        // TODO Auto-generated method stub
        
    }


}
