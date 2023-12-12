package org.CAP5400.Toolbox;

import jakarta.validation.constraints.NotNull;
import org.CAP5400.Exceptions.IllegalColorspaceException;
import org.CAP5400.Image.Image;
import org.CAP5400.Misc.Misc;
import org.CAP5400.Misc.StringOptions;
import org.CAP5400.RegionOfInterest.ROI;
import org.checkerframework.common.value.qual.IntRange;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.CAP5400.Image.Image.MAX_RGB;
import static org.CAP5400.Misc.Misc.delete;

/**
 * This class is used to perform Fourier Transform operations on an image. The operations that can be performed are:
 * <ul> <li>Low Pass Filter</li> <li>High Pass Filter</li> <li>Band Stop Filter</li> <li>Unsharp Masking</li> </ul>
 * The class is used to perform the operations on a given region of interest. The user specifies the region of interest.
 * The user can also specify the color space and the channel on which the operations will be performed.
 * @see ROI
 * @author Reubin George
 * @version 1.0
 */
public class Fourier implements AutoCloseable{

    private final Mat operationChannel;
    private final String colorSpace;
    private final int [] MAX_HSV = {179, MAX_RGB, MAX_RGB};
    private final int channel;
    private final ROI region;

    /**
     * Default constructor
     * @param region Region of Interest
     * @param colorSpace the color space on which the filters/masks will be applied
     * @param channel the image channel on which the filter/masks will be applied
     * @throws Exception Exception is an error is produced
     */
    public Fourier(
            @NotNull ROI region,
            @NotNull String colorSpace,
            @IntRange(from = 0, to = 2) int channel) throws Exception {
        this.colorSpace = colorSpace.toLowerCase(Locale.US);
        if(!(this.colorSpace.equals("hsv") || this.colorSpace.equals("rgb"))){
            throw new IllegalColorspaceException(colorSpace);
        }

        region.enforceBaseTwoDimensions(true);
        region.enforceSquareDimensions(true);
        this.operationChannel = region.getRegionImage().getOpenCvMat(colorSpace, channel);
        this.channel = channel;
        this.region = region;
    }

    /**
     * The method is used to perform the Discrete Fourier Transform on an image.
     * @param image image on which the DFT will be performed. This is an OpenCV Mat object.
     * @return the DFT of the image
     */
    private Mat performDFT(Mat image){
        Mat paddedImage = new Mat();
        var m = Core.getOptimalDFTSize(image.rows());
        var n = Core.getOptimalDFTSize(image.cols());
        Core.copyMakeBorder(image, paddedImage, 0, m - image.rows(), 0, n - image.cols(),
                Core.BORDER_CONSTANT, Scalar.all(0));
        var planes = new ArrayList<Mat>();
        paddedImage.convertTo(paddedImage, CvType.CV_32F);
        planes.add(paddedImage);
        planes.add(Mat.zeros(paddedImage.size(), CvType.CV_32F));
        var complexImage = new Mat();
        Core.merge(planes, complexImage);
        Core.dft(complexImage, complexImage);
        return complexImage;
    }

    /**
     * Method that perform a shifting operation on a DFT array. We want to ensure that the most frequent frequencies
     * are clustered at the center of the image
     * @param image Mat file containing the DFT
     */
    private void performShift(Mat image){
        image = new Mat(image, new Rect(0, 0, image.cols() & - 2, image.rows() & -2));

        int cx = image.cols() / 2;
        int cy = image.rows() / 2;

        var q0 = new Mat(image, new Rect(0, 0, cx, cy));
        var q1 = new Mat(image, new Rect(cx, 0, cx, cy));
        var q2 = new Mat(image, new Rect(0, cy, cx, cy));
        var q3 = new Mat(image, new Rect(cx, cy, cx, cy));

        var tmp = new Mat();
        q0.copyTo(tmp);
        q3.copyTo(q0);
        tmp.copyTo(q3);

        q1.copyTo(tmp);
        q2.copyTo(q1);
        tmp.copyTo(q2);
    }

    /**
     * Method that saves the magnitude spectrum of a DFT image
     * @param dftImage Mat file containing the DFT
     * @param performShift boolean value that indicates if the shifting operation should be performed
     * @throws Exception Exception is an error is produced
     */
    private void saveMagnitudeSpectrum(Mat dftImage, boolean performShift) throws Exception {
        var planes = new ArrayList<Mat>(2);
        Core.split(dftImage, planes);

        var magnitudeImage = new Mat();
        Core.magnitude(planes.get(0), planes.get(1), magnitudeImage);

        Core.add(magnitudeImage, Scalar.all(1), magnitudeImage);
        Core.log(magnitudeImage, magnitudeImage);

        if(performShift){ performShift(magnitudeImage); }

        Core.normalize(magnitudeImage, magnitudeImage, 0, getMaxChannelValue(), Core.NORM_MINMAX);

        var filename = "mag_" + Misc.getCurrentFormattedDateTime() + "_" + Misc.getRandomString(6) + ".pgm";
        Imgcodecs.imwrite(filename, magnitudeImage);
        Misc.appendToTrackerFile(filename);

        for(var plane : planes){
            plane.release();
        }
        magnitudeImage.release();
    }

