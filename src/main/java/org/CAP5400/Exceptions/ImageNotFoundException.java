package org.CAP5400.Exceptions;

/**
 * This exception is thrown when an image cannot be found/opened.
 * @Author Reubin George
 */
public class ImageNotFoundException extends Exception{
    /**
     * Constructor for the ImageNotFoundException class.
     * @param filename The filename of the image that cannot be found/opened.
     */
    public ImageNotFoundException(String filename) {
        super("Image file: `" + filename + "` cannot not found/opened.");
    }
}
