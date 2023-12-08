package org.CAP5400.Exceptions;

public class ImageIncorrectExtensionException extends Exception{
    public ImageIncorrectExtensionException(String extension) {
        super("`" + extension + "` is not a valid image extension. Please use `pgm` or `ppm` files. ");
    }
}
