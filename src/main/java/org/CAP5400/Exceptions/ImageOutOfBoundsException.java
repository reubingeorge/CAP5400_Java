package org.CAP5400.Exceptions;

public class ImageOutOfBoundsException extends Exception{
    public ImageOutOfBoundsException(int currentRow, int currentColumn, int maxRow, int maxColumn) {
        super("Pixel index: (" + currentRow + ", " + currentColumn +") is out of bounds. The image has size: (" +
                maxRow + "x" + maxColumn + ").");
    }
    public ImageOutOfBoundsException(int currentRow, int currentColumn, int currentChannel, int maxRow, int maxColumn, int maxChannel) {
        super("Pixel index: (" + currentRow + ", " + currentColumn + ", " + currentChannel +") is out of bounds. " +
                "The image has size: (" + maxRow + "x" + maxColumn + "x" + maxChannel +").");
    }
}
