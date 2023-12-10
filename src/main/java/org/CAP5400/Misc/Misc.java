package org.CAP5400.Misc;

import org.CAP5400.Main;

import java.io.IOException;
import java.nio.file.*;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains miscellaneous methods. These methods are used to perform various tasks such as getting the
 * extension of a file, generating a random string, getting the current date and time, deleting a file, etc.
 * @version 1.0
 * @author Reubin George
 */
public class Misc {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String trackerFilename = "tracker.txt";

    /**
     * This method is used to get the extension of a file.
     * @param fileName The name of the file.
     * @return The extension of the file.
     */
    public static String getFileExtension(String fileName) {
        if(fileName == null || fileName.isEmpty() || fileName.isBlank()) {
            return "";
        }
        var lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }

    /**
     * This method is used to generate a random string of a given length.
     * @param length The length of the string.
     * @return The random string.
     */
    public static String getRandomString(int length){
        if(length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0.");
        }
        var random = new SecureRandom();
        var stringBuilder = new StringBuilder(length);
        for(int i = 0; i < length; i++) {
            var randomIndex = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

    /**
     * This method is used to get the current date and time in the format yyyyMMdd_HHmmss.
     * @return The current date and time.
     */
    public static String getCurrentFormattedDateTime(){
        var currentTime = new Date();
        var dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return dateFormat.format(currentTime);
    }

    /**
     * This method is used to delete a file.
     * @param filename The name of the file to be deleted.
     * @throws IOException Error thrown if the file cannot be deleted.
     */
    public static void delete(String filename) throws IOException {
        var filePath = Path.of(filename);
        if(Files.exists(filePath)){
            Files.delete(filePath);
        }
    }

    /**
     * This method is used to create a tracker file if it does not exist.
     * @throws Exception Error thrown if the tracker file cannot be created.
     */
    protected static void createTrackerFile() throws Exception {
        var filePath = Paths
                .get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()) //location of the Main class
                .getParent() //parent directory of the main file
                .resolve(trackerFilename); // create a path for tracker file in parent directory

        try{ Files.createFile(filePath); }
        catch (FileAlreadyExistsException e) {}
        catch (Exception e){ throw e; }
    }

    /**
     * This method is used to append a filename to the tracker file.
     * @param filename The name of the file to be appended.
     * @throws Exception Error thrown if the tracker file cannot be created.
     */
    public static void appendToTrackerFile(String filename) throws Exception {
        createTrackerFile();
        var filePath = Paths
                .get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()) //location of the Main class
                .getParent() //parent directory of the main file
                .resolve(trackerFilename); // create a path for tracker file in parent directory
        try(var writer = Files.newBufferedWriter(filePath, StandardOpenOption.APPEND)){
            writer.write(filename);
            writer.newLine();
        }

    }

    /**
     * This method is used to remove a filename from the tracker file.
     * @param filename The name of the file to be removed.
     * @throws Exception Error thrown if the tracker file cannot be created.
     */
    public static void removeFromTrackerFile(String filename) throws Exception{
        var filePath = Paths
                .get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()) //location of the Main class
                .getParent() //parent directory of the main file
                .resolve(trackerFilename); // create a path for tracker file in parent directory
        if(!Files.exists(filePath)){
            return;
        }
        var lines = Files.readAllLines(filePath);
        var updatedLine = lines.stream()
                .filter(line -> !line.equals(filename))
                .collect(Collectors.toList());
        Files.write(filePath, updatedLine);
    }

    /**
     * This method is used to check if a file is blank or empty.
     * @param filePath The path of the file.
     * @return True if the file is blank or empty, false otherwise.
     * @throws Exception Error thrown if the file cannot be read.
     */
    private static boolean isFileBlankOrEmpty(Path filePath) throws Exception{
        if(!Files.exists(filePath)){
            return true;
        }

        var lines = Files.readAllLines(filePath);
        for(var line : lines){
            if(!line.trim().isEmpty()){
                return false;
            }
        }
        return true;
    }

    /**
     * This method is used to get the contents of the tracker file and remove said file from the tracker file.
     * @return The contents of the tracker file.
     * @throws Exception Error thrown if the tracker file cannot be read.
     */
    public static List<String> popContentsOfTrackerFile() throws Exception{
        var filePath = Paths
                .get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()) //location of the Main class
                .getParent() //parent directory of the main file
                .resolve(trackerFilename); // create a path for tracker file in parent directory
        if(isFileBlankOrEmpty(filePath)){
            return Collections.emptyList();
        }
        var files = Files.readAllLines(filePath);
        Files.write(filePath, Collections.emptyList());
        return files;
    }

    /**
     * This method is used to delete all files with a given extension.
     * @param extensions The extensions of the files to be deleted. The extensions should not contain the dot.
     * @throws Exception Error thrown if the files cannot be deleted.
     */
    public static void deleteFilesWithExtension(String...extensions) throws Exception {
        Path currentDirectory = Paths.get("");
        for(var extension : extensions){
            try (DirectoryStream<Path> directoryStream =
                         Files.newDirectoryStream(currentDirectory, "*." + extension)) {
                for (Path filePath : directoryStream) {
                    Files.delete(filePath);
                }
            }
        }
    }

    /**
     * This method is used to check if a file exists.
     * @param filename The name of the file.
     * @return True if the file exists, false otherwise.
     */
    public static boolean doesFileExist(String filename){
        var path = Paths.get(filename);
        return Files.exists(path) && Files.isRegularFile(path);
    }
}
