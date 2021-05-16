package services.interfaces;


import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface IFileService {
    public void readFileLines(String path, Consumer<String> callback) throws Exception;
    public void listDirectories(String path, Consumer<Path> callback) throws Exception;
    public void appendToFile(String path, String line) throws IOException;
    public byte[] readFiletoBuffer(String path);
    public void writeBuffertoFile(String path, byte[] buffer); 
    public void compressFile(String sourcePath, String targetPath) throws Exception;
    public void decompressFile(String sourcePath, String targetPath) throws Exception;
    public void makeFileWithParents(String filePath) throws IOException;
    
}
