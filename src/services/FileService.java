/**
 * This service is responsible for handling everything related to files 
 * (reading files, listing directories, reading files line by line, compressing and decompressing).
 * This service implements the IFileService interface and all of its methods.
 * 
 * @author Mitchell Mark-George
 */

package services;
// Nesseccary imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import services.interfaces.IFileService;

public class FileService implements IFileService {

    

    // https://mkyong.com/java/java-how-to-read-a-file/
    // https://www.baeldung.com/java-try-with-resources
    // https://www.javacodegeeks.com/2015/02/java-8-pitfall-beware-files-lines.html

    /**
     * This inherited method is responsible for reading a file line by line and calling a callback on each read line
     * This is used to read a projects versions file
     * @param path path of the file to be read
     * @param callback function to be executed on each file line
     * 
     * precondition: the file at the path must exist
     * postconstion: teh file should beread line-by-line succsessfully
     */
    @Override
    public void readFileLines(String path, Consumer<String> callback) throws Exception {
        // reads the file in order that the lines are in (instead of them being lazy)
        try (Stream<String> stream = Files.lines(Paths.get(path))) { 
            stream.forEachOrdered(callback);

        } catch (Exception e) {
            throw e;
        }

      
    }

    /**
     * This method is responsible for reading a directory and calling a callback on each found subdirectory
     * this is used to get the names of all the docit projects
     * @param path path of the folder to be read
     * @param callback function to be executed on each file line
     * 
     * precondition: the path is for a folder that exists
     */
    @Override
    public void listDirectories(String path, Consumer<Path> callback) throws Exception {
        // if the given path is not a directory, throw an exception
        if (!Files.isDirectory(Paths.get(path))) {
            throw new Exception("Path must be a directory");
        }
        // read the directory in an orderly fashion
        try (Stream<Path> stream = Files.list(Paths.get(path))) {
            stream.forEachOrdered(callback);
        } catch (Exception e) {
                     
            throw e;
        }
    }

    /**
     * This inherited method is responsible for appending a string line to the end of the file at the given path
     * Used to save a version to a verions file (basically the "timeline")
     * @param path path of the file to append the string
     * @param line the string to append at the end of the file
     * 
     * precondition: the provided path is for a file and it exists
     * postconditon: the line is appended to the end of the file
     */
    @Override
    public void appendToFile(String path, String line) throws IOException {
        
        // formats the line to have a newline character at the enf
        String formattedLine = line + System.lineSeparator();
        
            // appends the formated line to the file using an open option
            Files.write(Paths.get(path), formattedLine.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        
    }


    /**
     * This inherited method is responsible for compressing a file at the given source path and write the result at the given target path
     * @param sourcePath the file to be compressed
     * @param targetPath the path that the comrpessed result should be written to
     * 
     * preconsdition: sourcePath is a file path that exists
     * postcondition: the file at the sourcepath is compressed at the target path
     */
    @Override
    public void compressFile(String sourcePath, String targetPath) throws Exception { 
        

        // no case where either would be null -> if they were, the command service (where they are used) would throw
        // if the source path does not exist, throw an exception
        if (!Files.exists(Paths.get(sourcePath))) { 
            throw new Exception("File at source path must exist");
        }

            // create the needed file streams
            FileInputStream fileInputStream = new FileInputStream(sourcePath);

            FileOutputStream fileIoutputStream = new FileOutputStream(targetPath);
            // creates a stream to "deflate" the file
            DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(fileIoutputStream);

            int data;
            // compress the file till the end of the file is reached
            while ((data = fileInputStream.read()) != -1) {
                deflaterOutputStream.write(data);
            }

            // close the streams
            fileInputStream.close();
            deflaterOutputStream.close();
        
    }

    /**
     * This iherited method is responsible for decompressing a file at the given source path and write the result at the given target path
     * @param sourcePath the file to be decompressed
     * @param targetPath the path that the decomrpessed result should be written to
     * 
     * preconsdition: sourcePath is a file path that exists
     * postcondition: the file at the sourcepath is decompressed to the target path
     */
    @Override
    public void decompressFile(String sourcePath, String targetPath) throws Exception { 

        // if the souercepath does not exist throw an exception
        if (!Files.exists(Paths.get(sourcePath))) { 
            throw new Exception("File at source path must exist");
        }

            // create the needed file streams
            FileInputStream fileInputStream = new FileInputStream(sourcePath);

            FileOutputStream fileOutputStream = new FileOutputStream(targetPath);

            InflaterInputStream inflaterOutputStream = new InflaterInputStream(fileInputStream);

            int data;
            // decompress the file till the end of the file is reached
            while ((data = inflaterOutputStream.read()) != -1) {
                fileOutputStream.write(data);
            }

            // close the streamns
            inflaterOutputStream.close();
            fileOutputStream.close();
        
    }

    /**
     * This inherited utility method is responsible for creating an empty file with all of its parent directories (if they are not already present) 
     * @param filePath the path to the file 
     *     
     * preconditon: the file path must not exist
     * postcondition: file should be created (with all parent folders)
     * */
    @Override
    public void makeFileWithParents(String filePath) throws Exception   {

        // if the file already exists throw an exception
        if (Files.exists(Paths.get(filePath))) {    
            throw new Exception("File already exists");
        }
        
        // new file object
            File file = new File(filePath);
            // if parents dosent exist, create the parent directory(s)
        if (!file.getParentFile().exists()) {
            
            Files.createDirectories( Paths.get(file.getParentFile().getAbsolutePath()));
        }
        // create the empty file
        file.createNewFile();
     
        
    }

}
