package org.CAP5400.Toolbox;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.CAP5400.Image.Image;
import org.CAP5400.Misc.FloatRange;
import org.CAP5400.RegionOfInterest.ROI;
import org.checkerframework.common.value.qual.IntRange;

import static org.CAP5400.Image.Image.MAX_RGB;

/**
 * This class contains a set of static methods that can be used to perform operations on a region of interest (ROI)
 * within an image.
 * @see ROI
 * @author Reubin George
 * @version 1.0
 */
public class Toolbox {
    /**
     * <p>This method increases the intensity of all pixels in the given region of interest.</p>
     * <p><b>NOTE:</b> This method works only on grayscale images. </p>
     * @param region Region of interest on which this filter will be applied.
     * @param value the value by which the pixel intensity will be increased.
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void addGrey(ROI region, int value) throws Exception {
        if(region.getSourceImage().isColor()){
            throw new IllegalArgumentException("This filter only works on greyscale images.");
        }
        for(int i = 0; i < region.getTotalX(); i++) {
            for(int j = 0; j < region.getTotalY(); j++) {
                var newX = region.getStartX() + i;
                var newY = region.getStartY() + j;
                var pixel = region.getSourceImage().getPixel(newX, newY) + value;
                region.getRegionImage().setPixel(i, j, pixel);
            }
        }
    }

    /**
     * <p>This method increase the intensity of all pixel in all available channels in the given region of interest.</p>
     * @param region Region of interest on which this filter will be applied.
     * @param value the value by which the pixel intensity will be increased. The value can be in range (-50 to 50).
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void addBrightness(@NotNull ROI region, @IntRange(from = -50, to = 50) int value) throws Exception {
        if(value > -50 && value < 50) {
            for(int i = 0; i < region.getTotalX(); i++) {
                for(int j = 0; j < region.getTotalY(); j++) {
                    for(int k = 0; k < region.getNumChannels(); k++){
                        var sourceX = i + region.getStartX();
                        var sourceY = j + region.getStartY();
                        var oldValue = region.getSourceImage().getPixel(sourceX, sourceY, k);
                        var newValue = oldValue + value;
                        region.getRegionImage().setPixel(i, j, k, newValue);
                    }
                }
            }
        }
    }

    /**
     * This method takes a region of interest within an image and adjusts the brightness of pixels based on a given
     * threshold.
     * @param region Region of interest on which this filter will be applied.
     * @param threshold The threshold intensity value. If the pixel intensity in the source image is below this
     *                  threshold, brightness will be decreased.
     * @param value The value by which to decrease the brightness of pixels below the threshold.
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void decreaseBrightness(
            @NotNull ROI region,
            @IntRange(from = 0, to = MAX_RGB) int threshold,
            @IntRange(from = 0, to = MAX_RGB) int value) throws Exception {

        if(threshold < 0 || threshold > MAX_RGB){
            throw new IllegalArgumentException("Threshold needs to be capped between 0 to 255");
        }

        for(int i = 0; i < region.getTotalX(); i++) {
            for (int j = 0; j < region.getTotalY(); j++) {
                for (int k = 0; k < region.getNumChannels(); k++) {
                    var sourceX = i + region.getStartX();
                    var sourceY = j + region.getStartY();
                    var intensity = region.getSourceImage().getPixel(sourceX, sourceY, k);
                    if (intensity < threshold) {
                        var newValue = intensity - value;
                        region.getRegionImage().setPixel(i, j, k, newValue);
                    }
                }
            }
        }
    }


    /**
     * This method binarizes a region of interest (ROI) within an image based on a specified threshold.
     * @param region Region of interest on which this filter will be applied.
     * @param threshold The intensity threshold for binarization (0 to 255).
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void binarize(
            @NotNull ROI region,
            @IntRange(from = 0, to = MAX_RGB) int threshold) throws Exception {
        if(region.getSourceImage().isColor()){
            throw new IllegalArgumentException("This filter only works on greyscale images.");
        }
        if(threshold < 0 || threshold > MAX_RGB){
            throw new IllegalArgumentException("Threshold needs to be capped between 0 to 255");
        }
        for(int i = 0; i < region.getTotalX(); i++) {
            for (int j = 0; j < region.getTotalY(); j++) {
                var sourceX = i + region.getStartX();
                var sourceY = j + region.getStartY();
                var originalIntensity = region.getSourceImage().getPixel(sourceX, sourceY);
                var newIntensity = originalIntensity < threshold ? 0 : MAX_RGB;
                region.getRegionImage().setPixel(i, j, newIntensity);
            }
        }
    }

    /**
     * This method scales a region of interest (ROI) within an image by a specified ratio.
     * @param region Region of interest on which this filter will be applied.
     * @param ratio The scaling ratio (1.0 to 2.0).
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void scale(
            @NotNull ROI region,
            @FloatRange(from = 1.0f, to = 2.0f) float ratio) throws Exception {
        if(ratio < 1.0 || ratio > 2.0){
            throw new IllegalArgumentException("The scaling factor needs to be between 1 - 2");
        }
        var col = region.getTotalY();
        var row = region.getTotalX();

        var scaledRow = (int)((float)row * ratio);
        var scaledCol = (int)((float)col * ratio);
        var scaledImage = new Image(scaledRow, scaledCol, region.getNumChannels());

        for(int i = 0; i < scaledRow; i++){
            for(int j = 0; j < scaledCol; j++){
                for(int k = 0; k < region.getNumChannels(); k++){
                    var newX = (int)((float)i / ratio);
                    var newY = (int)((float)j / ratio);
                    var intensity = region.getRegionImage().getPixel(newX, newY, k);
                    scaledImage.setPixel(i, j, k, intensity);
                }
            }
        }

        region.getRegionImage().deepCopy(scaledImage);

    }

    /**
     * This method rotates a region of interest (ROI) within an image clockwise by 90 degrees.
     * @param region Region of interest on which this filter will be applied.
     * @throws Exception Thrown error if any problems are detected!
     */
    protected static void rotateClockwise90(@NotNull ROI region) throws Exception{
        region.enforceSquareDimensions(true);
        var rotatedImage = new Image(region.getTotalX(), region.getTotalY(), region.getNumChannels());

        for(int i = 0; i < region.getTotalX(); i++){
            for(int j = 0; j < region.getTotalY(); j++){
                for(int k = 0; k < region.getNumChannels(); k++){
                    var newY =  region.getTotalX() - i - 1;
                    var intensity = region.getRegionImage().getPixel(i, j, k);
                    rotatedImage.setPixel(j, newY, k, intensity);
                }
            }
        }

        region.getRegionImage().deepCopy(rotatedImage);
    }

