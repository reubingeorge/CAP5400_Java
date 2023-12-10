package org.CAP5400.Exceptions;

import org.CAP5400.Image.Image;
import org.CAP5400.RegionOfInterest.ROI;

/**
 * This exception is thrown when the ROI is out of bounds of the image.
 * @Author Reubin George
 */
public class RoiOutOfBoundsException extends Exception{
    /**
     * Constructor for the RoiOutOfBoundsException class.
     * @param image The image that the ROI is out of bounds of.
     * @param roi The ROI that is out of bounds of the image.
     */
    public RoiOutOfBoundsException(Image image, ROI roi){
        super("The bounding box for the ROI is (" +
                roi.getStartX() + ", " + roi.getStartY() + ") to (" + roi.getEndX() + ", " + roi.getEndY() + "). " +
                "The image has dimensions (" + image.getRows() + ", " + image.getColumns() + ").");
    }
}