    /**
     * Method that returns the maximum possible value for a given channel in a given color space.
     * @return the maximum possible value for a given channel in a given color space.
     */
    private int getMaxChannelValue(){
        if(colorSpace.equals("rbg")){
            return MAX_RGB;
        }
        else{
            return MAX_HSV[this.channel];
        }
    }

    /**
     * Method that returns a band stop filter
     * @param innerRadius inner radius of the filter
     * @param outerRadius outer radius of the filter
     * @param dftArray Mat file containing the DFT
     * @return the band stop filter
     */
    private Mat getBandStopFilter(int innerRadius, int outerRadius, Mat dftArray){
        int valueInCircle = 1, valueOutCircle = 0;
        Point center = new Point((double) dftArray.rows() /2, (double) dftArray.cols() /2);
        Mat filter = new Mat(dftArray.rows(), dftArray.cols(), CvType.CV_32F);
        for(int i = 0; i < dftArray.rows(); i++){
            for(int j = 0; j < dftArray.cols(); j++){
                var currentRadius = getRadius(new Point(i, j), center);
                if(currentRadius < (double) innerRadius || currentRadius > (double) outerRadius) {
                    filter.put(i, j, (float) valueOutCircle);
                }
                else {
                    filter.put(i, j, (float) valueInCircle);
                }
            }
        }
        return filter;
    }

    /**
     * Method that returns a pass filter (low pass or high pass)
     * @param filterRadius radius of the filter
     * @param dftArray Mat file containing the DFT
     * @param valueInCircle value inside the circle
     * @param valueOutCircle value outside the circle
     * @return the pass filter
     */
    private Mat getPassFilter(int filterRadius, Mat dftArray, int valueInCircle, int valueOutCircle){
        Point center = new Point((double) dftArray.rows() /2, (double) dftArray.cols() /2);
        Mat filter = new Mat(dftArray.rows(), dftArray.cols(), CvType.CV_32F);
        for(int i = 0; i < dftArray.rows(); i++) {
            for (int j = 0; j < dftArray.cols(); j++) {
                var currentRadius = getRadius(new Point(i, j), center);
                if(currentRadius > (double) filterRadius){
                    filter.put(i, j, (float) valueOutCircle);
                }
                else{
                    filter.put(i, j, (float) valueInCircle);
                }
            }
        }
        return filter;
    }

    /**
     * Method that returns a low-pass filter
     * @param filterRadius radius of the filter
     * @param dftArray Mat file containing the DFT
     * @return the low-pass filter
     */
    private Mat getLowPassFilter(int filterRadius, Mat dftArray){
        return getPassFilter(filterRadius, dftArray, 1, 0);
    }

    /**
     * Method that returns a high-pass filter
     * @param filterRadius radius of the filter
     * @param dftArray Mat file containing the DFT
     * @return the high-pass filter
     */
    private Mat getHighPassFilter(int filterRadius, Mat dftArray){
        return getPassFilter(filterRadius, dftArray, 0, 1);
    }

    /**
     * Method that returns the radius of a circle
     * @param currentPoint current point
     * @param circleCenter center of the circle
     * @return the radius of a circle
     */
    private double getRadius(@NotNull Point currentPoint, @NotNull Point circleCenter){
        return Math.sqrt(
                Math.pow((currentPoint.x - circleCenter.x), 2.0) +
                Math.pow((currentPoint.y - circleCenter.y), 2.0));
    }

    /**
     * Method that applies a filter to an image
     * @param distance1 distance 1 of the filter
     * @param distance2 distance 2 of the filter (might be optional depending on the filter)
     * @param filterType type of filter. It must be one of the following: "Low Pass", "High Pass", "Band Stop"
     * @throws Exception Exception is an error is produced
     */
    public void applyFilter(
            @IntRange(from = 0 , to = Integer.MAX_VALUE) int distance1,
            @IntRange(from = 0, to = Integer.MAX_VALUE) int distance2,
            @StringOptions({"Low Pass", "High Pass", "Band Stop"}) String filterType) throws Exception {
        Mat resultantDFT = performDFT(this.operationChannel);
        saveMagnitudeSpectrum(resultantDFT, true);
        Mat dftClone = resultantDFT.clone();
        Mat filter = switch (filterType) {
            case "Low Pass" -> getLowPassFilter(distance1, dftClone);
            case "High Pass" -> getHighPassFilter(distance1, dftClone);
            case "Band Stop" -> getBandStopFilter(distance1, distance2, dftClone);
            default -> throw new IllegalArgumentException("Unknown filter: `" + filterType + "` has been detected!");
        };

        var mergeMat = List.of(filter, filter);
        Core.merge(mergeMat, dftClone);
        performShift(resultantDFT);
        Core.mulSpectrums(resultantDFT, dftClone, resultantDFT, 0);
        saveMagnitudeSpectrum(resultantDFT, false);
        performShift(resultantDFT);
        Core.idft(resultantDFT, resultantDFT);

        applyChanges(resultantDFT);

        //Release all Mat objects
        resultantDFT.release();
        dftClone.release();
        filter.release();
        for(var mat : mergeMat){
            mat.release();
        }
    }

