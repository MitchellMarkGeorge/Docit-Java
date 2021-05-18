package services;


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

    // private PathService pathService = new PathService(projectName)


    // IErrorService errorService = (IErrorService) Container.resolveDependency(IErrorService.class);

    // writing/ reading binary data should be handles differenetly

    // https://mkyong.com/java/java-how-to-read-a-file/
    // https://www.baeldung.com/java-try-with-resources
    // https://www.javacodegeeks.com/2015/02/java-8-pitfall-beware-files-lines.html

    /**
     * Should be used for files like config file and versions files
     * 
     * @param path
     * @param callback
     * @throws Exception
     */
    @Override
    public void readFileLines(String path, Consumer<String> callback) throws Exception {
        // Files.lines(path).parallel().forEachOrdered(action);
        // Should be fast and not load eveything into memory
        try (Stream<String> stream = Files.lines(Paths.get(path))) { // should i use parrallel?
            stream.forEachOrdered(callback);

        } catch (Exception e) {
            
            throw e;
            // TODO: handle exception
            // GUI Dialog response
        }

        // Alternative
        // public static void main(String[] args) throws IOException {

        // String fileName = "/home/mkyong/app.log";

        // // defaultCharBufferSize = 8192; or 8k
        // try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        // String line;
        // while ((line = br.readLine()) != null) {
        // System.out.println(line);
        // }
        // }

        // }
    }

    /**
     * 
     * @param path
     * @param callback
     * @throws Exception
     */
    @Override
    public void listDirectories(String path, Consumer<Path> callback) throws Exception {
        if (!Files.isDirectory(Paths.get(path))) {
            // TODO: This should be handles
            throw new Exception("Path must be a directory");
        }
        try (Stream<Path> stream = Files.list(Paths.get(path))) {
            stream.forEachOrdered(callback);
        } catch (Exception e) {
            // TODO: handle exception
           
            throw e;
        }
    }

    @Override
    public void appendToFile(String path, String line) throws IOException {
        // try-resource-idiom

        String formattedLine = line + System.lineSeparator();
        // try {
            Files.write(Paths.get(path), formattedLine.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        // } catch (Exception e) {
        //     // TODO: handle exception
        //     e.printStackTrace();
        // }

    }


    @Override
    public void compressFile(String sourcePath, String targetPath) throws Exception { // should throw
        // Path source = Paths.get(sourcePath);
        // Path target = Paths.get(targetPath);

        // no case where either would be null -> if they were, the command service (where they are used)

        if (!Files.exists(Paths.get(sourcePath))) { // would be handled anyway but it is better to 
            throw new Exception("File at source path must exist");
        }

        // try {
            FileInputStream fileInputStream = new FileInputStream(sourcePath);

            FileOutputStream fileIoutputStream = new FileOutputStream(targetPath);

            DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(fileIoutputStream);

            int data;

            while ((data = fileInputStream.read()) != -1) {
                deflaterOutputStream.write(data);
            }

            // close the file
            fileInputStream.close();
            deflaterOutputStream.close();
        
    }

    @Override
    public void decompressFile(String sourcePath, String targetPath) throws Exception { // should throw

        if (!Files.exists(Paths.get(sourcePath))) { // would be handled anyway but it is better to 
            throw new Exception("File at source path must exist");
        }

        // try {
            FileInputStream fileInputStream = new FileInputStream(sourcePath);

            FileOutputStream fileOutputStream = new FileOutputStream(targetPath);

            InflaterInputStream inflaterOutputStream = new InflaterInputStream(fileInputStream);

            int data;

            while ((data = inflaterOutputStream.read()) != -1) {
                fileOutputStream.write(data);
            }

            // close the files

            inflaterOutputStream.close();
            fileOutputStream.close();
        // } catch (Exception e) {
        //     e.printStackTrace();

        //     errorService.showErrorDialog("There was an error decompressing the file. Make sure the document is closed.");
        // }

    }

    @Override
    public void makeFileWithParents(String filePath) throws Exception   {


        if (Files.exists(Paths.get(filePath))) {
            throw new Exception("File already exists");
        }
        // try {
            File file = new File(filePath);
        // Files.createFile(path, attrs)
        if (!file.getParentFile().exists()) {
            // file.getParentFile().mkdirs();
            Files.createDirectories( Paths.get(file.getParentFile().getAbsolutePath()));
        }

        file.createNewFile();
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     //TODO: handle exception
        // }
        
    }

}
