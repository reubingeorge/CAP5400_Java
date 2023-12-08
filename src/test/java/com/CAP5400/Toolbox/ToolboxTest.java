package com.CAP5400.Toolbox;

import org.CAP5400.Image.Image;
import static org.CAP5400.Image.Image.MAX_RGB;
import static org.CAP5400.Toolbox.Toolbox.*;
import org.CAP5400.RegionOfInterest.ROI;

import org.junit.jupiter.api.Test;


import java.util.function.IntUnaryOperator;

import static org.junit.jupiter.api.Assertions.*;



import static org.assertj.core.api.Assertions.assertThat;


public class ToolboxTest {

    IntUnaryOperator limitValue = value -> Math.min(MAX_RGB, Math.max(0, value));
    @Test
    public void testAddGreySuccess(){
        try {
            var image = new Image("baboon.pgm");
            var region = new ROI(image, 0, 0, 200, 200);
            var additiveValue = 50;
            addGrey(region, additiveValue);
            for(int i = 0; i < region.getTotalX(); i++){
                for(int j = 0; j < region.getTotalY(); j++){
                    var sourcePixel = region.getSourceImage().getPixel(
                            i + region.getStartX(),
                            j + region.getStartY());
                    var regionPixel = region.getRegionImage().getPixel(i, j);
                    assertThat(regionPixel).isEqualTo(Math.min(sourcePixel  + additiveValue, 255));
                }
            }
        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    @Test
    public void testAddGreyFailure(){
        try {
            var image = new Image("baboon.ppm");
            var region = new ROI(image, 0, 0, 200, 200);
            var additiveValue = 50;
            var toolboxError = assertThrows(IllegalArgumentException.class, ()->addGrey(region, additiveValue), "");
            assertTrue(toolboxError.getMessage().contains("This filter only works on greyscale images."));

        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    @Test
    public void testAddBrightnessColorImage(){
        try {
            var colorImage = new Image("baboon.ppm");
            var colorRegion1 = new ROI(colorImage, 0, 0, 200, 200);
            var additiveValue1 = 40;
            addBrightness(colorRegion1, additiveValue1);
            for(int i = 0; i < colorRegion1.getTotalX(); i++) {
                for (int j = 0; j < colorRegion1.getTotalY(); j++) {
                    for (int k = 0; k < colorRegion1.getChannels(); k++) {
                        var sourceX = i + colorRegion1.getStartX();
                        var sourceY = j + colorRegion1.getStartY();
                        var sourcePixel = colorImage.getPixel(sourceX, sourceY, k);
                        var regionPixel = colorRegion1.getRegionImage().getPixel(i, j, k);
                        assertThat(regionPixel).isEqualTo(Math.min(sourcePixel + additiveValue1, MAX_RGB));
                    }
                }
            }

            var colorRegion2 = new ROI(colorImage, 0, 0, 200, 200);
            var additiveValue2 = 60;
            addBrightness(colorRegion2, additiveValue2);
            for(int i = 0; i < colorRegion2.getTotalX(); i++) {
                for (int j = 0; j < colorRegion2.getTotalY(); j++) {
                    for (int k = 0; k < colorRegion2.getChannels(); k++) {
                        var sourceX = i + colorRegion2.getStartX();
                        var sourceY = j + colorRegion2.getStartY();
                        var sourcePixel = colorImage.getPixel(sourceX, sourceY, k);
                        var regionPixel = colorRegion2.getRegionImage().getPixel(i, j, k);
                        assertThat(regionPixel).isEqualTo(sourcePixel);
                    }
                }
            }
        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    @Test
    public void testAddBrightnessGreyscaleImage(){
        try {
            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion1 = new ROI(grayscaleImage, 0, 0, 200, 200);
            var additiveValue1 = 40;
            addBrightness(grayscaleRegion1, additiveValue1);
            for(int i = 0; i < grayscaleRegion1.getTotalX(); i++) {
                for (int j = 0; j < grayscaleRegion1.getTotalY(); j++) {
                    for (int k = 0; k < grayscaleRegion1.getChannels(); k++) {
                        var sourceX = i + grayscaleRegion1.getStartX();
                        var sourceY = j + grayscaleRegion1.getStartY();
                        var sourcePixel = grayscaleImage.getPixel(sourceX, sourceY, k);
                        var regionPixel = grayscaleRegion1.getRegionImage().getPixel(i, j, k);
                        assertThat(regionPixel).isEqualTo(Math.min(sourcePixel + additiveValue1, MAX_RGB));
                    }
                }
            }

            var grayscaleRegion2 = new ROI(grayscaleImage, 0, 0, 200, 200);
            var additiveValue2 = 60;
            addBrightness(grayscaleRegion2, additiveValue2);
            for(int i = 0; i < grayscaleRegion2.getTotalX(); i++) {
                for (int j = 0; j < grayscaleRegion2.getTotalY(); j++) {
                    for (int k = 0; k < grayscaleRegion2.getChannels(); k++) {
                        var sourceX = i + grayscaleRegion2.getStartX();
                        var sourceY = j + grayscaleRegion2.getStartY();
                        var sourcePixel = grayscaleImage.getPixel(sourceX, sourceY, k);
                        var regionPixel = grayscaleRegion2.getRegionImage().getPixel(i, j, k);
                        assertThat(regionPixel).isEqualTo(sourcePixel);
                    }
                }
            }
        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    @Test
    public void testDecreaseBrightnessColorImage(){
        try {
            var colorImage = new Image("baboon.ppm");
            var colorRegion1 = new ROI(colorImage, 0, 0, 200, 200);
            var threshold1 = 200;
            var subtractiveValue1 = 50;
            decreaseBrightness(colorRegion1, threshold1, subtractiveValue1);
            for(int i = 0; i < colorRegion1.getTotalX(); i++) {
                for (int j = 0; j < colorRegion1.getTotalY(); j++) {
                    for (int k = 0; k < colorRegion1.getChannels(); k++) {
                        var sourceX = i + colorRegion1.getStartX();
                        var sourceY = j + colorRegion1.getStartY();
                        var sourcePixel = colorImage.getPixel(sourceX, sourceY, k);
                        var regionPixel = colorRegion1.getRegionImage().getPixel(i, j, k);
                        if(regionPixel < threshold1){
                            assertThat(regionPixel).isEqualTo(limitValue.applyAsInt(sourcePixel - subtractiveValue1));
                        }
                        else{
                            assertThat(regionPixel).isEqualTo(sourcePixel);
                        }
                    }
                }
            }
        }
        catch (Exception e){fail(e.getMessage());}
    }

    @Test
    public void testDecreaseBrightnessGrayscaleImage(){
        try {
            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion = new ROI(grayscaleImage, 0, 0, 200, 200);
            var threshold1 = 200;
            var subtractiveValue1 = 50;
            decreaseBrightness(grayscaleRegion, threshold1, subtractiveValue1);
            for(int i = 0; i < grayscaleRegion.getTotalX(); i++) {
                for (int j = 0; j < grayscaleRegion.getTotalY(); j++) {
                    var sourceX = i + grayscaleRegion.getStartX();
                    var sourceY = j + grayscaleRegion.getStartY();
                    var sourcePixel = grayscaleImage.getPixel(sourceX, sourceY);
                    var regionPixel = grayscaleRegion.getRegionImage().getPixel(i, j);
                    if(regionPixel < threshold1){
                        assertThat(regionPixel).isEqualTo(limitValue.applyAsInt(sourcePixel - subtractiveValue1));
                    }
                    else{
                        assertThat(regionPixel).isEqualTo(sourcePixel);
                    }

                }
            }
        }
        catch (Exception e){fail(e.getMessage());}
    }

    @Test
    public void testDecreaseBrightnessFailure(){
        try {
            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion = new ROI(grayscaleImage, 0, 0, 200, 200);
            var threshold1 = 500;
            var subtractiveValue1 = 50;
            var throwGrayscale = assertThrows(
                    IllegalArgumentException.class,
                    () -> decreaseBrightness(grayscaleRegion, threshold1, subtractiveValue1));
            assertThat(throwGrayscale.getMessage().contains("Threshold needs to be capped between 0 to 255"));

        }
        catch (Exception e){fail(e.getMessage());}
    }

    @Test
    public void testBinarizeSuccess(){
        try {
            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion = new ROI(grayscaleImage, 0, 0, 200, 200);
            var threshold = 100;
            binarize(grayscaleRegion, threshold);
            for(int i = 0; i < grayscaleRegion.getTotalX(); i++){
                for(int j = 0; j < grayscaleRegion.getTotalY(); j++){
                    assertThat(grayscaleRegion.getRegionImage().getPixel(i, j)).isIn(0, MAX_RGB);
                }
            }
        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    @Test
    public void testBinarizeFailure(){
        try {
            var colorImage = new Image("baboon.ppm");
            var colorRegion = new ROI(colorImage, 0, 0, 200, 200);
            var threshold = 100;

            var throwColor = assertThrows(
                    IllegalArgumentException.class,
                    () -> binarize(colorRegion, threshold));
            assertThat(throwColor.getMessage().contains("This filter only works on greyscale images."));

            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion = new ROI(grayscaleImage, 0, 0, 200, 200);
            var threshold1 = 500;
            var throwGrayscale = assertThrows(
                    IllegalArgumentException.class,
                    () -> binarize(grayscaleRegion, threshold1));
            assertThat(throwGrayscale.getMessage().contains("Threshold needs to be capped between 0 to 255"));
        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    @Test
    public void testScaleGrayscaleSuccess(){
        try {
            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion = new ROI(grayscaleImage, 0, 0, 200, 200);
            float grayscaleRatio = 1.0f;
            scale(grayscaleRegion, grayscaleRatio);
            for(int i = 0; i < grayscaleRegion.getTotalX(); i++){
                for(int j = 0; j < grayscaleRegion.getTotalY(); j++){
                    var sourcePixel = grayscaleImage.getPixel(
                            i + grayscaleRegion.getStartX(),
                            j + grayscaleRegion.getStartY());
                    var regionPixel = grayscaleRegion.getRegionImage().getPixel(i, j);
                    assertThat(regionPixel).isEqualTo(sourcePixel);
                }
            }
        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    @Test
    public void testScaleColorSuccess(){
        try {
            var colorImage = new Image("baboon.ppm");
            var colorRegion = new ROI(colorImage, 0, 0, 200, 200);
            float colorRatio = 1.0f;
            scale(colorRegion, colorRatio);
            for(int i = 0; i < colorRegion.getTotalX(); i++){
                for(int j = 0; j < colorRegion.getTotalY(); j++){
                    for(int k = 0; k < colorRegion.getChannels(); k++){
                        var sourceX = i + colorRegion.getStartX();
                        var sourceY = j + colorRegion.getStartY();
                        var sourcePixel = colorImage.getPixel(sourceX, sourceY, k);
                        var regionX = (int)((float)i/colorRatio);
                        var regionY = (int)((float)j/colorRatio);
                        var regionPixel = colorRegion.getRegionImage().getPixel(regionX, regionY, k);
                        assertThat(regionPixel).isEqualTo(sourcePixel);
                    }
                }
            }
        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    @Test
    public void testScaleFailure(){
        try {
            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion = new ROI(grayscaleImage, 0, 0, 200, 200);
            float grayscaleRatio = 0.5f;
            var thrown = assertThrows(
                    IllegalArgumentException.class,
                    ()->scale(grayscaleRegion, grayscaleRatio));
            assertThat(thrown.getMessage().contains("The scaling factor needs to be between 1 - 2"));

        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    @Test
    public void testRotateSuccess(){
        try {
            var colorImage = new Image("baboon.pgm");
            var colorRegion = new ROI(colorImage, 0, 0, 200, 200);
            rotate(colorRegion, 360);
            for(int i = 0; i < colorRegion.getTotalX(); i++){
                for(int j = 0; j < colorRegion.getTotalY(); j++){
                    for(int k = 0; k < colorRegion.getChannels(); k++){
                        var sourceX = i + colorRegion.getStartX();
                        var sourceY = j + colorRegion.getStartY();
                        var sourcePixel = colorImage.getPixel(sourceX, sourceY, k);
                        var regionPixel = colorRegion.getRegionImage().getPixel(i, j, k);
                        assertThat(regionPixel).isEqualTo(sourcePixel);
                    }
                }
            }
        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    @Test
    public void testRotateFailure(){
        try {
            var grayscaleImage = new Image("baboon.pgm");
            var grayscaleRegion = new ROI(grayscaleImage, 0, 0, 200, 300);
            var thrown = assertThrows(
                    IllegalArgumentException.class,
                    ()->rotate(grayscaleRegion, 90));
            assertThat(thrown.getMessage().contains("ROI must be square"));

        }
        catch (Exception e){ fail(e.getMessage()); }
    }

}
