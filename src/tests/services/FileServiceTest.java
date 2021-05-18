package tests.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.TestRule;

import di.Container;
import services.interfaces.IFileService;
import tests.Testable;

public class FileServiceTest extends Testable {

    @Test
    public void testReadFileLinesNoFile() {

        IFileService fileService = Container.resolveDependency(IFileService.class);

        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();

        String filePath = Paths.get(rootPath, "test.txt").toString();
        try {
            fileService.readFileLines(filePath, (line) -> System.out.println(line));
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }

        Assert.assertTrue(hasError);

    }

    @Test
    public void testReadFileLines() {

        IFileService fileService = Container.resolveDependency(IFileService.class);

        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String fileName = "test.txt";
        String filePath = Paths.get(rootPath, fileName).toString();
        System.out.println(filePath);
        String[] testLines = { "1st Version", "2nd Version", "3rd Version" };
        ArrayList<String> linesFromFile = new ArrayList<>();

        try {
            tempFolder.newFile(fileName);

            for (int i = 0; i < testLines.length; i++) {
                fileService.appendToFile(filePath, testLines[i]);
            }

            fileService.readFileLines(filePath, (line) -> {
                linesFromFile.add(line);
            });
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }

        Assert.assertFalse(hasError);
        Assert.assertFalse(linesFromFile.isEmpty());

        Assert.assertArrayEquals(testLines, linesFromFile.toArray());

    }

    @Test
    public void testReadDirectoriesNoFolder() {

        IFileService fileService = Container.resolveDependency(IFileService.class);

        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();

        String folderPath = Paths.get(rootPath, "test").toString();
        try {
            fileService.listDirectories(folderPath, (path) -> System.out.println(path.toAbsolutePath()));
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }

        Assert.assertTrue(hasError);

    }

    @Test
    public void testListDirectories() {

        IFileService fileService = Container.resolveDependency(IFileService.class);

        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();

        String[] testFolders = { "Chemistry", "English", "Math" };
        ArrayList<String> foldersFromFile = new ArrayList<>();

        try {

            for (int i = 0; i < testFolders.length; i++) {
                tempFolder.newFolder(testFolders[i]);
            }

            fileService.listDirectories(rootPath, (path) -> {

                String folderName = path.getFileName().toString();
                foldersFromFile.add(folderName);

            });
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }

        Assert.assertFalse(hasError);
        Assert.assertFalse(foldersFromFile.isEmpty());

        Assert.assertArrayEquals(testFolders, foldersFromFile.toArray());

    }

    @Test
    public void testAppendFileNoFile() {

        IFileService fileService = Container.resolveDependency(IFileService.class);

        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testLine = "1st Version";
        String filePath = Paths.get(rootPath, "test.txt").toString();
        String lineFromFile = null;
        try {
            fileService.appendToFile(filePath, testLine);

            lineFromFile = Files.readString(Paths.get(filePath));
            lineFromFile = lineFromFile.replace(System.lineSeparator(), "");
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }

        Assert.assertFalse(hasError);
        Assert.assertNotNull(lineFromFile);
        Assert.assertTrue(Files.exists(Paths.get(filePath))); // file should be created automatically
        Assert.assertTrue(new File(filePath).length() != 0); // file isnt empty - should i get the sirt line of the file
        Assert.assertEquals(testLine, lineFromFile);

    }

    @Test
    public void testAppendFileEmptyFile() {

        IFileService fileService = Container.resolveDependency(IFileService.class);

        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testLine = "1st Version";
        String filePath = Paths.get(rootPath, "test.txt").toString();
        String lineFromFile = null;
        try {
            tempFolder.newFile("test.txt");
            fileService.appendToFile(filePath, testLine);

            lineFromFile = Files.readString(Paths.get(filePath));
            lineFromFile = lineFromFile.replace(System.lineSeparator(), "");
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }

        Assert.assertFalse(hasError);
        Assert.assertNotNull(lineFromFile);
        Assert.assertTrue(Files.exists(Paths.get(filePath))); // file should be created automatically
        Assert.assertTrue(new File(filePath).length() != 0); // file isnt empty - should i get the sirt line of the file
        Assert.assertEquals(testLine, lineFromFile);
    }

    @Test
    public void testAppendFile() {

        
        IFileService fileService = Container.resolveDependency(IFileService.class);

        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String[] testLines =  {"1st Version", "2nd Version"};
        String filePath = Paths.get(rootPath, "test.txt").toString();
        ArrayList<String> linesFromFile = new ArrayList<>();


        try {
            // tempFolder.
            tempFolder.newFile("test.txt");
            Files.writeString(Paths.get(filePath), testLines[0] + System.lineSeparator(), StandardCharsets.UTF_8);
            fileService.appendToFile(filePath, testLines[1]);

           fileService.readFileLines(filePath, (line) -> linesFromFile.add(line));
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }
        
        Assert.assertFalse(hasError);
        Assert.assertFalse(linesFromFile.isEmpty());
        // Assert.assertTrue(Files.exists(Paths.get(filePath))); // file should be created automatically
        // Assert.assertTrue(new File(filePath).length() != 0); // file isnt empty - should i get the sirt line of the file
        Assert.assertArrayEquals(testLines, linesFromFile.toArray());
    }


    @Test
    public void testCompressFileNoSourceFile() {
        IFileService fileService = Container.resolveDependency(IFileService.class);


        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); // must exist
        

        try {
            fileService.compressFile(testSourcePath, testTargetPath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }


        Assert.assertTrue(hasError);
    }

