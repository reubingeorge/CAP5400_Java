package org.CAP5400.Exceptions;

/**
 * This exception is thrown when an image cannot be found/opened.
 * @Author Reubin George
 */
public class ImageNotFoundException extends Exception{
    public ImageNotFoundException(String filename) {
        super("Image file: `" + filename + "` cannot not found/opened.");
    }
}
