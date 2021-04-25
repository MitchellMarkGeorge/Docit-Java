package services.interfaces;

import java.nio.file.Path;
import java.util.function.Consumer;

public interface IFileService {
    public void readFileLines(Path path, Consumer<String> callback);
    public void listDirectories(Path path, Consumer<Path> callback);
    public void appendToFile(Path path, String line);
    public byte[] readFiletoBuffer(Path path);
    public void writeBuffertoFile(Path path, byte[] buffer);

    
}