    /**
     * This method rotates a region of interest (ROI) within an image by a specified angle.
     * @param region Region of interest on which this filter will be applied.
     * @param angle The angle by which to rotate the region (must be a positive multiple of 90 degrees).
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void rotate(@NotNull ROI region, int angle) throws Exception {
        var baseAngle = 90;
        if(angle <= 0){
            throw new IllegalArgumentException("Angle must be positive.");
        }

        if(angle % baseAngle != 0){
            throw new IllegalArgumentException("Angle must be a multiple of 90.");
        }

        var numRotations = angle / baseAngle;
        for(int i = 0; i < numRotations; i++){
            rotateClockwise90(region);
        }
    }

    /**
     * This method applies histogram equalization to a region of interest (ROI) within an image. The equalization is
     * performed separately on each channel for color images.
     * @param region Region of interest on which this filter will be applied.
     * @param colorSpace The color space in which to perform histogram equalization. It can be either "RGB" or "HSV".
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void histogramEqualizationAll(@NotNull ROI region,
                                                @NotNull @NotBlank @NotEmpty String colorSpace) throws Exception {
        var histogram = new Histogram(region, colorSpace);
        histogram.performEqualization();
        histogram.close();
    }

    /**
     * This method applies histogram equalization to a region of interest (ROI) within an image based on the specified
     * color space.
     * @param region Region of interest on which this filter will be applied.
     * @param colorSpace The color space in which to perform histogram equalization. It can be either "RGB" or "HSV".
     * @param channelIndex The index of the channel to equalize.
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void histogramEqualization(
            @NotNull ROI region,
            @NotNull @NotBlank @NotEmpty String colorSpace,
            @IntRange(from = 0, to = 2) int channelIndex) throws Exception{
        var histogram = new Histogram(region, colorSpace);
        histogram.performEqualization(channelIndex);
        histogram.close();
    }

    /**
     * This method applies threshold histogram equalization to a specified channel within a region of interest (ROI)
     * within an image. The method enhances the histogram of dark pixels (below the threshold) in the specified channel.
     * @param region Region of interest on which this filter will be applied.
     * @param threshold The intensity threshold below which pixel values are considered dark.
     * @param channelIndex The index of the channel to perform threshold histogram equalization.
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void thresholdHistogramEqualization(
            @NotNull ROI region,
            @IntRange(from = 0, to = MAX_RGB) int threshold,
            @IntRange(from = 0, to = 2) int channelIndex) throws Exception {

        var histogram = new Histogram(region, "rgb");
        histogram.performThresholdEqualization(threshold, channelIndex);
        histogram.close();
    }

    /**
     * This method applies histogram stretching to a specific channel within a region of interest (ROI). The stretched
     * histogram is based on the specified minimum and maximum intensity values. The stretched values are applied to the
     * pixels in the specified channel, and the resulting image is saved with an updated histogram.
     * @param region Region of interest on which this filter will be applied.
     * @param minStretch The minimum intensity value for histogram stretching (0-255).
     * @param maxStretch The maximum intensity value for histogram stretching (0-255).
     * @param channel The channel for which the histogram stretching is applied (0-2).
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void histogramStretch(
            @NotNull ROI region,
            @IntRange(from = 0, to = MAX_RGB) int minStretch,
            @IntRange(from = 0, to = MAX_RGB) int maxStretch,
            @IntRange(from = 0, to = 2) int channel) throws Exception {
        var histogram = new Histogram(region, "rgb");
        histogram.performStretch(minStretch, maxStretch, channel);
        histogram.close();
    }

    /**
     * This method applies histogram stretching to all channels within a region of interest (ROI). The stretched
     * histogram is based on the specified minimum and maximum intensity values. The stretched values are applied to
     * each channel independently, and the resulting image is saved with updated histograms for all channels.
     * @param region Region of interest on which this filter will be applied.
     * @param minStretch The minimum intensity value for histogram stretching (0-255).
     * @param maxStretch The maximum intensity value for histogram stretching (0-255).
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void histogramStretchAll(
            @NotNull ROI region,
            @IntRange(from = 0, to = MAX_RGB) int minStretch,
            @IntRange(from = 0, to = MAX_RGB) int maxStretch) throws Exception{
        var histogram = new Histogram(region, "rgb");
        histogram.performStretch(minStretch, maxStretch);
        histogram.close();
    }

    /**
     * This method applies a low-pass filter to a region of interest (ROI) within an image. The filter is applied to
     * each channel independently for color images.
     * @param region Region of interest on which this filter will be applied.
     * @param colorspace The color space in which to perform the low-pass filter. It can be either "RGB" or "HSV".
     * @param channel The index of the channel to which the low-pass filter is applied.
     * @param filterRadius The radius of the low-pass filter.
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void lowPassFilter(
            @NotNull ROI region,
            @NotNull @NotBlank String colorspace,
            @IntRange(from = 0, to = 2) int channel,
            @Positive int filterRadius) throws Exception {
        try(var fourier = new Fourier(region, colorspace, channel)){
            fourier.applyFilter(filterRadius, 0, "Low Pass");
        }
    }

    /**
     * This method applies a high-pass filter to a region of interest (ROI) within an image. The filter is applied to
     * each channel independently for color images.
     * @param region Region of interest on which this filter will be applied.
     * @param colorspace The color space in which to perform the high-pass filter. It can be either "RGB" or "HSV".
     * @param channel The index of the channel to which the high-pass filter is applied.
     * @param filterRadius The radius of the high-pass filter.
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void highPassFilter(
            @NotNull ROI region,
            @NotNull @NotBlank String colorspace,
            @IntRange(from = 0, to = 2) int channel,
            @Positive int filterRadius) throws Exception {
        try(var fourier = new Fourier(region, colorspace, channel)){
            fourier.applyFilter(filterRadius, 0, "High Pass");
        }
    }

    /**
     * This method applies a band-pass filter to a region of interest (ROI) within an image. The filter is applied to
     * each channel independently for color images.
     * @param region Region of interest on which this filter will be applied.
     * @param colorspace The color space in which to perform the band-pass filter. It can be either "RGB" or "HSV".
     * @param channel The index of the channel to which the band-pass filter is applied.
     * @param innerFilterRadius The radius of the inner band-pass filter.
     * @param outerFilterRadius The radius of the outer band-pass filter.
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void bandStopFilter(
            @NotNull ROI region,
            @NotNull @NotBlank String colorspace,
            @IntRange(from = 0, to = 2) int channel,
            @Positive int innerFilterRadius, @Positive int outerFilterRadius) throws Exception {
        if(outerFilterRadius <= innerFilterRadius){
            throw new Exception("The outer filter (" + outerFilterRadius +
                    ") must be greater than the inner filter (" + innerFilterRadius + ")");
        }
        try(var fourier = new Fourier(region, colorspace, channel))
        {
            fourier.applyFilter(innerFilterRadius, outerFilterRadius, "Band Stop");
        }
    }

    /**
     * This method sharpens the edges of a region of interest (ROI) within an image. The sharpening is performed
     * independently on each channel for color images using an unsharp mask.
     * @param region Region of interest on which this filter will be applied.
     * @param colorspace The color space in which to perform the sharpening. It can be either "RGB" or "HSV".
     * @param channel The index of the channel to which the sharpening is applied.
     * @param filterRadius The size of the filter used to perform the sharpening.
     * @param sharpeningFactor The sharpening factor (0.0 to 1.0).
     * @throws Exception Thrown error if any problems are detected!
     */
    public static void sharpenEdge(@NotNull ROI region,
                                   @NotNull @NotBlank String colorspace,
                                   @IntRange(from = 0, to = 2) int channel,
                                   @Positive int filterRadius, double sharpeningFactor) throws Exception {
        try(var fourier = new Fourier(region, colorspace, channel)){
            fourier.sharpenEdges(filterRadius, sharpeningFactor);
        }
    }

}


