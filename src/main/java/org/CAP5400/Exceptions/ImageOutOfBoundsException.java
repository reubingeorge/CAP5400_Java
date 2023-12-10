package org.CAP5400.Exceptions;

/**
 * This exception is thrown when an image cannot be found/opened.
 * @Author Reubin George
 */
public class ImageOutOfBoundsException extends Exception{
    /**
     * Constructor for ImageOutOfBoundsException class.
     * @param currentRow current row
     * @param currentColumn current column
     * @param maxRow max row
     * @param maxColumn max column
     */
    public ImageOutOfBoundsException(int currentRow, int currentColumn, int maxRow, int maxColumn) {
        super("Pixel index: (" + currentRow + ", " + currentColumn +") is out of bounds. The image has size: (" +
                maxRow + "x" + maxColumn + ").");
    }

    /**
     * Constructor for ImageOutOfBoundsException class.
     * @param currentRow current row
     * @param currentColumn current column
     * @param currentChannel current channel
     * @param maxRow max row
     * @param maxColumn max column
     * @param maxChannel max channel
     */
    public ImageOutOfBoundsException(int currentRow, int currentColumn, int currentChannel, int maxRow, int maxColumn, int maxChannel) {
        super("Pixel index: (" + currentRow + ", " + currentColumn + ", " + currentChannel +") is out of bounds. " +
                "The image has size: (" + maxRow + "x" + maxColumn + "x" + maxChannel +").");
    }
}
