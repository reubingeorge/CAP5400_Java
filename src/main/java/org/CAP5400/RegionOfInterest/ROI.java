package org.CAP5400.RegionOfInterest;

import jakarta.validation.constraints.NotNull;
import org.CAP5400.Exceptions.RoiOutOfBoundsException;
import org.CAP5400.Image.Image;

import java.util.Objects;

/**
 * This class represents a region of interest in an image. It is used to perform operations on a subset of an image.
 * @see Image
 * @Author Reubin George
 */
public class ROI {
    private Image sourceImage, regionImage;
    private int startX, startY, totalX, totalY;

    /**
     * Method to get the start x coordinate of the region of interest.
     * @return The start x coordinate of the region of interest.
     */
    public int getStartX() {
        return startX;
    }

    /**
     * Method to get the start y coordinate of the region of interest.
     * @return The start y coordinate of the region of interest.
     */
    public int getStartY() {
        return startY;
    }

    /**
     * Method to get the total x coordinate of the region of interest.
     * @return The total x coordinate of the region of interest.
     */
    public int getTotalX() {
        return totalX;
    }

    /**
     * Method to get the source image of the region of interest.
     * @return The source image of the region of interest.
     */
    public Image getSourceImage() {
        return sourceImage;
    }

    /**
     * Method to get the region image of the region of interest.
     * @return The region image of the region of interest.
     */
    public Image getRegionImage() {
        return regionImage;
    }

    /**
     * Method to get the total y coordinate of the region of interest.
     * @return The total y coordinate of the region of interest.
     */
    public int getTotalY() {
        return totalY;
    }

    /**
     * Method to get the end x coordinate of the region of interest.
     * @return The end x coordinate of the region of interest.
     */
    public int getEndX() {
        return startX + totalX;
    }

    /**
     * Method to get the end y coordinate of the region of interest.
     * @return The end y coordinate of the region of interest.
     */
    public int getEndY() {
        return startY + totalY;
    }

    /**
     * Method to get the number of channels in the region of interest.
     * @return The number of channels in the region of interest.
     */
    public int getChannels() {
        return this.sourceImage.getChannels();
    }

    /**
     * Constructor to create a region of interest from an image.
     * @param image The image to create the region of interest from.
     * @param startX The start x coordinate of the region of interest.
     * @param startY The start y coordinate of the region of interest.
     * @param totalX The total x coordinate of the region of interest.
     * @param totalY The total y coordinate of the region of interest.
     * @throws Exception Error thrown if the region of interest is out of bounds.
     */
    public ROI(@NotNull Image image, int startX, int startY, int totalX, int totalY) throws Exception {

        var isStartInBound = image.isInBounds(startX, startY);
        var isEndInBound = image.isInBounds(startX + totalX, startY + totalY);
        if(!isStartInBound || !isEndInBound) {
            throw new RoiOutOfBoundsException(image, this);
        }

        this.sourceImage = image;
        this.startX = startX;
        this.startY = startY;
        this.totalX = totalX;
        this.totalY = totalY;
        this.regionImage = new Image(totalX, totalY, getChannels());

        for(int i = 0; i < totalX; i++) {
            for(int j = 0; j < totalY; j++) {
                for(int k = 0; k < this.sourceImage.getChannels(); k++) {
                    var value = this.sourceImage.getPixel(i + startX, j + startY, k);
                    this.regionImage.setPixel(i, j, k, value);
                }
            }
        }
    }

    /**
     * Method to enforce that the region of interest is square.
     * @param enforceSquare A boolean value to enforce that the region of interest is square.
     */
    public void enforceSquareDimensions(boolean enforceSquare){
        if(enforceSquare && (this.totalX != this.totalY)){
            throw new IllegalArgumentException("ROI must be square");
        }
    }

    /**
     * Method to enforce that the region of interest is in the form of 2^n x 2^m.
     * @param enforceBaseTwo A boolean value to enforce that the region of interest is in the form of 2^n x 2^m.
     */
    public void enforceBaseTwoDimensions(boolean enforceBaseTwo){
        boolean baseTwoX = (this.totalX & (this.totalX - 1)) == 0;
        boolean baseTwoY = (this.totalY & (this.totalY - 1)) == 0;
        if(enforceBaseTwo && (!baseTwoX || !baseTwoY)){
            throw new IllegalArgumentException("ROI must be in the form of 2^n x 2^n");
        }
    }

    /**
     * Method to check if two regions of interest are equal.
     * @param obj The other region of interest to compare to.
     * @return A boolean value indicating if the two regions of interest are equal.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ROI other) {
            return this.regionImage.equals(other.regionImage) &&
                    this.startX == other.startX &&
                    this.startY == other.startY &&
                    this.totalX == other.totalX &&
                    this.totalY == other.totalY;
        }
        return false;
    }

    /**
     * Method to get the string representation of the region of interest.
     * @return The string representation of the region of interest.
     */
    @Override
    public String toString() {
        return "ROI{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", totalX=" + totalX +
                ", totalY=" + totalY +
                '}';
    }

    /**
     * Method to get the hash code of the region of interest.
     * @return The hash code of the region of interest.
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.regionImage, this.startX, this.startY, this.totalX, this.totalY);
    }

    /**
     * Method to apply the modifications to the region of interest.
     * @throws Exception Error thrown if the modifications cannot be applied.
     */
    public void applyModifications() throws Exception {
        for(int i = getStartX(); i < getEndX(); i++){
            for(int j = getStartY(); j < getEndY(); j++){
                for(int k = 0; k < sourceImage.getChannels(); k++){
                    var newValue = regionImage.getPixel(i - startX, j - startY, k);
                    sourceImage.setPixel(i, j, k, newValue);
                }
            }
        }
    }

}
