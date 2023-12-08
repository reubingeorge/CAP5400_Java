package org.CAP5400.Exceptions;

import org.CAP5400.Image.Image;
import org.CAP5400.RegionOfInterest.ROI;

public class RoiOutOfBoundsException extends Exception{
    public RoiOutOfBoundsException(Image image, ROI roi){
        super("The bounding box for the ROI is (" +
                roi.getStartX() + ", " + roi.getStartY() + ") to (" + roi.getEndX() + ", " + roi.getEndY() + "). " +
                "The image has dimensions (" + image.getRows() + ", " + image.getColumns() + ").");
    }
}
