package org.CAP5400.Exceptions;

public class ImageNotFoundException extends Exception{
    public ImageNotFoundException(String filename) {
        super("Image file: `" + filename + "` cannot not found/opened.");
    }
}
