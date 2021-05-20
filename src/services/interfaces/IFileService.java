/**
 * This interface defines what a FileService class should do (how it behaves) and the methods it should implement. 
 * 
 * @author Mitchell Mark-George
 */

package services.interfaces;


import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface IFileService {
    /**
     * This method is responsible for reading a file line by line and calling a callback on each read line
     * @param path path of the file to be read
     * @param callback function to be executed on each file line
     * 
     */
    public void readFileLines(String path, Consumer<String> callback) throws Exception;


    /**
     * This method is responsible for reading a directory and calling a callback on each found subdirectory
     * @param path path of the folder to be read
     * @param callback function to be executed on each file line
     * 
     */
    public void listDirectories(String path, Consumer<Path> callback) throws Exception;

    /**
     * This method is responsible for appending a string line to the end of the file at the given path
     * @param path path of the file to append the string
     * @param line the string to append at the end of the file
     * 
     */
    public void appendToFile(String path, String line) throws IOException;

    /**
     * This method is responsible for compressing a file at the given source path and write the result at the given target path
     * @param sourcePath the file to be compressed
     * @param targetPath the path that the comrpessed result should be written to
     * 
     */
    public void compressFile(String sourcePath, String targetPath) throws Exception; //lossless

    /**
     * This method is responsible for decompressing a file at the given source path and write the result at the given target path
     * @param sourcePath the file to be decompressed
     * @param targetPath the path that the decomrpessed result should be written to
     * 
     */
    public void decompressFile(String sourcePath, String targetPath) throws Exception;

    /**
     * This utility method is responsible for creating an empty file with all of its parent directories (if they are not already present) 
     * @param filePath the path to the file 
     *     
     * */
    public void makeFileWithParents(String filePath) throws IOException, Exception;
    
}
