package org.CAP5400.Exceptions;

import org.CAP5400.Image.Image;

/**
 * This exception is thrown when two images are not the same.
 * @author Reubin George
 */
public class ImageDimensionsNotSameException extends Exception{
    /**
     * Constructor for ImageNotSameException
     * @param dstImage destination image
     * @param srcImage source image
     */
    public ImageDimensionsNotSameException(Image dstImage, Image srcImage){
        super("The source image has dimensions ("
                + srcImage.getRows() + ", " + srcImage.getColumns() + ", " + srcImage.getNumChannels() +
                ") while the destination image has dimensions (" +
                dstImage.getRows() + ", " + dstImage.getColumns() + ", " + dstImage.getNumChannels() + ")");
    }
}
