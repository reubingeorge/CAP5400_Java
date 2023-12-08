package com.CAP5400.Image;

import org.CAP5400.Image.Image;
import org.CAP5400.Exceptions.ImageOutOfBoundsException;
import org.junit.jupiter.api.Test;

import static org.CAP5400.Misc.Misc.delete;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImageTest {

    @Test
    public void testImageConstructors(){
        try {
            var grayscaleImage = new Image("baboon.pgm");
            assertThat(grayscaleImage.getChannels()).isEqualTo(1);
            assertThat(grayscaleImage.getRows()).isEqualTo(512);
            assertThat(grayscaleImage.getColumns()).isEqualTo(512);

            var colorImage = new Image("baboon.ppm");
            assertThat(colorImage.getChannels()).isEqualTo(3);
            assertThat(colorImage.getRows()).isEqualTo(512);
            assertThat(colorImage.getColumns()).isEqualTo(512);

            if(areSameImages(colorImage, grayscaleImage)){
                fail("One image is color while the other is grayscale");
            }

            var emptyGrayscaleImage = new Image(512, 512);
            assertThat(emptyGrayscaleImage.getChannels()).isEqualTo(1);
            assertThat(emptyGrayscaleImage.getRows()).isEqualTo(512);
            assertThat(emptyGrayscaleImage.getColumns()).isEqualTo(512);
            for(int i = 0; i < 512; i++){
                for(int j = 0; j < 512; j++){
                    assertThat(emptyGrayscaleImage.getPixel(i, j)).isEqualTo(0);
                }
            }


            var emptyColorImage = new Image(512, 512, 3);
            assertThat(emptyColorImage.getChannels()).isEqualTo(3);
            assertThat(emptyColorImage.getRows()).isEqualTo(512);
            assertThat(emptyColorImage.getColumns()).isEqualTo(512);
            for(int i = 0; i < 512; i++){
                for(int j = 0; j < 512; j++){
                    for(int k = 0; k < 3; k++) {
                        assertThat(emptyColorImage.getPixel(i, j, k)).isEqualTo(0);
                    }
                }
            }

            var copiedGrayscaleImage = new Image(grayscaleImage);
            assertThat(areSameImages(grayscaleImage, copiedGrayscaleImage)).isTrue();

            var copiedColorImage = new Image(colorImage);
            assertThat(areSameImages(colorImage, copiedColorImage)).isTrue();

            assertThat(areSameImages(copiedColorImage, copiedGrayscaleImage)).isFalse();


        }
        catch (Exception e){
            e.printStackTrace();
            fail("There should be no exceptions!"); }
    }

    @Test
    public void testBounds(){
        var emptyGrayscaleImage = new Image(512, 512);
        var thrownGrayscaleImageChannel = assertThrows(ImageOutOfBoundsException.class,
                () -> emptyGrayscaleImage.getPixel(0, 0, 2),
                "channel out of bound");
        assertTrue(thrownGrayscaleImageChannel.getMessage().contains("out of bounds"));

        var thrownGrayscaleImageRow = assertThrows(ImageOutOfBoundsException.class,
                () -> emptyGrayscaleImage.getPixel(700, 0, 0),
                "channel out of bound");
        assertTrue(thrownGrayscaleImageRow.getMessage().contains("out of bounds"));

        var thrownGrayscaleImageCol = assertThrows(ImageOutOfBoundsException.class,
                () -> emptyGrayscaleImage.getPixel(123, 513, 0),
                "channel out of bound");
        assertTrue(thrownGrayscaleImageCol.getMessage().contains("out of bounds"));


    }

    @Test
    public void testGetAndSetChannels(){
        try{
            var colorImage = new Image("baboon.ppm");
            var colorImageCopy = new Image(colorImage.getRows(), colorImage.getColumns(), colorImage.getChannels());
            var channel1 = colorImage.getChannel(0);
            var channel2 = colorImage.getChannel(1);
            var channel3 = colorImage.getChannel(2);

            colorImageCopy.setChannel(channel1, 0);
            colorImageCopy.setChannel(channel2, 1);
            colorImageCopy.setChannel(channel3, 2);

            assertThat(areSameImages(colorImage, colorImageCopy)).isTrue();
        }
        catch (Exception e){
            fail(e.getMessage());
        }

    }

    @Test
    public void testImageSimilarityWithNoFilters(){
        try {
            var grayscaleImage = new Image("baboon.pgm");
            grayscaleImage.save("baboon_saved.pgm");
            var savedGrayscaleImage = new Image("baboon_saved.pgm");
            assertThat(areSameImages(grayscaleImage, savedGrayscaleImage)).isTrue();

            var colorImage = new Image("baboon.ppm");
            colorImage.save("baboon_saved.ppm");
            var savedColorImage = new Image("baboon_saved.ppm");
            assertThat(areSameImages(colorImage, savedColorImage)).isTrue();

            delete("baboon_saved.ppm");
            delete("baboon_saved.pgm");

        }
        catch (Exception e){ fail(e.getMessage()); }
    }

    private boolean areSameImages(Image leftImage, Image rightImage){
        if(leftImage.getRows() != rightImage.getRows()){ return false; }
        if(leftImage.getColumns() != rightImage.getColumns()){ return false; }
        if(leftImage.getChannels() != rightImage.getChannels()){ return false; }
        if(leftImage.isColor() != rightImage.isColor()) { return false; }

        for(int i = 0; i < leftImage.getRows(); i++){
            for(int j = 0; j < rightImage.getRows(); j++){
                for(int k = 0; k < leftImage.getChannels(); k++){
                    try{
                        var leftPixel = leftImage.getPixel(i, j, k);
                        var rightPixel = rightImage.getPixel(i, j, k);
                        if(leftPixel != rightPixel) { System.out.println(leftPixel + " " + rightPixel); return false; }
                    }

                    catch (Exception e){ return false; }
                }
            }
        }
        return true;
    }

}