    @Test
    public void testCompressAndDecompressFile() {
        //  most common case
        // this method is also a test for the decompress method
        IFileService fileService = Container.resolveDependency(IFileService.class);

        //The targetpoath will never exist as the file name is a uuid (uniqe)
        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        // empty
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); // 

        String resultTargetPath = Paths.get(rootPath, "result.txt").toString(); // 
        byte[] decompressedContent = null;   
        String originalContent = "1st Version";

        try {
            tempFolder.newFile("source.txt");
            Files.writeString(Paths.get(testSourcePath), originalContent, StandardCharsets.UTF_8);
            fileService.compressFile(testSourcePath, testTargetPath);


            fileService.decompressFile(testTargetPath, resultTargetPath);

            decompressedContent = Files.readAllBytes(Paths.get(resultTargetPath));



        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }


        Assert.assertFalse(hasError);
        Assert.assertTrue(Files.exists(Paths.get(testTargetPath)));
        Assert.assertNotNull(decompressedContent);
        Assert.assertArrayEquals(originalContent.getBytes(), decompressedContent);
    }

    @Test
    public void testCompressFileEmptyFile()  {
        //  most common case
        IFileService fileService = Container.resolveDependency(IFileService.class);

        //The targetpoath will never exist as the file name is a uuid (uniqe)
        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        // empty
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); // 

        String resultTargetPath = Paths.get(rootPath, "result.txt").toString(); // 
        byte[] decompressedContent = null;  
        byte[] originalContent = null;          

        try {
            tempFolder.newFile("source.txt");
            
            fileService.compressFile(testSourcePath, testTargetPath);


            fileService.decompressFile(testTargetPath, resultTargetPath);

            decompressedContent = Files.readAllBytes(Paths.get(resultTargetPath));

            originalContent = Files.readAllBytes(Paths.get(testSourcePath));


        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }


        Assert.assertFalse(hasError);
        Assert.assertTrue(Files.exists(Paths.get(testTargetPath)));
        Assert.assertNotNull(decompressedContent);
        Assert.assertNotNull(originalContent);
        Assert.assertArrayEquals(originalContent, decompressedContent);
    }


    @Test
    public void testDecompressFileNoSourceFile() {
        IFileService fileService = Container.resolveDependency(IFileService.class);


        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); // must exist
        

        try {
            fileService.decompressFile(testSourcePath, testTargetPath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }


        Assert.assertTrue(hasError);
    }


    @Test
    public void testDecompressFileNotCompressed() {
        IFileService fileService = Container.resolveDependency(IFileService.class);


        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); // must exist
        

        try {

            tempFolder.newFile("source.txt");
            Files.writeString(Paths.get(testSourcePath), "1st Version", StandardCharsets.UTF_8);


            fileService.decompressFile(testSourcePath, testTargetPath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }


        Assert.assertTrue(hasError);
    }

    
    @Test
    public void testDecompressEmptyFile() {
        IFileService fileService = Container.resolveDependency(IFileService.class);


        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); // must exist
        

        try {

            tempFolder.newFile("source.txt");
            // Files.writeString(Paths.get(testSourcePath), "1st Version", StandardCharsets.UTF_8);


            fileService.decompressFile(testSourcePath, testTargetPath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }


        Assert.assertTrue(hasError);
    }

    @Test
    public void testDecompressFileExistingTargetfile() {
        IFileService fileService = Container.resolveDependency(IFileService.class);
        // overtiting a file
        // compreesses source file to tatget path,
        // decompresses the target file to the source path
        /// checks if both content is the same


        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); // must exist
        
        // String resultTargetPath = Paths.get(rootPath, "result.txt").toString(); // 
        byte[] decompressedContent = null;  
        String originalContent = "1st Version";

        try {

            tempFolder.newFile("source.txt");
            Files.writeString(Paths.get(testSourcePath), originalContent, StandardCharsets.UTF_8);

            fileService.compressFile(testSourcePath, testTargetPath);

            fileService.decompressFile(testTargetPath, testSourcePath);

            decompressedContent = Files.readAllBytes(Paths.get(testSourcePath));

        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        }


        

        Assert.assertFalse(hasError);
        Assert.assertTrue(Files.exists(Paths.get(testTargetPath)));
        Assert.assertNotNull(decompressedContent);
        Assert.assertArrayEquals(originalContent.getBytes(), decompressedContent);
    }


    @Test 
    public void testmakeFileWithParentsExists() {
        IFileService fileService = Container.resolveDependency(IFileService.class);
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testFilePath = Paths.get(rootPath, "test.txt").toString(); // must exist
        boolean hasError = false;
        try {
            tempFolder.newFile("test.txt");
            fileService.makeFileWithParents(testFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;

            
        }

        Assert.assertTrue(hasError);

    }


    @Test 
    public void testmakeFileWithParentsParentsExists() {
        IFileService fileService = Container.resolveDependency(IFileService.class);
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testFilePath = Paths.get(rootPath, "test.txt").toString(); // must exist
        boolean hasError = false;
        try {
            
            fileService.makeFileWithParents(testFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;

            
        }

        Assert.assertFalse(hasError);
        Assert.assertTrue(Files.exists(Paths.get(testFilePath)));

    }


    @Test 
    public void testmakeFileWithParentsNoParentsExists() {
        IFileService fileService = Container.resolveDependency(IFileService.class);
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String testFilePath = Paths.get(rootPath, "test folder", "test.txt").toString(); // must exist
        boolean hasError = false;
        try {
            
            fileService.makeFileWithParents(testFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;

            
        }

        Assert.assertFalse(hasError);
        Assert.assertTrue(Files.exists(Paths.get(testFilePath)));

    }




}
