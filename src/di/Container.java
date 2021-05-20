/**
 * This class/ container is responsible for managing dependencies for the dependency injection framework. 
 * It has methods to bind dependencies to their types, resolve dependencies from their types, and reset the entire container if needed.
 * 
 * @author Mitchell Mark-George
 */

package di;
// Neccessary Imports
import java.util.HashMap;
import java.util.Map;


public class Container {


    

     /**
     * The way it works is that the dependecies are added to the container. 
     * This is done by binding the interface/abstract class to a instace of a class that implments it.
     * In binding, the class of the class instace is mapped with the interface/abstract class and the 
     * class is mapped to instance
     * This is done so it easy to swap put implementations for things like testing (Inversion of Control).
     * When the dependency is resolved (using the interface/ abstract class), 
     * the Container checks if gets the class intance of the implementing class and returns it
     */
    

    // Maps all interfaces to their class implementation/ services
    // eg: IFileService and FileService
    private static Map<Class<?>, Class<?>> typeMap = new HashMap<>();

    // maps all class implementations to their instances
    // eg: FileService and new FileService
    private static Map<Class<?>, Object> singletonMap = new HashMap<>();

    
    /**
     * This method is responsible for binging a class instance to a type in the container. It
     * \
     * @param interfaceType the type of the class intance
     * @param classInstance the instance to be binded to the interfact type
     * 
     * precondition: interFaceType and class intace cannot be null. classInstace should be of type interfacetype.
     * The interfaceType can not be alsready bound to another class instace
     * postcondition: the intercae should be bound to the class insace and trhe class instace be stored in the container
     */
    public static <T> void bindDependency(Class<T> interfaceType, T classInstance) {
        // neither interfaceType or classInstace should be null
        if (interfaceType == null || classInstance == null) {
            throw new Error("No parameters can not be null");
        }

        // if the interface has already been stored in the typemap (meaning there is alsready a class instace bound to it in the continer)
        // throw an error
        if (typeMap.containsKey(interfaceType)) {
            throw new Error("Type " + interfaceType.getSimpleName() + " is already in the container");
        }

        // gets the actual class of the class instace
        Class<?> implementation = classInstance.getClass(); 
        // maps the interfaceType with the class of the class instace (basically maping/linking the two types togetther)
        // this is basically establishing the relationship that this interfacetype will ALWAYS be linked to this implmenting class
        typeMap.put(interfaceType, implementation);
        // maps the class of the class instace to the instace
        // establishing the relashonship that this class object is ALWAYS linked to this paticular instace of the class
        singletonMap.put(implementation, classInstance);

        // its basically a three-way chain split into two maps
    }

    /**
     * This method is responsible to retuning a class instace given the interfaceType it is bound to.
     * @param interfaceType the interface/ type that a dependency in the container is re
     * @return the requested class instacne
     * 
     * precondition: the interfaceType cannot be null and should be bound to an existing instace in the container
     * postcondition: returns a class instace that is of type interfaceType 
     */
    public static <T> T resolveDependency(Class<T> interfaceType) {
        // interfaceType cant be null
        if (interfaceType == null) {
            throw new Error("interfaceType cannot be null");
        }

        // iof the the given interfaceType is not in the container, throw an error
        if (!typeMap.containsKey(interfaceType)) {
            throw new Error("No dependency with type " + interfaceType.getSimpleName() + " exists.");
            // return null;
        }

        // get the class that the interface is bound to
        Class<?> classImplementation = typeMap.get(interfaceType);

        // cant happen
        // if (!singletonMap.containsKey(classImplementation)) {
        //     // throw new Error("No singleton that instantiates class " + classImplementation.getSimpleName() + " exists");
        //     return null;
        // }
            // use the retreived class to get the class instace from the singletonMap
        Object classInstance = singletonMap.get(classImplementation);
        
        // return the clas instace cast to the interface type (better type security and developement experience)
       return interfaceType.cast(classInstance); 

    }

    /**
     * This method is responsible for emptying the container (removing/ reseting all of the dependencies)
     * precondition: the contaier should have some bound dependencies
     * postcondition: the contaier should be empty (no bound interfaceTypes or class intances)
     */
    public static void resetDependencies() {
        // clears the typeMap and singletonMap of any bindings
        typeMap.clear();
        singletonMap.clear();
    }

   
}