    /**
     * Method that sharpens the edges of an image using unsharp masking.
     * @param distance distance of the filter
     * @param factor factor of the filter
     * @throws Exception Exception is an error is produced
     */
    public void sharpenEdges(
            @NotNull @IntRange(from = 0, to = Integer.MAX_VALUE) int distance,
            @NotNull double factor) throws Exception {
        var resultantDFT = performDFT(this.operationChannel);
        saveMagnitudeSpectrum(resultantDFT, true);
        var lowPassFilter = getLowPassFilter(distance, resultantDFT);
        var highPassFilter = getHighPassFilter(distance, resultantDFT);
        var lowPassImage = getFilteredImage(lowPassFilter, resultantDFT);
        var highPassImage = getFilteredImage(highPassFilter, resultantDFT);

        Core.multiply(highPassImage, Scalar.all(1.0 + factor), highPassImage);
        Core.add(lowPassImage, highPassImage, resultantDFT);
        saveMagnitudeSpectrum(resultantDFT, false);
        performShift(resultantDFT);
        Core.idft(resultantDFT, resultantDFT);

        applyChanges(resultantDFT);

        resultantDFT.release();
        lowPassFilter.release();
        lowPassImage.release();
        highPassImage.release();
        highPassFilter.release();
    }

    /**
     * Method that returns the filtered image
     * @param filter filter to be applied
     * @param dftImage Mat file containing the DFT
     * @return the filtered image
     */
    private Mat getFilteredImage(Mat filter, Mat dftImage){
        var dftClone = dftImage.clone();
        var mergedFilter = new Mat();
        var mergeMat = List.of(filter, filter);
        Core.merge(mergeMat, mergedFilter);
        performShift(dftClone);
        Core.mulSpectrums(dftClone, mergedFilter, dftClone, 0);
        return dftClone;
    }

    /**
     * Method that applies the changes to the image.
     * @param inverseDFT Mat file containing the inverse DFT
     * @throws Exception Exception is an error is produced
     */
    private void applyChanges(Mat inverseDFT) throws Exception {
        var planes = new ArrayList<Mat>(2);
        Core.split(inverseDFT, planes);
        Core.normalize(planes.get(0), operationChannel, 0, getMaxChannelValue(), Core.NORM_MINMAX, CvType.CV_8U);

        var originalImageSize = region.getRegionImage().getChannel(this.channel).length;
        var processedImageSize = this.operationChannel.total();

        if(originalImageSize != processedImageSize){
            throw new Exception("The original image has size: " + originalImageSize +
                    " while the processed image has size: " + processedImageSize);
        }


        if(colorSpace.equals("rgb")){
            for(int i = 0; i < region.getTotalX(); i++){
                for(int j = 0; j < region.getTotalY(); j++){
                    var newValue = (int)operationChannel.get(i, j)[0];
                    region.getRegionImage().setPixel(i, j, channel, newValue);
                }
            }
        }

        else if(colorSpace.equals("hsv")){
            var extension = Misc.getFileExtension(region.getSourceImage().getFileName());
            var filename = Misc.getRandomString(10) + "." + extension;
            region.getRegionImage().save(filename);
            var rgbImage = Imgcodecs.imread(filename);
            var hsvImage = new Mat();
            var updatedRgbImage = new Mat();
            Imgproc.cvtColor(rgbImage, hsvImage, Imgproc.COLOR_BGR2HSV);
            var hsvChannels = new ArrayList<Mat>(region.getNumChannels());
            Core.split(hsvImage, hsvChannels);
            hsvChannels.set(channel, operationChannel);
            Core.merge(hsvChannels, hsvImage);
            Imgproc.cvtColor(hsvImage, updatedRgbImage, Imgproc.COLOR_HSV2BGR);
            if(updatedRgbImage.rows() != region.getTotalX() || updatedRgbImage.cols() != region.getTotalY()){
                throw new Exception("Error during HSV to RGB conversion!");
            }
            Imgcodecs.imwrite(filename, updatedRgbImage);
            region.getRegionImage().deepCopy(new Image(filename));

            //Release allocated resources
            delete(filename);
            rgbImage.release();
            hsvImage.release();
            updatedRgbImage.release();
            for(var ch: hsvChannels){
                ch.release();
            }
        }
    }

    @Override
    public void close() {
        this.operationChannel.release();
    }
}
