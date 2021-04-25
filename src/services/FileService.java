package services;

import java.io.IOException;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.stream.Stream;
// import java.io.FileInputStream;

import services.interfaces.IFileService;


public class FileService implements IFileService {

    // private PathService pathService = new PathService(projectName)

    // writing/ reading binary data should be handles differenetly

    // https://mkyong.com/java/java-how-to-read-a-file/
    // https://www.baeldung.com/java-try-with-resources
    // https://www.javacodegeeks.com/2015/02/java-8-pitfall-beware-files-lines.html

    /**
     * Should be used for files like config file and versions files
     * @param path
     * @param callback
     */
    @Override
    public void readFileLines(Path path, Consumer<String> callback) {
        // Files.lines(path).parallel().forEachOrdered(action);
        //Should be fast and not load eveything into memory
        try (Stream<String> stream = Files.lines(path)) { // should i use parrallel?
            stream.forEachOrdered(callback);
            
        } catch (Exception e) {
            //TODO: handle exception
            // GUI Dialog response
        }


        // Alternative
        // public static void main(String[] args) throws IOException {

        //     String fileName = "/home/mkyong/app.log";
    
        //     // defaultCharBufferSize = 8192; or 8k
        //     try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        //         String line;
        //         while ((line = br.readLine()) != null) {
        //             System.out.println(line);
        //         }
        //     }
    
        // }
    }


    /**
     * 
     * @param path
     * @param callback
     */
    @Override
    public void listDirectories(Path path, Consumer<Path> callback) {
        if (!Files.isDirectory(path)) {
            // TODO: This should be handles 
        }
        try (Stream<Path> stream = Files.list(path)) {
            stream.forEachOrdered(callback);
        } catch (Exception e) {
            //TODO: handle exception
        }
    }

    @Override
    public void appendToFile(Path path, String line) {
        // try-resource-idiom
        try {
            Files.write(path, line.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
        } catch (Exception e) {
            //TODO: handle exception
        }
        
    }

    /**
     * This should be used with version files and word documents
     * @param path
     * @return
     */
    @Override
    public byte[] readFiletoBuffer(Path path) { // should it throw?
        // https://docs.oracle.com/javase/9/docs/api/java/nio/file/Files.html#readAllBytes-java.nio.file.Path-
        try  {
            // Max size of file this method can work with 2gb
            // With just text, max word document size is 32mb,
            // but with images and videos, it can increase to about 512mb
            return Files.readAllBytes(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            //TODO: handle exception
        }
    };

    @Override
    public void writeBuffertoFile(Path path, byte[] buffer) { // might rename
        try {
            Files.write(path, buffer, StandardOpenOption.CREATE);
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
            
        }
    }

    

    

    
}
