package org.CAP5400.Exceptions;

/**
 * This exception is thrown when an illegal colorspace is detected.
 * @author Reubin George
 *
 */
public class IllegalColorspaceException extends Exception{
    /**
     * Constructor for the IllegalColorspaceException class.
     * @param colorspace The illegal colorspace detected.
     */
    public IllegalColorspaceException(String colorspace) {
        super("Illegal colorspace: `" + colorspace + "` detected!");
    }
}
