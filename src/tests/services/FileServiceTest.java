/**
 * This class is responsible for tesing the FileService class and all its methods.
 * It extends the Testable abstract class to ihertit is default behaviour 
 * 
 * @author Mitchell Mark-George
 */ 

package tests.services;
// Nesseccary imports
import java.io.File;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;


import di.Container;
import services.interfaces.IFileService;
import tests.Testable;

public class FileServiceTest extends Testable {

    /**
     * This methods test reading the lines of a file that doesn not exist
     */
    @Test
    public void testReadFileLinesNoFile() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);
        // error flag
        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // test file path
        String filePath = Paths.get(rootPath, "test.txt").toString();
        try {
            // try and read the file lines of the test path
            fileService.readFileLines(filePath, (line) -> System.out.println(line));
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occurred
        }
        // an exception should be thrown
        Assert.assertTrue(hasError);

    }

    /**
     * This method tests reading the lines of an existing file 
     */
    @Test
    public void testReadFileLines() {   
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);
        // error flag
        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        String fileName = "test.txt";
        // file name of the test file
        String filePath = Paths.get(rootPath, fileName).toString();
        // lines to be written to the file
        String[] testLines = { "1st Version", "2nd Version", "3rd Version" };
        ArrayList<String> linesFromFile = new ArrayList<>();

        try {
            // create the file
            tempFolder.newFile(fileName);
            // write the lines to the file
            for (int i = 0; i < testLines.length; i++) {
                fileService.appendToFile(filePath, testLines[i]);
            }
            // try and read the lines of the file and save the result in a list
            fileService.readFileLines(filePath, (line) -> {
                linesFromFile.add(line);
            });
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }
        // no error should occur
        Assert.assertFalse(hasError);
        // the list should not be empty
        Assert.assertFalse(linesFromFile.isEmpty());
        // the test lines should be eraul to the result of the list
        Assert.assertArrayEquals(testLines, linesFromFile.toArray());

    }

    /**
     * This method terts listing directories when there is no folder
     */
    @Test
    public void testReadDirectoriesNoFolder() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);

        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // test path for non existet folder
        String folderPath = Paths.get(rootPath, "test").toString();
        try {
            // try to list the directoried of the folder path
            fileService.listDirectories(folderPath, (path) -> System.out.println(path.toAbsolutePath()));
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }
        // an exception should be thrown
        Assert.assertTrue(hasError);

    }

    /**
     * This method test reading an exision folder
     */
    @Test
    public void testListDirectories() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);
        //error flag
        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // test folders to be made
        String[] testFolders = { "Chemistry", "English", "Math" };
        ArrayList<String> foldersFromFile = new ArrayList<>();

        try {
            // create all the est folders
            for (int i = 0; i < testFolders.length; i++) {
                tempFolder.newFolder(testFolders[i]);
            }
            // readt the rood folders and save the result in an arraylist
            fileService.listDirectories(rootPath, (path) -> {

                String folderName = path.getFileName().toString();
                foldersFromFile.add(folderName);

            });
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }

        // there shouold be no error
        Assert.assertFalse(hasError);
        // the result list cant be empty
        Assert.assertFalse(foldersFromFile.isEmpty());
        // the result list and the lest array should have the same content
        Assert.assertArrayEquals(testFolders, foldersFromFile.toArray());

    }

    /**
     * This method test appending a tring to the end of a file that does not exist
     */
    @Test
    public void testAppendFileNoFile() { 
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);
        // error flag
        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // line to be appended to the file
        String testLine = "1st Version";
        // file path for the test file that does not exist
        String filePath = Paths.get(rootPath, "test.txt").toString();
        String lineFromFile = null;
        try {
            // try to append the test srtring to the file that does not exist 
            fileService.appendToFile(filePath, testLine);

            // read the line from the test file and clean the string
            lineFromFile = Files.readString(Paths.get(filePath));
            lineFromFile = lineFromFile.replace(System.lineSeparator(), "");
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }
        // there should be no error
        Assert.assertFalse(hasError);
        // the read line from the file should not be null
        Assert.assertNotNull(lineFromFile);
        Assert.assertTrue(Files.exists(Paths.get(filePath))); // file should be created automatically
        Assert.assertTrue(new File(filePath).length() != 0); // file isnt empty 
        Assert.assertEquals(testLine, lineFromFile); // the line from the file should be the save as the string that was appended

    }

    /**
    * This method tests appending a string to the end of an empty file
     */
    @Test
    public void testAppendFileEmptyFile() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);
        // error flag
        boolean hasError = false;
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // test string to be appended
        String testLine = "1st Version";
        // path of the file to be appended to 
        String filePath = Paths.get(rootPath, "test.txt").toString();
        String lineFromFile = null;
        try {
            // create the test file
            tempFolder.newFile("test.txt");
            // append the tring to the test file
            fileService.appendToFile(filePath, testLine);
            // read the line from the test file and clean the string
            lineFromFile = Files.readString(Paths.get(filePath));
            lineFromFile = lineFromFile.replace(System.lineSeparator(), "");
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }

        // error should not have occures
        Assert.assertFalse(hasError);
        // the line can not be null
        Assert.assertNotNull(lineFromFile);
        Assert.assertTrue(Files.exists(Paths.get(filePath))); // file should be created automatically
        Assert.assertTrue(new File(filePath).length() != 0); // file isnt empty
        Assert.assertEquals(testLine, lineFromFile); // the line from the file should be the save as the string that was appended
    }


    /**
     * This methosd tests compressing a file with a non existed source file
     */
    @Test
    public void testCompressFileNoSourceFile() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);

        // error flag
        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // pathth to the non existent source path
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        // path to the non existedt target path
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); 
        

        try {
            // try and compresss the file at the source path
            fileService.compressFile(testSourcePath, testTargetPath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occurred
        }

        // an exception shoul be thrown
        Assert.assertTrue(hasError);
    }

    /**
     * This method tests compressing then decomressing a file
     */
    @Test
    public void testCompressAndDecompressFile() {
        //  most common case
        // this method is also a test for the decompress method

        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);

        //error path
        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // path for the source file (to be compressed to the target)
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        // path for the target file (file with compressed content of source file)
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); // 
    // path for the result file (target path will be decompresed here)
        String resultTargetPath = Paths.get(rootPath, "result.txt").toString(); // 
        byte[] decompressedContent = null;   
        String originalContent = "1st Version";


        try {
            // create the source file and whrite some content to is
            tempFolder.newFile("source.txt");
            Files.writeString(Paths.get(testSourcePath), originalContent, StandardCharsets.UTF_8);
            // compress the test source file to the target path
            fileService.compressFile(testSourcePath, testTargetPath);
            // decompress the target file to the result path
            fileService.decompressFile(testTargetPath, resultTargetPath);
            // read the decompressed file into a variable
            decompressedContent = Files.readAllBytes(Paths.get(resultTargetPath));



        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occurred
        }

        // no error should occure
        Assert.assertFalse(hasError);
        // ta file at the target path should be created
        Assert.assertTrue(Files.exists(Paths.get(testTargetPath)));
        // decompressed content cant be null
        Assert.assertNotNull(decompressedContent);
        // the original content and the decompressed content should be the same
        Assert.assertArrayEquals(originalContent.getBytes(), decompressedContent);
    }

    /**
     * This method tests compresseing an empty file
     */
    @Test
    public void testCompressFileEmptyFile()  {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);

        // error flag
        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // the path to the source file
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        // path to the target file (dosent exist)
        String testTargetPath = Paths.get(rootPath, "target.txt").toString();  
        // path for the result file
        String resultTargetPath = Paths.get(rootPath, "result.txt").toString();  
        byte[] decompressedContent = null;  
        byte[] originalContent = null;          

        try {
            // creates rhe soirce file
            tempFolder.newFile("source.txt");
            // compresses the source file to the target path
            fileService.compressFile(testSourcePath, testTargetPath);
            // decomresses the file tothe result file (to test)
            fileService.decompressFile(testTargetPath, resultTargetPath);
            // read the decompressed result file into a variable
            decompressedContent = Files.readAllBytes(Paths.get(resultTargetPath));
            // read the original file into a variable
            originalContent = Files.readAllBytes(Paths.get(testSourcePath));


        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }

        // no exception should be thrown
        Assert.assertFalse(hasError);
        // a file at the target path should be made
        Assert.assertTrue(Files.exists(Paths.get(testTargetPath)));
        // neither the decompressed or original content can be null
        Assert.assertNotNull(decompressedContent);
        Assert.assertNotNull(originalContent);
        // the original content of the file should be the sdame as the decompressed contetnof the result
        Assert.assertArrayEquals(originalContent, decompressedContent);
    }


    /**
     * This method tests decoresseng a file with no existing source file
     */
    @Test
    public void testDecompressFileNoSourceFile() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);

        // error file
        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // path of the source file
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        // path of the target file
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); 
        

        try {
            fileService.decompressFile(testSourcePath, testTargetPath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }

        // an exception should be thrown
        Assert.assertTrue(hasError);
    }


    /**
     * This file test decopressing a file that is not compressed
     */
    @Test
    public void testDecompressFileNotCompressed() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);

        // error flag
        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // path to the souerce file
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        // path to the target file
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); 
        

        try {   
            // create the source file and write to it
            tempFolder.newFile("source.txt");
            
            Files.writeString(Paths.get(testSourcePath), "1st Version", StandardCharsets.UTF_8);

            // try and decomress the file into the target path
            fileService.decompressFile(testSourcePath, testTargetPath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }

        // an exception should be thrown
        Assert.assertTrue(hasError);
    }

    /**
     * This method tests decompressing an empty file
     */
    @Test
    public void testDecompressEmptyFile() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);

        // error flag
        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // path to the source file
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        // path to the target file
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); 
        

        try {
        // create the source file
            tempFolder.newFile("source.txt");
            // try and decomress the source file into the target path
            fileService.decompressFile(testSourcePath, testTargetPath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }

        // an exception should be thrown
        Assert.assertTrue(hasError);
    
    }

    /**
     * This method tests decompressing a file into another existing file
     */
    @Test
    public void testDecompressFileExistingTargetfile() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);
        // overtiting a file
       

        // error flag
        boolean hasError = false; 
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // path to source file
        String testSourcePath = Paths.get(rootPath, "source.txt").toString(); // must exist
        // path to target file
        String testTargetPath = Paths.get(rootPath, "target.txt").toString(); 
        
        
        byte[] decompressedContent = null;  
        String originalContent = "1st Version";

        try {
            // create the source file and write to it   
            tempFolder.newFile("source.txt");
            Files.writeString(Paths.get(testSourcePath), originalContent, StandardCharsets.UTF_8);
            // compress tthe source path into the target path
            fileService.compressFile(testSourcePath, testTargetPath);
            // decompress rhe target path in to the source path
            fileService.decompressFile(testTargetPath, testSourcePath);
            // read the decompressed content
            decompressedContent = Files.readAllBytes(Paths.get(testSourcePath));

        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured
        }


        
        // no exception should be thrown
        Assert.assertFalse(hasError);
        // the target path should be created
        Assert.assertTrue(Files.exists(Paths.get(testTargetPath)));
        // the decomressed content from the file cannot be null
        Assert.assertNotNull(decompressedContent);
        // the decomressed from the file shouls be the same as the original content
        Assert.assertArrayEquals(originalContent.getBytes(), decompressedContent);
    }


    /**
     * This method tests trying to make a file when it already exists
     *      */
    @Test 
    public void testmakeFileWithParentsExists() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // test file path that already exist
        String testFilePath = Paths.get(rootPath, "test.txt").toString();
        // error flag
        boolean hasError = false;
        try {
            // create the test flag
            tempFolder.newFile("test.txt");
            // try to make a file that already exists
            fileService.makeFileWithParents(testFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occured            
        }
        // an exception should be thrown
        Assert.assertTrue(hasError);

    }


    /**
     * This method tests making a file when the parents exist
     */
    @Test 
    public void testmakeFileWithParentsParentsExists() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // path of the test file to be made
        String testFilePath = Paths.get(rootPath, "test.txt").toString();
        // error flag
        boolean hasError = false;
        try {
            // try and make the file at the path
            fileService.makeFileWithParents(testFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // errror occurred

            
        }
        // no error should be thrown
        Assert.assertFalse(hasError);
        // the file should be created
        Assert.assertTrue(Files.exists(Paths.get(testFilePath)));

    }


    /**
     * This method tests creating a file and its parent
     */
    @Test 
    public void testmakeFileWithParentsNoParentsExists() {
        // Injecting the needed services from the dependency injection container
        IFileService fileService = Container.resolveDependency(IFileService.class);
        String rootPath = tempFolder.getRoot().getAbsolutePath();
        // path for the test file with non existent parents
        String testFilePath = Paths.get(rootPath, "test folder", "test.txt").toString(); // must exist
        //error flag
        boolean hasError = false;
        try {
            // try to make the file and its parent directory
            fileService.makeFileWithParents(testFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
            // error occuured
            
        }
        // no error should be thrown
        Assert.assertFalse(hasError);
        // the file should be created (with its parents)
        Assert.assertTrue(Files.exists(Paths.get(testFilePath)));

    }




}
