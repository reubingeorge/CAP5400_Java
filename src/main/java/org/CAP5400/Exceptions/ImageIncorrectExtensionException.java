package org.CAP5400.Exceptions;

/**
 * This exception is thrown when an incorrect image extension is detected.
 * @Author Reubin George
 */
public class ImageIncorrectExtensionException extends Exception{
    /**
     * Constructor for the ImageIncorrectExtensionException class.
     * @param extension The incorrect image extension detected.
     */
    public ImageIncorrectExtensionException(String extension) {
        super("`" + extension + "` is not a valid image extension. Please use `pgm` or `ppm` files. ");
    }
}
