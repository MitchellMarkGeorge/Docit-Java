package services.interfaces;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.Consumer;

public interface IFileService {
    public void readFileLines(String path, Consumer<String> callback);
    public void listDirectories(String path, Consumer<Path> callback);
    public void appendToFile(String path, String line);
    public byte[] readFiletoBuffer(String path);
    public void writeBuffertoFile(String path, byte[] buffer); 
    public void compressFile(String sourcePath, String targetPath);
    public void decompressFile(String sourcePath, String targetPath);
    public void makeFileWithParents(String filePath);
    
}
