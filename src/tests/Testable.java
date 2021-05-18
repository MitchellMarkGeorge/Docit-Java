package tests;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import di.Container;
import di.ContainerModule;

public abstract class Testable {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void configureContainer() {
        File root = tempFolder.getRoot();

        Container.resetDependencies();
        ContainerModule.bootstrap(root);
    }    
}
