package org.CAP5400.Toolbox;

import com.github.javaparser.quality.Nullable;
import jakarta.validation.constraints.NotNull;
import org.CAP5400.Exceptions.IllegalColorspaceException;
import org.CAP5400.Image.Image;
import org.CAP5400.Misc.Misc;
import org.CAP5400.RegionOfInterest.ROI;
import org.checkerframework.common.value.qual.IntRange;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

import static org.CAP5400.Image.Image.MAX_RGB;
import static org.CAP5400.Misc.Misc.appendToTrackerFile;
import static org.CAP5400.Misc.Misc.delete;

/**
 * This class is used to perform histogram operations on a region of interest.
 * This class performs the following
 * operations:
 * <ul>
 *     <li>Histogram Equalization</li>
 *     <li>Histogram Stretching</li>
 *     <li>Threshold Equalization</li>
 * </ul>
 * @Author Reubin George
 * @see ROI
 * @see AutoCloseable
 */
public class Histogram implements AutoCloseable{
    private List<Map<Integer, Set<Point>>> data;
    private final String colorspace;
    private final int [] MAX_HSV = {179, MAX_RGB, MAX_RGB};
    private ROI region;

    /**
     * Constructor for the Histogram class.
     * @param region The region of interest.
     * @param colorSpace The color space to use. Can be either "rgb" or "hsv".
     * @throws Exception Exception thrown if an error occurs.
     */
    public Histogram(@NotNull ROI region, @Nullable String colorSpace) throws Exception {
        colorspace = (colorSpace == null || colorSpace.isBlank()) ? "rgb" : colorSpace.toLowerCase(Locale.US);
        if(!colorspace.equals("rgb") && !colorspace.equals("hsv")){
            throw new IllegalColorspaceException(colorSpace);
        }
        this.region = region;
        this.region.getRegionImage().addObserver(o ->{
            try {
                computeBin(); //recompute the bin if the region image changes
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        });
        if(data == null){
            computeBin();
        }


    }

    /**
     * This method is used to compute the bin for the histogram.
     * The bin is a collection of points that have the same
     * intensity value for all channels in the region of interest.
     * @throws Exception Exception thrown if an error occurs.
     */
    private void computeBin() throws Exception {

        this.data = new ArrayList<>();
        for(var i = 0; i < region.getNumChannels(); i++){
            data.add(new HashMap<>());
            var maxIntensity = getMaxChannelValue(i);
            for(var j = 0; j <= maxIntensity; j++){
                this.data.get(i).put(j, new HashSet<>());
            }
        }

        Mat hsvImage = new Mat();
        if(this.colorspace.equals("hsv")){
            Imgproc.cvtColor(region.getRegionImage().getOpenCvMat(), hsvImage, Imgproc.COLOR_BGR2HSV);
        }

        for(int i = 0; i < region.getTotalX(); i++){
            for(int j = 0; j < region.getTotalY(); j++){
                for(int k = 0; k < region.getNumChannels(); k++){
                    var point = new Point(i, j);
                    int intensity;
                    if(colorspace.equals("rgb")){
                        intensity = region.getRegionImage().getPixel(i, j, k);
                    }
                    else {
                        intensity = (int) hsvImage.get(i, j)[k];
                    }
                    this.data.get(k).get(intensity).add(point);
                }
            }
        }
        hsvImage.release();
    }

/**
     * This method is used to save the histogram of a channel.
     * @param channel The channel to save the histogram for.
     * @throws Exception Exception thrown if an error occurs.
     */
    private void saveChannelHistogram(@IntRange(from = 0, to = 2) int channel) throws Exception {
        var histogramImage = new Image(MAX_RGB + 1, MAX_RGB + 1);
        var channelData = new int[MAX_RGB + 1];
        for(int i = 0; i <= getMaxChannelValue(channel); i++){
            var binSize = data.get(channel).get(i).size();
            if(binSize > 0){ channelData[i] = binSize; }
            else { channelData[i] = -1; }
        }

        var maxBinSize = Arrays.stream(channelData).filter(value -> value >= 0).max()
                .orElseThrow(()-> new IllegalStateException("Max bin size could not be found!"));
        var minBinSize = Arrays.stream(channelData).filter(value -> value >= 0).min()
                .orElseThrow(()-> new IllegalStateException("Min bin size could not be found!"));

        for(int j = 0; j <= MAX_RGB; j++){
            var ratio = ((float)(channelData[j] - minBinSize))/((float) (maxBinSize - minBinSize));
            for(int i = 0; i <= MAX_RGB; i++){
                if(i <(MAX_RGB - (MAX_RGB * ratio))){ histogramImage.setPixel(i, j, 0); }
                else { histogramImage.setPixel(i, j, MAX_RGB); }

            }
        }

        var filename = "hist_" + Misc.getCurrentFormattedDateTime() + "_" + Misc.getRandomString(6) + ".pgm";
        histogramImage.save(filename);
        appendToTrackerFile(filename);
    }

    /**
     * This method is used to close the histogram. It clears the data.
     */
    @Override
    public void close() {
        for (var channelBin : data) {
            for (var points : channelBin.values()) {
                points.clear();
            }
            channelBin.clear();
        }
        data.clear();
    }

    /**
     * This method is used to get the maximum possible intensity value for a channel.
     * @param channel The channel to get the maximum possible intensity value for.
     * @return The maximum possible intensity value for a channel.
     * @throws Exception Exception thrown if an error occurs.
     */
    private int getMaxChannelValue(@IntRange(from = 0, to = 2) int channel) throws Exception {
        int maxIntensity;
        if(this.colorspace.equals("rgb")){
            maxIntensity = MAX_RGB;
        } else if (this.colorspace.equals("hsv")) {
            maxIntensity = MAX_HSV[channel];
        }
        else {
            throw new IllegalColorspaceException(this.colorspace);
        }
        return maxIntensity;
    }

    /**
     * This method is used to perform histogram equalization on a channel.
     * @param channel The channel to perform histogram equalization on.
     * @throws Exception Exception thrown if an error occurs.
     */
    public void performEqualization(@IntRange(from = 0, to = 2) int channel) throws Exception {
        saveChannelHistogram(channel);
        var channels = new ArrayList<Mat>();
        var cvRegion = region.getRegionImage().getOpenCvMat();
        if(colorspace.equals("rgb")){
            Core.split(cvRegion, channels);
            var interestedChannel = channels.get(channel);
            Imgproc.equalizeHist(interestedChannel, interestedChannel);
            channels.set(channel, interestedChannel);
            Core.merge(channels, cvRegion);
        }
        else if (colorspace.equals("hsv")) {
            Imgproc.cvtColor(cvRegion, cvRegion, Imgproc.COLOR_BGR2HSV);
            Core.split(cvRegion, channels);
            var interestedChannel = channels.get(channel);
            Imgproc.equalizeHist(interestedChannel, interestedChannel);
            channels.set(channel, interestedChannel);
            Core.merge(channels, cvRegion);
            Imgproc.cvtColor(cvRegion, cvRegion, Imgproc.COLOR_HSV2BGR);
        }
        else{
            throw new IllegalColorspaceException(colorspace);
        }

        var extension = Misc.getFileExtension(region.getSourceImage().getFileName());
        var filename = Misc.getRandomString(10) + "." + extension;
        Imgcodecs.imwrite(filename, cvRegion);
        region.getRegionImage().deepCopy(new Image(filename));
        saveChannelHistogram(channel);
        delete(filename);

        cvRegion.release();
        for(var ch: channels){
            ch.release();
        }
        channels.clear();
    }

    /**
     * This method is used to perform histogram equalization on all channels.
     * @throws Exception Exception thrown if an error occurs.
     */
    public void performEqualization() throws Exception {
        for(int i = 0; i < region.getNumChannels(); i++){
            performEqualization(i);
        }
    }

    /**
     * This method is used to perform histogram stretching on a channel.
     * @param minStretch The minimum stretch value.
     * @param maxStretch The maximum stretch value.
     * @param channel The channel to perform histogram stretching on.
     * @throws Exception Exception thrown if an error occurs.
     */
    public void performStretch(
            @IntRange(from = 0, to = MAX_RGB) int minStretch,
            @IntRange(from = 0, to = MAX_RGB) int maxStretch,
            @IntRange(from = 0, to = 2) int channel) throws Exception {

        var maxChannelValue = getMaxChannelValue(channel);
        if (minStretch < 0 || maxStretch < 0 || minStretch > maxChannelValue || maxStretch > maxChannelValue) {
            throw new IllegalArgumentException("The stretch value must be between 0 - " + maxChannelValue);
        }
        if (channel < 0 || channel > 3) {
            throw new IllegalArgumentException("The channel must be between 0 - 3!");
        }
        if (minStretch >= maxStretch) {
            throw new IllegalArgumentException("The minStretch must be less than the maxStretch value!");
        }

        if(channel > region.getNumChannels()){
            throw new IllegalArgumentException("The channel index entered: " + channel +
                    "exceeds the max number of channels ("+region.getNumChannels()+")");
        }

        saveChannelHistogram(channel);

        var minPercentile = 1.05;
        var maxPercentile = 0.95;

        var minIntensity = -1;
        for (int i = 0; i <= maxChannelValue; i++) {
            if (!data.get(channel).get(i).isEmpty()) {
                minIntensity = i;
                break;
            }
        }

        var maxIntensity = -1;
        for (int i = maxChannelValue; i >= 0; i--) {
            if (!data.get(channel).get(i).isEmpty()) {
                maxIntensity = i;
                break;
            }
        }

        var newMinIntensity = (int) ((float) minIntensity * minPercentile);
        var newMaxIntensity = (int) ((float) maxIntensity * maxPercentile);
        var stretchFactor = ((float) (maxStretch - minStretch)) / ((float) (newMaxIntensity - newMinIntensity));


        Map<Integer, List<Point>> stretchedBin = new HashMap<>();
        for (int i = 0; i <= maxChannelValue; i++) {
            stretchedBin.put(i, new ArrayList<>());
        }
        for (int i = 0; i <= maxChannelValue; i++) {

            if (i >= 0 && i < newMinIntensity) {
                for (Point point : data.get(channel).get(i)) {
                    stretchedBin.get(newMinIntensity).add(point);
                }
            } else if (i >= newMinIntensity && i < newMaxIntensity) {
                var j = (int) (((float) (i - newMinIntensity)) * stretchFactor + ((float) minStretch));
                for (var point : data.get(channel).get(i)) {
                    stretchedBin.get(j).add(point);
                }
            } else {
                for (var point : data.get(channel).get(i)) {
                    stretchedBin.get(newMaxIntensity).add(point);
                }
            }

        }

        var image = new Image(region.getRegionImage());

        for(int i = 0; i <= maxChannelValue; i++){
            for(var point : stretchedBin.get(i)){
                var x = point.x;
                var y = point.y;
                image.setPixel(x, y, channel, i);
            }
        }

        region.getRegionImage().deepCopy(image);


        saveChannelHistogram(channel);

    }

    /**
     * This method is used to perform histogram stretching on all channels.
     * @param minStretch The minimum stretch value.
     * @param maxStretch The maximum stretch value.
     * @throws Exception Exception thrown if an error occurs.
     */
    public void performStretch(
            @IntRange(from = 0, to = MAX_RGB) int minStretch,
            @IntRange(from = 0, to = MAX_RGB) int maxStretch) throws Exception {
        for(int i = 0; i < region.getNumChannels(); i++){
            performStretch(minStretch, maxStretch, i);
        }
    }

    /**
     * This method is used to perform threshold equalization on a channel.
     * The method extracts all the pixel points with
     * an intensity value less than the threshold value and performs histogram equalization just on them.
     * @param threshold The threshold value.
     * @param channelIndex The channel to perform threshold equalization on.
     * @throws Exception Exception thrown if an error occurs.
     */
    public void performThresholdEqualization(
            @IntRange(from = 0, to = MAX_RGB) int threshold,
            @IntRange(from = 0, to = 2) int channelIndex) throws Exception {
        saveChannelHistogram(channelIndex);
        var cvRegion = region.getRegionImage().getOpenCvMat(channelIndex);
        var darkLocation = new ArrayList<Point>();
        for(int i = 0; i < region.getTotalX(); i++){
            for(int j = 0; j < region.getTotalY(); j++){
                var intensity = cvRegion.get(i, j)[0];
                if(intensity < threshold){ darkLocation.add(new Point(i, j)); }
            }
        }
        var cvDarkRegion = new Mat(darkLocation.size(), 1, CvType.CV_8UC1);
        for(int i = 0; i < darkLocation.size(); i++){
            var x = darkLocation.get(i).x;
            var y = darkLocation.get(i).y;
            cvDarkRegion.put(i, 0, cvRegion.get(x, y)[0]);
        }
        Imgproc.equalizeHist(cvDarkRegion, cvDarkRegion);

        for(int i = 0; i < darkLocation.size(); i++){
            var x = darkLocation.get(i).x;
            var y = darkLocation.get(i).y;
            var newValue = cvDarkRegion.get(i, 0)[0];
            cvRegion.put(x, y, newValue);
        }

        var image = region.getRegionImage().getOpenCvMat();
        var imageChannels = new ArrayList<Mat>();
        Core.split(image, imageChannels);
        imageChannels.set(channelIndex, cvRegion);
        Core.merge(imageChannels, image);

        var extension = Misc.getFileExtension(region.getSourceImage().getFileName());
        var filename = Misc.getRandomString(10) + "." + extension;
        Imgcodecs.imwrite(filename, image);

        region.getRegionImage().deepCopy(new Image(filename));
        saveChannelHistogram(channelIndex);

        delete(filename);
        image.release();
        for(int i = 0; i < imageChannels.size(); i++){
            imageChannels.get(i).release();
        }
        imageChannels.clear();
        cvDarkRegion.release();
        cvRegion.release();
    }

}
