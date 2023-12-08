package org.CAP5400.Toolbox;

/*import jakarta.validation.constraints.NotNull;
import org.CAP5400.Exceptions.IllegalColorspaceException;
import org.CAP5400.Image.Image;
import org.CAP5400.RegionOfInterest.ROI;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.checkerframework.common.value.qual.IntRange;
import org.opencv.core.Mat;

import java.util.Locale;

public class Fourier {

    private final Mat operationChannel;
    private final String colorSpace;
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

    }

    private Mat performDFT(Mat image){
        Mat paddedImage = new Mat();
        var m = opencv_core.getOptimalDFTSize(image.rows());
        var n = opencv_core.getOptimalDFTSize(image.cols());
        opencv_core.copyMakeBorder(image, paddedImage, 0, m - image.rows(), 0, n - image.cols(),
                opencv_core.BORDER_CONSTANT, Scalar.all(0));
        Mat[] planes = {
                new Mat(paddedImage),
                new Mat(Mat.zeros(paddedImage.size(), opencv_core.CV_32F)) // Explicit conversion to Mat
        };

        var complexImage = new Mat();
        opencv_core.merge(new MatVector(planes), complexImage);
        opencv_core.dft(complexImage, complexImage);
        return complexImage;
    }

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

    private void saveMagnitudeSpectrum(Mat dftImage, boolean performShift){
        var planes = new MatVector();
        opencv_core.split(dftImage, planes);
        opencv_core.magnitude(planes.get(0), planes.get(1), planes.get(0));
        var magnitudePlane = planes.get(0);

        opencv_core.add(magnitudePlane, Scalar.all(1));
        opencv_core.log(magnitudePlane, magnitudePlane);

        if(performShift){
            performShift(magnitudePlane);
        }

        if(this.colorSpace.equals("rgb")){
            opencv_core.normalize(magnitudePlane, magnitudePlane, 0, Image.MAX_RGB, opencv_core.NORM_MINMAX, -1, new Mat());
        }

    }
}*/
