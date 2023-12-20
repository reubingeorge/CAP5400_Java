package org.CAP5400.Image;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.CAP5400.Exceptions.*;
import org.CAP5400.Misc.Misc;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

/**
 * This class represents an image object. The image object is a 3D array of pixels. The first dimension represents the
 * rows, the second dimension represents the columns, and the third dimension represents the channels. The image object
 * can be created from a file or from scratch. The image object can be saved to a file.
 * <br><br>
 * <p><b>NOTE: </b> This class uses the OpenCV library to read and write images and deploys the observer design pattern.
 * Every time a pixel value is changed, the observers are notified.</p>
 * @Author: Reubin George
 */
public class Image implements AutoCloseable{

    private String fileName;
    private final int rows, columns, channels;
    private int[][][] pixels;
    private final boolean isColor;
    public static final int MAX_RGB = 255;
    private List<Consumer<Image>> observers = new ArrayList<>(); //Deploy observer design pattern

    /**
     * Overloaded constructor that creates an empty image object based on the rows, columns, and channels
     * @param rows The number of rows in the image
     * @param columns The number of columns in the image
     * @param channels The number of channels in the image
     */
    public Image(@Positive int rows, @Positive int columns, @Positive int channels) {
        if(rows <= 0 || columns <= 0 || channels <= 0) {
            throw new IllegalArgumentException("Invalid image dimensions: " + rows + "x" + columns + "x" + channels);
        }
        this.rows = rows;
        this.columns = columns;
        this.channels = channels;
        this.pixels = new int[rows][columns][channels];
        this.isColor = channels == 3;

    }

    /**
     * Overloaded constructor that creates an empty image object based on the rows and columns. This is used for
     * grayscale images.
     * @param rows The number of rows in the image
     * @param columns The number of columns in the image
     */
    public Image(@Positive int rows, @Positive int columns) {
        this(rows, columns, 1);
    }

