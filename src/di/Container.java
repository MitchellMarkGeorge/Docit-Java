package di;


import java.util.HashMap;
import java.util.Map;


public class Container {


    /** OLD WAY
     * The way it works is that the dependecies are added to the container. 
     * This is done by binding the interface/abstract class to a class that implements it.
     * This is done so it easy to swap put implementations for things like testing (Inversion of Control).
     * When the dependency is resolved (using the interface/ abstract class), 
     * the Container checks if the class implementation has a singleton instance. If it does not, then it is created and stored. Else, it is simply returned
     */

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
    // ex: IFileService and FileService
    private static Map<Class<?>, Class<?>> typeMap = new HashMap<>();
    // maps all class implementations to their instances
    // ex: FileService and new FileService
    private static Map<Class<?>, Object> singletonMap = new HashMap<>();

    // public static <T, U extends T> void bindDependency(Class<T> interfaceType, Class<U> implementation) {
    //     // Container.class
    //     if (interfaceType == null || implementation == null) {
    //         throw new Error("Cannot have null arguments");
    //     }

    //     typeMap.put(interfaceType, implementation);
    //     singletonMap.put(implementation, null);
    // }

    public static <T, U> void bindDependency(Class<T> interfaceType, T classInstance) {
        if (interfaceType == null || classInstance == null) {
            return;
        }

        if (typeMap.containsKey(interfaceType)) {
            throw new Error("Type " + interfaceType.getSimpleName() + " is already in the container");
        }

        Class<?> implementation = classInstance.getClass(); 
        typeMap.put(interfaceType, implementation);
        singletonMap.put(implementation, classInstance);
    }

    public static <T> T resolveDependency(Class<T> interfaceType) {
        if (!typeMap.containsKey(interfaceType)) {
            throw new Error("No dependency with type " + interfaceType.getSimpleName() + " exists.");
            // return null;
        }

        Class<?> classImplementation = typeMap.get(interfaceType);

        // cant happen
        // if (!singletonMap.containsKey(classImplementation)) {
        //     // throw new Error("No singleton that instantiates class " + classImplementation.getSimpleName() + " exists");
        //     return null;
        // }

        Object classInstance = singletonMap.get(classImplementation);
        
       return interfaceType.cast(classInstance); // LOOK INTO THIS

        // return classInstance; // cast to T type

        // refactor this
        // if (classInstance == null) {
        //     Object newInstance = instanctiateClass(classImplementation);
        //     singletonMap.put(classImplementation, newInstance);
        //     return  newInstance;
        // } else {
        //     return classInstance;
        // }
    }

    public static void resetDependencies() {
        typeMap.clear();
        singletonMap.clear();
    }

    // private static <T> Object instanctiateClass(Class<T> classImplementation) {

    //     try {
    //         Constructor<T> constructor = classImplementation.getConstructor();

    //         return constructor.newInstance();
    //     } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
    //             | IllegalArgumentException | InvocationTargetException e) {
    //         throw new Error("Error occured trying to create class instace", e);
    //     }

    // }

    // public static void main(String[] args) throws Exception {
    //     System.out.println(HashService.class.getConstructor());

    //     Container.bindDependency(IFileService.class, FileService.class);

        

    // }
}
