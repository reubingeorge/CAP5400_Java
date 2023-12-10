package com.CAP5400.RegionOfInterest;

import nu.pattern.OpenCV;
import org.CAP5400.Image.Image;
import org.CAP5400.RegionOfInterest.ROI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RoiTest {
    @Test
    public void testSquareRoiSuccess() {
        try{
            OpenCV.loadLocally();
            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion = new ROI(grayscaleImage, 0, 0, 200, 200);
            grayscaleRegion.enforceSquareDimensions(true);

            var colorImage = new Image("baboon.ppm");
            var colorRegion = new ROI(colorImage, 0, 0, 200, 200);
            colorRegion.enforceSquareDimensions(true);
        }
        catch (Exception e){ fail(e.getMessage()); }

    }

    @Test
    public void testSquareRoiFailure() {
        try{
            OpenCV.loadLocally();
            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion = new ROI(grayscaleImage, 0, 0, 200, 300);
            var thrownGrayscaleImageChannel = assertThrows(IllegalArgumentException.class,
                    () -> grayscaleRegion.enforceSquareDimensions(true),
                    "");
            assertTrue(thrownGrayscaleImageChannel.getMessage().contains("ROI must be square"));

            var colorImage = new Image("baboon.ppm");
            var colorRegion = new ROI(colorImage, 0, 0, 200, 300);
            var thrownColorImageChannel = assertThrows(IllegalArgumentException.class,
                    () -> colorRegion.enforceSquareDimensions(true),
                    "");
            assertTrue(thrownColorImageChannel.getMessage().contains("ROI must be square"));

        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    @Test
    public void testBaseTwoRoiSuccess() {
        try{
            OpenCV.loadLocally();
            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion = new ROI(grayscaleImage, 0, 0, 128, 128);
            grayscaleRegion.enforceBaseTwoDimensions(true);

            var colorImage = new Image("baboon.ppm");
            var colorRegion = new ROI(colorImage, 0, 0, 128, 128);
            colorRegion.enforceBaseTwoDimensions(true);
        }
        catch (Exception e){ fail(e.getMessage()); }

    }

    @Test
    public void testBaseTwoRoiFailure() {
        try{
            OpenCV.loadLocally();
            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion = new ROI(grayscaleImage, 0, 0, 200, 300);
            var thrownGrayscaleImageChannel = assertThrows(IllegalArgumentException.class,
                    () -> grayscaleRegion.enforceBaseTwoDimensions(true),
                    "");
            assertTrue(thrownGrayscaleImageChannel.getMessage().contains("ROI must be in the form of 2^n x 2^n"));

            var colorImage = new Image("baboon.ppm");
            var colorRegion = new ROI(colorImage, 0, 0, 200, 300);
            var thrownColorImageChannel = assertThrows(IllegalArgumentException.class,
                    () -> colorRegion.enforceBaseTwoDimensions(true),
                    "");
            assertTrue(thrownColorImageChannel.getMessage().contains("ROI must be in the form of 2^n x 2^n"));

        }
        catch (Exception e){ fail(e.getMessage()); }
    }


}
