package org.CAP5400.RegionOfInterest;

import org.CAP5400.Exceptions.RoiOutOfBoundsException;
import org.CAP5400.Image.Image;

import java.util.Objects;

public class ROI {
    private Image sourceImage, regionImage;
    private int startX, startY, totalX, totalY;
    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getTotalX() {
        return totalX;
    }

    public Image getSourceImage() {
        return sourceImage;
    }

    public Image getRegionImage() {
        return regionImage;
    }

    public int getTotalY() {
        return totalY;
    }

    public int getEndX() {
        return startX + totalX;
    }

    public int getEndY() {
        return startY + totalY;
    }

    public int getChannels() {
        return this.sourceImage.getChannels();
    }

    public ROI(Image image, int startX, int startY, int totalX, int totalY) throws Exception {

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

    public void enforceSquareDimensions(boolean enforceSquare){
        if(enforceSquare && (this.totalX != this.totalY)){
            throw new IllegalArgumentException("ROI must be square");
        }
    }

    public void enforceBaseTwoDimensions(boolean enforceBaseTwo){
        boolean baseTwoX = (this.totalX & (this.totalX - 1)) == 0;
        boolean baseTwoY = (this.totalY & (this.totalY - 1)) == 0;
        if(enforceBaseTwo && (!baseTwoX || !baseTwoY)){
            throw new IllegalArgumentException("ROI must be in the form of 2^n x 2^n");
        }
    }

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

    @Override
    public String toString() {
        return "ROI{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", totalX=" + totalX +
                ", totalY=" + totalY +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.regionImage, this.startX, this.startY, this.totalX, this.totalY);
    }

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