    /**
     * Overloaded constructor that creates an empty image object based another image object.
     * @param image The image object to be copied
     */
    public Image(@NotNull Image image){
        this.rows = image.rows;
        this.columns = image.columns;
        this.channels = image.channels;
        this.fileName = image.fileName;
        this.pixels = new int[rows][columns][channels];
        this.isColor = image.isColor;

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                for(int k = 0; k < channels; k++) {
                    this.pixels[i][j][k] = image.pixels[i][j][k];
                }
            }
        }


    }

    /**
     * Overloaded constructor that creates an image object based on the file name. This constructor opens the image file
     * while checking for the correct extension and verifying that the file exists.
     * @param fileName The name of the image file. The file must have a .pgm or .ppm extension.
     * @throws Exception If the file does not exist or the extension is incorrect.
     */
    public Image(@NotNull @NotBlank @NotEmpty String fileName) throws Exception {
        this.fileName = fileName;
        var extension = Misc.getFileExtension(fileName);
        var flag = -1;

        if (extension.equals("pgm")) {
            flag = Imgcodecs.IMREAD_GRAYSCALE;
            this.isColor = false;
        } else if (extension.equals("ppm")) {
            flag = Imgcodecs.IMREAD_COLOR;
            this.isColor = true;
        } else {
            throw new ImageIncorrectExtensionException(extension);
        }

        if(!Misc.doesFileExist(this.fileName)){
            throw new ImageNotFoundException(this.fileName);
        }

        // Load image from resources
        try (InputStream inputStream = Files.newInputStream(Paths.get(fileName))) {
            if (inputStream == null) {
                throw new ImageNotFoundException(fileName);
            }

            // Read image from InputStream
            var imageBytes = inputStream.readAllBytes();
            var image = Imgcodecs.imdecode(new MatOfByte(imageBytes), flag);

            if (image.empty()) {
                throw new ImageNotFoundException(fileName);
            }

            this.rows = image.rows();
            this.columns = image.cols();
            this.channels = image.channels();
            pixels = new int[rows][columns][channels];

            // Convert Mat to pixel array
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    for (int k = 0; k < channels; k++) {
                        pixels[i][j][k] = (int)image.get(i, j)[k]; // convert byte (-127 to 128) to int (0-255)
                    }
                }
            }
        }

    }

    /**
     * This method returns the pixel value at the specified row, column, and channel.
     * @param row The row of the pixel
     * @param column The column of the pixel
     * @param channel The channel of the pixel
     * @return The pixel value at the specified row, column, and channel
     * @throws Exception If the row, column, or channel is out of bounds for the image
     */
    public int getPixel(int row, int column, int channel) throws Exception {
        if(!isInBounds(row, column, channel)) {
            throw new ImageOutOfBoundsException(row, column, channel, rows, columns, channels);
        }
        return pixels[row][column][channel];
    }

    /**
     * This method returns the pixel value at the specified row and column. This method is used for grayscale images.
     * @param row The row of the pixel
     * @param column The column of the pixel
     * @return The pixel value at the specified row and column
     * @throws Exception If the row or column is out of bounds for the image or the image is not grayscale
     */
    public int getPixel(int row, int column) throws Exception {
        if(!isInBounds(row, column)) {
            throw new ImageOutOfBoundsException(row, column, rows, columns);
        }
        if(isColor) {
            throw new Exception("This method is used for grayscale images only.");
        }

        return pixels[row][column][0];
    }

    /**
     * This method sets the pixel value at the specified row, column, and channel.
     * <p><b>NOTE: </b> The observer is notified when this method is called. If more than 50 pixels are changed, it
     * might be better to use the deep copy method.</p>
     * @param row The row of the pixel
     * @param column The column of the pixel
     * @param channel The channel of the pixel
     * @param value The value to be set
     * @throws Exception If the row, column, or channel is out of bounds for the image
     *
     */
    public void setPixel(int row, int column, int channel, int value) throws Exception {
        if(!isInBounds(row, column, channel)) {
            throw new ImageOutOfBoundsException(row, column, channel, rows, columns, channels);
        }
        pixels[row][column][channel] = limitValue(value);
        notifyObserver();

    }

    /**
     * This method sets the pixel value at the specified row and column. This method is used for grayscale images.
     * <p><b>NOTE: </b> The observer is notified when this method is called. If more than 50 pixels are changed, it
     * might be better to use the deep copy method.</p>
     * @param row The row of the pixel
     * @param column The column of the pixel
     * @param value The value to be set
     * @throws Exception If the row or column is out of bounds for the image or the image is not grayscale
     */
    public void setPixel(int row, int column, int value) throws Exception {
        if(!isInBounds(row, column)) {
            throw new ImageOutOfBoundsException(row, column, rows, columns);
        }
        if(isColor) {
            throw new Exception("This method is used for grayscale images only.");
        }
        pixels[row][column][0] = limitValue(value);
        notifyObserver();
    }

    /**
     * This method checks if the specified row, column, and channel is in bounds for the image.
     * @param row The row of the pixel
     * @param column The column of the pixel
     * @param channel The channel of the pixel
     * @return True if the specified row, column, and channel is in bounds for the image, false otherwise
     */
    public boolean isInBounds(int row, int column, int channel) {
        return isInBounds(row, column) && (channel >= 0 && channel < channels);
    }

    /**
     * This method checks if the specified row and column is in bounds for the image.
     * @param row The row of the pixel
     * @param column The column of the pixel
     * @return True if the specified row and column is in bounds for the image, false otherwise
     */
    public boolean isInBounds(int row, int column) {
        return (row >= 0 && row <= rows) && (column >= 0 && column <= columns);
    }

    /**
     * This method returns the pixel array of the image.
     * @param channel The channel of the pixel array
     * @return The pixel array of the image
     */
    public int [] getChannel(int channel) {
        if(channel < 0 || channel >= channels) {
            throw new IllegalArgumentException("Invalid channel index: " + channel +
                    ". The image has " + channels + " channels.");
        }
        var channelPixels = new int[rows * columns];
        for(int i = 0; i < (rows * columns); i++){ channelPixels[i] = 0; }

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                channelPixels[i * columns + j] = pixels[i][j][channel];
            }
        }
        return channelPixels;
    }

    /**
     * This method sets the pixel array of the image.
     * <p><b>NOTE: </b> The observer is notified when this method is called.
     * @param channelData The pixel array to be set
     * @param channel The channel of the pixel array
     */
    public void setChannel(int [] channelData, int channel){
        if(channel < 0 || channel >= channels) {
            throw new IllegalArgumentException("Invalid channel index: " + channel +
                    ". The image has " + channels + " channels.");
        }

        if (channelData.length != rows * columns) {
            throw new IllegalArgumentException("Invalid channel data length: " + channelData.length +
                    ". The image has " + rows * columns + " pixels.");
        }

        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                pixels[i][j][channel] = limitValue(channelData[i * columns + j]);
            }
        }
        notifyObserver();
    }

    /**
     * This method sets a cap of the pixel value at 0 and 255.
     * @param value The pixel value to be capped.
     * @return The capped pixel value. If the value is less than 0, it is set to 0. If the value is greater than 255,
     * it is set to 255.
     */
    protected int limitValue(int value) {
        if(value < 0) {
            return 0;
        }
        else if(value > MAX_RGB) {
            return MAX_RGB;
        }
        return value;
    }

    /**
     * This method saves the image to the specified file name.
     * @param fileName The name of the file to be saved
     */
    public void save(@NotNull @NotBlank @NotEmpty String fileName) throws ImageIncorrectExtensionException {
        var image = getOpenCvMat();
        var extension = Misc.getFileExtension(fileName);
        if(!extension.equals("ppm") && !extension.equals("pgm")){
            throw new ImageIncorrectExtensionException(extension);
        }
        Imgcodecs.imwrite(fileName, image);
    }

    /**
     * This method returns the OpenCV Mat object of the image.
     * @return The OpenCV Mat object of the image
     */
    public Mat getOpenCvMat(){
        var image = new Mat(rows, columns, isColor ? CvType.CV_8UC3 : CvType.CV_8UC1);
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(this.isColor){
                    var channelValues = new double[this.channels];
                    for(int k = 0; k < channels; k++) {
                        channelValues[k] = pixels[i][j][k];
                    }
                    image.put(i, j, channelValues);
                }
                else {
                    image.put(i, j, pixels[i][j][0]);
                }

            }
        }
        return image;
    }

    /**
     * This method returns the OpenCV Mat object of the image based on the specified channel.
     * @param channel The channel of the image
     * @return The OpenCV Mat object of the image based on the specified channel
     */
    public Mat getOpenCvMat(int channel){
        var image = getOpenCvMat();
        var imageChannels = new ArrayList<Mat>();
        Core.split(image, imageChannels);
        if(isColor){ return imageChannels.get(channel); }
        else { return imageChannels.get(0); }
    }

    /**
     * This method returns the OpenCV Mat object of the image based on the specified color space and channel.
     * @param colorSpace The color space of the image. Only "hsv" and "rgb" are supported.
     * @param channel The channel of the image
     * @return The OpenCV Mat object of the image based on the specified color space and channel
     * @throws Exception If the color space is not supported
     */
    public Mat getOpenCvMat(String colorSpace, int channel) throws Exception {
        if(colorSpace.toLowerCase(Locale.US).equals("hsv")){
            var image = getOpenCvMat();
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
            var channels = new ArrayList<Mat>(3);
            Core.split(image, channels);
            return channels.get(channel);

        }
        else if(colorSpace.toLowerCase(Locale.US).equals("rgb")){
            return getOpenCvMat(channel);
        }
        else {throw new IllegalColorspaceException(colorSpace);}

    }

    /**
     * This method is used to check if two image objects are equal.
     * @param obj The object to be compared
     * @return True if the two image objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Image other)) {
            return false;
        }
        if(rows != other.rows || columns != other.columns || channels != other.channels) {
            return false;
        }
        return Arrays.deepEquals(pixels, other.pixels);
    }

    /**
     * This method is used to get the hash code of the image object.
     * @return The hash code of the image object
     */
    @Override
    public int hashCode() {
        return Objects.hash(rows, columns, channels, Arrays.deepHashCode(pixels));
    }

    /**
     * This method is used to get the string representation of the image object.
     * @return The string representation of the image object
     */
    @Override
    public String toString() {
        return "Image{" +
                "fileName= '" + fileName + '\'' +
                ", rows= " + rows +
                ", columns= " + columns +
                ", channels= " + channels +
                '}';
    }

    /**
     * This method is used to get the file name of the image object.
     * @return The file name of the image object
     */
    public String getFileName() {
        if(this.fileName == null || this.fileName.isEmpty() || this.fileName.isBlank()) {
            return "";
        }
        return fileName;
    }

    /**
     * This method is used to get the total number of rows in the image.
     * @return The total number of rows in the image
     */
    public int getRows() {
        return rows;
    }

     /**
     * This method is used to get the total number of columns in the image.
     * @return The total number of columns in the image
     */
    public int getColumns() {
        return columns;
    }

    /**
     * This method is used to get the total number of channels in the image.
     * @return The total number of channels in the image
     */
    public int getNumChannels() {
        return channels;
    }

    /**
     * This method is used to check if the image is a color image.
     * @return True if the image is a color image, false otherwise
     */
    public boolean isColor() {
        return isColor;
    }

    /**
     * Method that performs a deep copy from one image object into the current image.
     * <p><b>NOTE: </b> The observer is notified when this method is called.</p>
     * @param other Image to be copied.
     */
    public void deepCopy(Image other) throws ImageDimensionsNotSameException {
        if(other.rows != this.rows || other.columns != this.columns || other.channels != this.channels){
            throw new ImageDimensionsNotSameException(this, other);
        }

        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.columns; j++){
                for(int k = 0; k < this.channels; k++){
                    this.pixels[i][j][k] = other.pixels[i][j][k];
                }
            }
        }
        notifyObserver();
    }

    /**
     * This method is used to add an observer to the image object.
     * @param observer The observer to be added
     */
    public void addObserver(Consumer<Image> observer) {
        observers.add(observer);
    }


    /**
     * This method is used to remove an observer from the image object.
     * @param observer The observer to be removed
     */
    public void removeObserver(Consumer<Image> observer) {
        observers.remove(observer);
    }

    /**
     * This method is used to notify the observers of the image object. This method is called whenever a pixel value
     * is changed.
     */
    private void notifyObserver() {
        observers.forEach(observer -> observer.accept(this));
    }

    /**
     * This method is used to close the image object.
     */
    @Override
    public void close() {
        for(var o : observers){
            removeObserver(o);
        }
        observers.clear();
        pixels = null;
    }

}
