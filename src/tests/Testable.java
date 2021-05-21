/**
 * This abstract class serves as parent class for all the onther classes. 
 * It defines a tempoary folder that is deleted after every test is run and before a test is run,
 * the dependency injection container is restet and bootstrapped again.
 */
package tests;
// nesseccary imports
import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import di.Container;
import di.ContainerModule;

public abstract class Testable {
    /** tempoary filder that is deleted after every test */
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * This method is called before every test is run.
     * It resets the container and bootstraps it again
     */
    @Before
    public void configureContainer() {
        File root =  tempFolder.getRoot();
        // resets the container and bootstraps it anew
        Container.resetDependencies();
        ContainerModule.bootstrap(root);
    }    
}
