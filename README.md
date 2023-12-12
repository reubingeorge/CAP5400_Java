<h3>CAP 5400: Java Implementation</h3>
<dl>
    <dt><h4>Installation</h4></dt>
    <dd>
        <p>The jar file can only be executed on a system with Java 17+</p>
        <code>$ java -version</code>
        <p>The output of the command should look something like this:</p>
        
```console
openjdk version "17.0.9" 2023-10-17
OpenJDK Runtime Environment (build 17.0.9+9-Ubuntu-122.04)
OpenJDK 64-Bit Server VM (build 17.0.9+9-Ubuntu-122.04, mixed mode, sharing)
```
If you have an older version of Java, please upgrade it using the following commands (for Ubuntu):
```console
sudo apt-get update
sudo apt-get install openjdk-17-jdk
sudo update-alternatives --config java
```
Use the last command to set the needed version of Java

If you are not using an IDE but using a terminal instead, use the following commands:

```console
java -jar CAP5400.jar parameters.txt
```
</dd>
</dl>
<dl>
    <dt><h4>Parameters File</h4></dt>
    <dd>
        <code>source output #roi x1 y1 Sx1 Sy1 func p1 p2 pX x2 y2 Sx2 Sy2 func p1 p2 pX</code>
        <ol>There are for parameters:
            <li><code>source</code> - name of original input image</li>
            <li><code>output</code> - name of new modified output image</li>
            <li><code>#roi</code> - number of ROIs</li>
            <li><code>x</code> - initial row, this means the first pixel in the leftmost position to begin the ROI 
                calculation</li>
            <li><code>y</code> - initial column, means first pixel in the top most position to begin the ROI 
                calculation.</li>
            <li><code>Sx</code> - total number of pixels in the x-axis</li>
            <li><code>Sy</code> - total number of pixels in the y-axis</li>
            <li><code>func</code> - function name to be applied</li>
            <li><code>p1, p2, . . . ,pX</code> - parameters needed for function. Can range from 1 parameter through X 
                number parameters depending on the function</li>
        </ol>
    </dd>
</dl>
<dl>
    <dt><h4>Function names and parameters</h4></dt>
<dd>
Here is the list of functions names and parameter that is needed for the parameter file. 

| Function Name                           | Function Description                                                                                                                                                                                                                                                                                                           | Parameters (in order)                                                                                                                                                                                                                                                                                                                                           |
|-----------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| <h5>addGrey</h5>                        | This method increases the intensity of all pixels in the given region of interest                                                                                                                                                                                                                                              | <ol><li>additiveValue</li></ol>                                                                                                                                                                                                                                                                                                                                 |
| <h5>addBrightness</h5>                  | This method increase the intensity of all pixel in all available channels in the given region of interest.                                                                                                                                                                                                                     | <ol> <li>value - This value must be between -50 & 50.</li> </ol>                                                                                                                                                                                                                                                                                                |
| <h5>decreaseBrightness</h5>             | This method takes a region of interest within an image and adjusts the brightness of pixels based on a given threshold.                                                                                                                                                                                                        | <ol> <li>threshold – The threshold intensity value. If the pixel intensity in the source image is below this threshold, brightness will be decreased.</li> <li> value – The value by which to decrease the brightness of pixels below the threshold</li>  </ol>                                                                                                 |
| <h5>binarize</h5>                       | This method binarizes a region of interest (ROI) within an image based on a specified threshold.                                                                                                                                                                                                                               | <ol> <li>threshold – The intensity threshold for binarization (0 to 255).</li> </ol>                                                                                                                                                                                                                                                                            |
| <h5>scale</h5>                          | This method scales a region of interest (ROI) within an image by a specified ratio.                                                                                                                                                                                                                                            | <ol> <li>ratio – The scaling ratio (1.0 to 2.0).</li> </ol>                                                                                                                                                                                                                                                                                                     |
| <h5>rotate</h5>                         | This method rotates a region of interest (ROI) within an image by a specified angle.                                                                                                                                                                                                                                           | <ol> <li>angle – The angle by which to rotate the region (must be a positive multiple of 90 degrees).</li> </ol>                                                                                                                                                                                                                                                |
| <h5>histogramEqualizationAll</h5>       | This method applies histogram equalization to a region of interest (ROI) within an image. The equalization is performed separately on each channel for color images.                                                                                                                                                           | <ol><li>colorSpace – The color space in which to perform histogram equalization. It can be either "RGB" or "HSV".</li></ol>                                                                                                                                                                                                                                     |
| <h5>histogramEqualization</h5>          | This method applies histogram equalization to a region of interest (ROI) within an image based on the specified color space.                                                                                                                                                                                                   | <ol> <li>colorSpace – The color space in which to perform histogram equalization. It can be either "RGB" or "HSV".</li> <li>channelIndex – The index of the channel to equalize.</li> </ol>                                                                                                                                                                     |
| <h5>thresholdHistogramEqualization</h5> | This method applies threshold histogram equalization to a specified channel within a region of interest (ROI) within an image. The method enhances the histogram of dark pixels (below the threshold) in the specified channel.                                                                                                | <ol> <li>threshold – The intensity threshold below which pixel values are considered dark.</li> <li>channelIndex – The index of the channel to perform threshold histogram equalization.</li> </ol>                                                                                                                                                             |
| <h5>histogramStretch</h5>               | This method applies histogram stretching to a specific channel within a region of interest (ROI). The stretched histogram is based on the specified minimum and maximum intensity values. The stretched values are applied to the pixels in the specified channel, and the resulting image is saved with an updated histogram. | <ol> <li>minStretch – The minimum intensity value for histogram stretching (0-255).</li> <li>maxStretch – The maximum intensity value for histogram stretching (0-255).</li> <li>channel – The channel for which the histogram stretching is applied (0-2).</li> </ol>                                                                                          |
| <h5>histogramStretchAll</h5>            | This method applies histogram stretching to all channels within a region of interest (ROI). The stretched histogram is based on the specified minimum and maximum intensity values. The stretched values are applied to each channel independently, and the resulting image is saved with updated histograms for all channels. | <ol> <li>minStretch – The minimum intensity value for histogram stretching (0-255).</li> <li>maxStretch – The maximum intensity value for histogram stretching (0-255).</li> </ol>                                                                                                                                                                              |
| <h5>lowPassFilter</h5>                  | This method applies a low-pass filter to a region of interest (ROI) within an image. The filter is applied to each channel independently for color images.                                                                                                                                                                     | <ol> <li>colorspace – The color space in which to perform the low-pass filter. It can be either "RGB" or "HSV".</li> <li>channel – The index of the channel to which the low-pass filter is applied.</li> <li>filterRadius – The radius of the low-pass filter.</li> </ol>                                                                                      |
| <h5>highPassFilter</h5>                 | This method applies a high-pass filter to a region of interest (ROI) within an image. The filter is applied to each channel independently for color images.                                                                                                                                                                    | <ol> <li>colorspace – The color space in which to perform the high-pass filter. It can be either "RGB" or "HSV".</li> <li>channel – The index of the channel to which the high-pass filter is applied.</li> <li>filterRadius – The radius of the high-pass filter.</li> </ol>                                                                                   |
| <h5>bandStopFilter</h5>                 | This method applies a band-pass filter to a region of interest (ROI) within an image. The filter is applied to each channel independently for color images.                                                                                                                                                                    | <ol> <li>colorspace – The color space in which to perform the band-pass filter. It can be either "RGB" or "HSV".</li> <li>channel – The index of the channel to which the band-pass filter is applied.</li> <li>innerFilterRadius – The radius of the inner band-pass filter.</li> <li>outerFilterRadius – The radius of the outer band-pass filter.</li> </ol> |
| <h5>sharpenEdge</h5>                    | This method sharpens the edges of a region of interest (ROI) within an image. The sharpening is performed independently on each channel for color images using an unsharp mask.                                                                                                                                                | <ol> <li>colorspace – The color space in which to perform the sharpening. It can be either "RGB" or "HSV".</li> <li>channel – The index of the channel to which the sharpening is applied.</li>  <li>filterRadius – The size of the filter used to perform the sharpening.</li>  <li>sharpeningFactor – The sharpening factor (0.0 to 1.0).</li>  </ol>         |

</dd>
</dl>

<dl>
    <dt><h4>Results</h4></dt>
    <dd>

|            Grayscale Baboon             |                Color Baboon                 |             Grayscale Mountain              |            Grayscale Slope            |
|:---------------------------------------:|:-------------------------------------------:|:-------------------------------------------:|:-------------------------------------:|
| ![baboon.jpg](readme-pics%2Fbaboon.jpg) | ![baboon_1.jpg](readme-pics%2Fbaboon_1.jpg) | ![mountain.jpg](readme-pics%2Fmountain.jpg) | ![slope.jpg](readme-pics%2Fslope.jpg) |


|                          Rotate 90                           |                           Rotate 180                           |                           Rotate 270                           |
|:------------------------------------------------------------:|:--------------------------------------------------------------:|:--------------------------------------------------------------:|
| ```baboon.pgm baboon_ro90.pgm 1 200 200 200 200 rotate 90``` | ```baboon.pgm baboon_ro180.pgm 1 200 200 200 200 rotate 180``` | ```baboon.pgm baboon_ro270.pgm 1 200 200 200 200 rotate 270``` |
|      ![baboon_ro90.jpg](readme-pics%2Fbaboon_ro90.jpg)       |      ![baboon_ro180.jpg](readme-pics%2Fbaboon_ro180.jpg)       |      ![baboon_ro270.jpg](readme-pics%2Fbaboon_ro270.jpg)       |
| ```baboon.ppm baboon_ro90.ppm 1 200 200 200 200 rotate 90``` | ```baboon.ppm baboon_ro180.ppm 1 200 200 200 200 rotate 180``` | ```baboon.ppm baboon_ro270.ppm 1 200 200 200 200 rotate 270``` |
|    ![baboon_ro90_1.jpg](readme-pics%2Fbaboon_ro90_1.jpg)     |    ![baboon_ro180_1.jpg](readme-pics%2Fbaboon_ro180_1.jpg)     |    ![baboon_ro270_1.jpg](readme-pics%2Fbaboon_ro270_1.jpg)     |


|                            Scale                             |                                    Scale w/ Rotate 90                                     |                                     Scale w/ Rotate 180                                     |                                     Scale w/ Rotate 270                                     |
|:------------------------------------------------------------:|:-----------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------:|
| ```baboon.pgm baboon_sc15.pgm 1 200 200 200 200 scale 1.5``` | ```baboon.pgm baboon_sr15-90.pgm 2 200 200 200 200 scale 1.5 200 200 200 200 rotate 90``` | ```baboon.pgm baboon_sr15-180.pgm 2 200 200 200 200 scale 1.5 200 200 200 200 rotate 180``` | ```baboon.pgm baboon_sr15-270.pgm 2 200 200 200 200 scale 1.5 200 200 200 200 rotate 270``` |
|      ![baboon_sc15.jpg](readme-pics%2Fbaboon_sc15.jpg)       |                  ![baboon_sr15-90.jpg](readme-pics%2Fbaboon_sr15-90.jpg)                  |                  ![baboon_sr15-180.jpg](readme-pics%2Fbaboon_sr15-180.jpg)                  |                  ![baboon_sr15-270.jpg](readme-pics%2Fbaboon_sr15-270.jpg)                  |
| ```baboon.ppm baboon_sc15.ppm 1 200 200 200 200 scale 1.5``` | ```baboon.ppm baboon_sr15-90.ppm 2 200 200 200 200 scale 1.5 200 200 200 200 rotate 90``` | ```baboon.ppm baboon_sr15-180.ppm 2 200 200 200 200 scale 1.5 200 200 200 200 rotate 180``` | ```baboon.ppm baboon_sr15-270.ppm 2 200 200 200 200 scale 1.5 200 200 200 200 rotate 270``` |
|    ![baboon_sc15_1.jpg](readme-pics%2Fbaboon_sc15_1.jpg)     |                ![baboon_sr15-90_1.jpg](readme-pics%2Fbaboon_sr15-90_1.jpg)                |                ![baboon_sr15-180_1.jpg](readme-pics%2Fbaboon_sr15-180_1.jpg)                |                ![baboon_sr15-270_1.jpg](readme-pics%2Fbaboon_sr15-270_1.jpg)                |


|                            Histogram Stretching                            |                                     Histogram Stretching w/ Rotate 90                                     |                                     Histogram Stretching w/ Rotate 180                                      |                                     Histogram Stretching w/ Rotate 270                                      |
|:--------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------:|:-----------------------------------------------------------------------------------------------------------:|
| ```baboon.pgm baboon_hs.pgm 1 200 200 200 200 histogramStretchAll 0 255``` | ```baboon.pgm baboon_hs_ro90.pgm 2 200 200 200 200 histogramStretchAll 0 255 200 200 200 200 rotate 90``` | ```baboon.pgm baboon_hs_ro180.pgm 2 200 200 200 200 histogramStretchAll 0 255 200 200 200 200 rotate 180``` | ```baboon.pgm baboon_hs_ro270.pgm 2 200 200 200 200 histogramStretchAll 0 255 200 200 200 200 rotate 270``` |
|               ![baboon_hs.jpg](readme-pics%2Fbaboon_hs.jpg)                |                          ![baboon_hs_ro90.jpg](readme-pics%2Fbaboon_hs_ro90.jpg)                          |                          ![baboon_hs_ro180.jpg](readme-pics%2Fbaboon_hs_ro180.jpg)                          |                          ![baboon_hs_ro270.jpg](readme-pics%2Fbaboon_hs_ro270.jpg)                          |
| ```baboon.ppm baboon_hs.ppm 1 200 200 200 200 histogramStretchAll 0 255``` | ```baboon.ppm baboon_hs_ro90.ppm 2 200 200 200 200 histogramStretchAll 0 255 200 200 200 200 rotate 90``` | ```baboon.ppm baboon_hs_ro180.ppm 2 200 200 200 200 histogramStretchAll 0 255 200 200 200 200 rotate 180``` | ```baboon.ppm baboon_hs_ro270.ppm 2 200 200 200 200 histogramStretchAll 0 255 200 200 200 200 rotate 270``` | 
|             ![baboon_hs_1.jpg](readme-pics%2Fbaboon_hs_1.jpg)              |                        ![baboon_hs_ro90_1.jpg](readme-pics%2Fbaboon_hs_ro90_1.jpg)                        |                        ![baboon_hs_ro180_1.jpg](readme-pics%2Fbaboon_hs_ro180_1.jpg)                        |                        ![baboon_hs_ro270_1.jpg](readme-pics%2Fbaboon_hs_ro270_1.jpg)                        |


|                                                                        Histogram Stretching                                                                         |                                                                                 Histogram Stretching                                                                                 |
|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                             ```baboon.pgm baboon_hs.pgm 1 200 200 200 200 histogramStretchAll 0 255```                                              |                                                        ```slope.pgm slope_hs.pgm 1 25 25 484 750 histogramStretchAll 0 255```                                                        |
|                                                            ![baboon_hs.jpg](readme-pics%2Fbaboon_hs.jpg)                                                            |                                                                     ![slope_hs.jpg](readme-pics%2Fslope_hs.jpg)                                                                      |
| ![hist_20231211_033057_34OYld.jpg](readme-pics%2Fhist_20231211_033057_34OYld.jpg) ![hist_20231211_033058_EbhbmA.jpg](readme-pics%2Fhist_20231211_033058_EbhbmA.jpg) | <center>![hist_20231211_033057_BgWD9y.jpg](readme-pics%2Fhist_20231211_033057_BgWD9y.jpg) ![hist_20231211_033057_iX7Lxp.jpg](readme-pics%2Fhist_20231211_033057_iX7Lxp.jpg)</center> |


|                               Histogram Equalization                                |                                       Histogram Equalization w/ Rotate 90                                        |                                        Histogram Equalization w/ Rotate 180                                        |                                        Histogram Equalization w/ Rotate 270                                        |
|:-----------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------:|
|    ```baboon.pgm baboon_he.pgm 1 200 200 200 200 histogramEqualizationAll rgb```    |   ```baboon.pgm baboon_he_ro90.pgm 2 200 200 200 200 histogramEqualizationAll rgb 200 200 200 200 rotate 90```   |   ```baboon.pgm baboon_he_ro180.pgm 2 200 200 200 200 histogramEqualizationAll rgb 200 200 200 200 rotate 180```   |   ```baboon.pgm baboon_he_ro270.pgm 2 200 200 200 200 histogramEqualizationAll rgb 200 200 200 200 rotate 270```   |
|                    ![baboon_he.jpg](readme-pics%2Fbaboon_he.jpg)                    |                             ![baboon_he_ro90.jpg](readme-pics%2Fbaboon_he_ro90.jpg)                              |                             ![baboon_he_ro180.jpg](readme-pics%2Fbaboon_he_ro180.jpg)                              |                             ![baboon_he_ro270.jpg](readme-pics%2Fbaboon_he_ro270.jpg)                              |
|   ```baboon.ppm baboon_he_1.ppm 1 200 200 200 200 histogramEqualizationAll rgb```   |  ```baboon.ppm baboon_he_ro90_1.ppm 2 200 200 200 200 histogramEqualizationAll rgb 200 200 200 200 rotate 90```  |  ```baboon.ppm baboon_he_ro180_1.ppm 2 200 200 200 200 histogramEqualizationAll rgb 200 200 200 200 rotate 180```  |  ```baboon.ppm baboon_he_ro270_1.ppm 2 200 200 200 200 histogramEqualizationAll rgb 200 200 200 200 rotate 270```  | 
|                  ![baboon_he_1.jpg](readme-pics%2Fbaboon_he_1.jpg)                  |                           ![baboon_he_ro90_1.jpg](readme-pics%2Fbaboon_he_ro90_1.jpg)                            |                           ![baboon_he_ro180_1.jpg](readme-pics%2Fbaboon_he_ro180_1.jpg)                            |                           ![baboon_he_ro270_1.jpg](readme-pics%2Fbaboon_he_ro270_1.jpg)                            |
| ```baboon.ppm baboon_he_hsv_1.ppm 1 200 200 200 200 histogramEqualizationAll hsv``` | ```baboon.ppm baboon_he_ro90_hsv.ppm 2 200 200 200 200 histogramEqualizationAll hsv 200 200 200 200 rotate 90``` | ```baboon.ppm baboon_he_ro180_hsv.ppm 2 200 200 200 200 histogramEqualizationAll hsv 200 200 200 200 rotate 180``` | ```baboon.ppm baboon_he_ro270_hsv.ppm 2 200 200 200 200 histogramEqualizationAll hsv 200 200 200 200 rotate 270``` |
|              ![baboon_he_hsv_1.jpg](readme-pics%2Fbaboon_he_hsv_1.jpg)              |                         ![baboon_he_ro90_hsv.jpg](readme-pics%2Fbaboon_he_ro90_hsv.jpg)                          |                         ![baboon_he_ro180_hsv.jpg](readme-pics%2Fbaboon_he_ro180_hsv.jpg)                          |                         ![baboon_he_ro270_hsv.jpg](readme-pics%2Fbaboon_he_ro270_hsv.jpg)                          |


|                                                                       Histogram Equalization                                                                       |                                                                       Histogram Equalization                                                                        |                                                                       Histogram Equalization                                                                       |
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------------------------------------------------------------------:|:------------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                           ```baboon.pgm baboon_he.pgm 1 200 200 200 200 histogramEqualizationAll rgb```                                            |                                              ```slope.pgm slope_he.pgm 1 25 25 484 750 histogramEqualizationAll rgb```                                              |                                          ```mountain.pgm mountain_he.pgm 1 10 10 321 492 histogramEqualizationAll rgb```                                           |
|                                                           ![baboon_he.jpg](readme-pics%2Fbaboon_he.jpg)                                                            |                                                             ![slope_he.jpg](readme-pics%2Fslope_he.jpg)                                                             |                                                         ![mountain_he.jpg](readme-pics%2Fmountain_he.jpg)                                                          |
| ![hist_20231211_033058_l8lU13.jpg](readme-pics%2Fhist_20231211_033058_l8lU13.jpg)![hist_20231211_033058_XVjGix.jpg](readme-pics%2Fhist_20231211_033058_XVjGix.jpg) | ![hist_20231211_033059_HgZIek.jpg](readme-pics%2Fhist_20231211_033059_HgZIek.jpg) ![hist_20231211_033059_r7CD9L.jpg](readme-pics%2Fhist_20231211_033059_r7CD9L.jpg) | ![hist_20231211_033058_XAGVZn.jpg](readme-pics%2Fhist_20231211_033058_XAGVZn.jpg)![hist_20231211_033058_xSis7n.jpg](readme-pics%2Fhist_20231211_033058_xSis7n.jpg) |


|                                                                     Low Pass Filter                                                                     |                                                                      High Pass Filter                                                                      |                                                                         Band Stop Filter                                                                         |
|:-------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                         ```baboon.pgm baboon_lp.pgm 1 128 128 256 256 lowPassFilter rgb 0 30```                                         |                                          ```baboon.pgm baboon_hp.pgm 1 128 128 256 256 highPassFilter rgb 0 15```                                          |                                           ```baboon.pgm baboon_bs.pgm 1 128 128 256 256 bandStopFilter rgb 0 10 30```                                            |
|                                                      ![baboon_lp.jpg](readme-pics%2Fbaboon_lp.jpg)                                                      |                                                       ![baboon_hp.jpg](readme-pics%2Fbaboon_hp.jpg)                                                        |                                                          ![baboon_bs.jpg](readme-pics%2Fbaboon_bs.jpg)                                                           |
| ```baboon.ppm baboon_lp_h_s_v.ppm 3 0 0 128 128 lowPassFilter hsv 0 10 128 128 128 128 lowPassFilter hsv 1 10 256 256 128 128 lowPassFilter hsv 2 10``` | ```baboon.ppm baboon_hp_h_s_v.ppm 3 0 0 128 128 highPassFilter hsv 0 15 128 128 128 128 highPassFilter hsv 1 15 256 256 128 128 highPassFilter hsv 2 15``` | ```baboon.ppm baboon_bs_h_s_v.ppm 3 0 0 128 128 bandStopFilter hsv 0 5 15 128 128 128 128 bandStopFilter hsv 1 5 15 256 256 128 128 bandStopFilter hsv 2 5 15``` |
|                                                ![baboon_lp_h_s_v.jpg](readme-pics%2Fbaboon_lp_h_s_v.jpg)                                                |                                                 ![baboon_hp_h_s_v.jpg](readme-pics%2Fbaboon_hp_h_s_v.jpg)                                                  |                                                    ![baboon_bs_h_s_v.jpg](readme-pics%2Fbaboon_bs_h_s_v.jpg)                                                     |


|                                                    Edge Sharpening (Radius = 30, Strengthening Factor = 0.3)                                                    |                                                    Edge Sharpening (Radius = 60, Strengthening Factor = 0.3)                                                    |                                                    Edge Sharpening (Radius = 120, Strengthening Factor = 0.3)                                                    |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------------------------------------------------------------------------------------:|
|                                            ```baboon.pgm baboon_um3003.pgm 1 0 0 512 512 sharpenEdge rgb 0 30 0.3```                                            |                                            ```baboon.pgm baboon_um6003.pgm 1 0 0 512 512 sharpenEdge rgb 0 60 0.3```                                            |                                           ```baboon.pgm baboon_um12003.pgm 1 0 0 512 512 sharpenEdge rgb 0 120 0.3```                                            |
|                                                      ![baboon_um3003.jpg](readme-pics%2Fbaboon_um3003.jpg)                                                      |                                                      ![baboon_um6003.jpg](readme-pics%2Fbaboon_um6003.jpg)                                                      |                                                     ![baboon_um12003.jpg](readme-pics%2Fbaboon_um12003.jpg)                                                      |
| ![mag_20231211_033102_YtgH5s.jpg](readme-pics%2Fmag_20231211_033102_YtgH5s.jpg) ![mag_20231211_033102_M6eW44.jpg](readme-pics%2Fmag_20231211_033102_M6eW44.jpg) | ![mag_20231211_033102_Gs7wBf.jpg](readme-pics%2Fmag_20231211_033102_Gs7wBf.jpg) ![mag_20231211_033102_14aRAJ.jpg](readme-pics%2Fmag_20231211_033102_14aRAJ.jpg) | ![mag_20231211_033102_oM08Yh.jpg](readme-pics%2Fmag_20231211_033102_oM08Yh.jpg)  ![mag_20231211_033102_0Nq9Ii.jpg](readme-pics%2Fmag_20231211_033102_0Nq9Ii.jpg) |


</dd>
</dl>

<dl>
<dt><h4>Terminal Output</h4></dt>
<dd>

```console
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_adbi.pgm          

Perform Operation: addGrey                            [SUCCESS]
Perform Operation: binarize                           [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_ro90.pgm          

Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_ro180.pgm         

Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_ro270.pgm         

Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_ro90.ppm          

Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_ro180.ppm         

Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_ro270.ppm         

Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_sc15.pgm          

Perform Operation: scale                              [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_sr15-90.pgm       

Perform Operation: scale                              [SUCCESS]
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_sr15-180.pgm      

Perform Operation: scale                              [SUCCESS]
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_sr15-270.pgm      

Perform Operation: scale                              [SUCCESS]
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_sc15.ppm          

Perform Operation: scale                              [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_sr15-90.ppm       

Perform Operation: scale                              [SUCCESS]
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_sr15-180.ppm      

Perform Operation: scale                              [SUCCESS]
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_sr15-270.ppm      

Perform Operation: scale                              [SUCCESS]
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:slope.pgm                Target Image:slope_hs.pgm             

Perform Operation: histogramStretchAll                [SUCCESS]
       Additional Files: 
          hist_20231211_033057_BgWD9y.pgm
          hist_20231211_033057_iX7Lxp.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_hs.pgm            

Perform Operation: histogramStretchAll                [SUCCESS]
       Additional Files: 
          hist_20231211_033057_34OYld.pgm
          hist_20231211_033058_EbhbmA.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_hs_ro90.pgm       

Perform Operation: histogramStretchAll                [SUCCESS]
       Additional Files: 
          hist_20231211_033058_25Khix.pgm
          hist_20231211_033058_G0oIbN.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_hs_ro180.pgm      

Perform Operation: histogramStretchAll                [SUCCESS]
       Additional Files: 
          hist_20231211_033058_Iy3DVL.pgm
          hist_20231211_033058_hq6V7Q.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_hs_ro270.pgm      

Perform Operation: histogramStretchAll                [SUCCESS]
       Additional Files: 
          hist_20231211_033058_2ALydE.pgm
          hist_20231211_033058_rUHj3Q.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:slope.pgm                Target Image:slope_he.pgm             

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files: 
          hist_20231211_033058_uCUaKn.pgm
          hist_20231211_033058_FMrSla.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_he.pgm            

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files: 
          hist_20231211_033058_l8lU13.pgm
          hist_20231211_033058_XVjGix.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_he_ro90.pgm       

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files: 
          hist_20231211_033058_lt9Esy.pgm
          hist_20231211_033058_B9fzXN.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_he_ro180.pgm      

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files: 
          hist_20231211_033058_NbJzNF.pgm
          hist_20231211_033058_VSULpL.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_he_ro270.pgm      

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files: 
          hist_20231211_033058_sbfcPK.pgm
          hist_20231211_033058_kQw6I9.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:mountain.pgm             Target Image:mountain_he.pgm          

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files: 
          hist_20231211_033058_XAGVZn.pgm
          hist_20231211_033058_xSis7n.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:slope.pgm                Target Image:slope_he.pgm             

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files: 
          hist_20231211_033059_HgZIek.pgm
          hist_20231211_033059_r7CD9L.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:mountain.pgm             Target Image:mountain_he_th50.pgm     

Perform Operation: thresholdHistogramEqualization     [SUCCESS]
       Additional Files: 
          hist_20231211_033059_LuRMvB.pgm
          hist_20231211_033059_es9MGA.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_r_g_b.ppm      

Perform Operation: histogramEqualization              [SUCCESS]
       Additional Files: 
          hist_20231211_033059_c8M5Aq.pgm
          hist_20231211_033059_2dQPVD.pgm
Perform Operation: histogramEqualization              [SUCCESS]
       Additional Files: 
          hist_20231211_033059_ZujFsX.pgm
          hist_20231211_033059_0wKi0P.pgm
Perform Operation: histogramEqualization              [SUCCESS]
       Additional Files: 
          hist_20231211_033059_h8DN5Q.pgm
          hist_20231211_033059_z4BrIr.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_rgb.ppm        

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files: 
          hist_20231211_033059_gBU4im.pgm
          hist_20231211_033059_g1LLno.pgm
          hist_20231211_033059_DrNMa3.pgm
          hist_20231211_033059_Ql3Psr.pgm
          hist_20231211_033059_DDqUZh.pgm
          hist_20231211_033100_uwYO87.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_h_s_v.ppm      

Perform Operation: histogramEqualization              [SUCCESS]
       Additional Files: 
          hist_20231211_033100_XZhcYA.pgm
          hist_20231211_033100_jhMfDL.pgm
Perform Operation: histogramEqualization              [SUCCESS]
       Additional Files: 
          hist_20231211_033100_csMxbK.pgm
          hist_20231211_033100_GG683H.pgm
Perform Operation: histogramEqualization              [SUCCESS]
       Additional Files: 
          hist_20231211_033100_YPIREY.pgm
          hist_20231211_033100_uOTP2n.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_hsv.ppm        

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files: 
          hist_20231211_033100_I4mDif.pgm
          hist_20231211_033100_4vnixU.pgm
          hist_20231211_033100_zcfuVm.pgm
          hist_20231211_033101_4F9Rq0.pgm
          hist_20231211_033101_TZ1HoN.pgm
          hist_20231211_033101_ez9gnW.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_lp.pgm            

Perform Operation: lowPassFilter                      [SUCCESS]
       Additional Files: 
          mag_20231211_033101_WRKHeX.pgm
          mag_20231211_033101_lqgMA3.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_lp_ro90.pgm       

Perform Operation: lowPassFilter                      [SUCCESS]
       Additional Files: 
          mag_20231211_033101_r3hU3n.pgm
          mag_20231211_033101_YpbbaH.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_lp_ro180.pgm      

Perform Operation: lowPassFilter                      [SUCCESS]
       Additional Files: 
          mag_20231211_033101_QtKsNv.pgm
          mag_20231211_033101_gBSzON.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_lp_ro270.pgm      

Perform Operation: lowPassFilter                      [SUCCESS]
       Additional Files: 
          mag_20231211_033101_www2rx.pgm
          mag_20231211_033101_ph2OvZ.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_hp.pgm            

Perform Operation: highPassFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033101_C6CCMG.pgm
          mag_20231211_033101_zZkdgL.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_hp_ro90.pgm       

Perform Operation: highPassFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033101_Zxb20y.pgm
          mag_20231211_033101_N4nZMs.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_hp_ro180.pgm      

Perform Operation: highPassFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033101_5TVHbW.pgm
          mag_20231211_033101_SX5djk.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_hp_ro270.pgm      

Perform Operation: highPassFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033101_smhwAA.pgm
          mag_20231211_033101_GeK502.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_bs.pgm            

Perform Operation: bandStopFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033101_vkCAmx.pgm
          mag_20231211_033101_2DhMn3.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_bs_ro90.pgm       

Perform Operation: bandStopFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033101_Jh3o17.pgm
          mag_20231211_033101_ij1q0t.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_bs_ro180.pgm      

Perform Operation: bandStopFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033102_GC4Jv4.pgm
          mag_20231211_033102_E7qFzY.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_bs_ro270.pgm      

Perform Operation: bandStopFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033102_zP7LCM.pgm
          mag_20231211_033102_pVaATa.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_lp_h_s_v.ppm      

Perform Operation: lowPassFilter                      [SUCCESS]
       Additional Files: 
          mag_20231211_033102_yzroRy.pgm
          mag_20231211_033102_XrQfw5.pgm
Perform Operation: lowPassFilter                      [SUCCESS]
       Additional Files: 
          mag_20231211_033102_4lzvXq.pgm
          mag_20231211_033102_ApNQ5S.pgm
Perform Operation: lowPassFilter                      [SUCCESS]
       Additional Files: 
          mag_20231211_033102_527gRQ.pgm
          mag_20231211_033102_MEfOyp.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_hp_h_s_v.ppm      

Perform Operation: highPassFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033102_BvHpLx.pgm
          mag_20231211_033102_RUbHG2.pgm
Perform Operation: highPassFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033102_oT449G.pgm
          mag_20231211_033102_yFvZT3.pgm
Perform Operation: highPassFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033102_CgfCMK.pgm
          mag_20231211_033102_g2Voza.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_bs_h_s_v.ppm      

Perform Operation: bandStopFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033102_rj1Gvy.pgm
          mag_20231211_033102_IXbVvu.pgm
Perform Operation: bandStopFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033102_TAepqK.pgm
          mag_20231211_033102_WLc8Aw.pgm
Perform Operation: bandStopFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033102_WQscXB.pgm
          mag_20231211_033102_49ws8I.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_um3003.pgm        

Perform Operation: sharpenEdge                        [SUCCESS]
       Additional Files: 
          mag_20231211_033102_YtgH5s.pgm
          mag_20231211_033102_M6eW44.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_um6003.pgm        

Perform Operation: sharpenEdge                        [SUCCESS]
       Additional Files: 
          mag_20231211_033102_Gs7wBf.pgm
          mag_20231211_033102_14aRAJ.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_um12003.pgm       

Perform Operation: sharpenEdge                        [SUCCESS]
       Additional Files: 
          mag_20231211_033102_oM08Yh.pgm
          mag_20231211_033102_0Nq9Ii.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_v_um3003.ppm      

Perform Operation: sharpenEdge                        [SUCCESS]
       Additional Files: 
          mag_20231211_033103_hb1SKv.pgm
          mag_20231211_033103_Tzo8I9.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.pgm               Target Image:baboon_lp_hp_bs.pgm      

Perform Operation: lowPassFilter                      [SUCCESS]
       Additional Files: 
          mag_20231211_033103_K4aPic.pgm
          mag_20231211_033103_SriZ8V.pgm
Perform Operation: highPassFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033103_WIMilV.pgm
          mag_20231211_033103_qWcexB.pgm
Perform Operation: bandStopFilter                     [SUCCESS]
       Additional Files: 
          mag_20231211_033103_8Zh9dM.pgm
          mag_20231211_033103_0Q795P.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_hs.ppm

Perform Operation: histogramStretchAll                [SUCCESS]
       Additional Files:
          hist_20231212_143119_YAwEZL.pgm
          hist_20231212_143119_v74o7C.pgm
          hist_20231212_143119_btd46B.pgm
          hist_20231212_143119_RTlKO9.pgm
          hist_20231212_143119_Gyt90o.pgm
          hist_20231212_143119_2kAcjt.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_hs_ro90.ppm

Perform Operation: histogramStretchAll                [SUCCESS]
       Additional Files:
          hist_20231212_143119_Lb5bGa.pgm
          hist_20231212_143119_XaVtGn.pgm
          hist_20231212_143119_1AVUS2.pgm
          hist_20231212_143119_FkP8EO.pgm
          hist_20231212_143119_5OHF5v.pgm
          hist_20231212_143119_sqDNRc.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_hs_ro180.ppm

Perform Operation: histogramStretchAll                [SUCCESS]
       Additional Files:
          hist_20231212_143120_PIEgeh.pgm
          hist_20231212_143120_c8Br01.pgm
          hist_20231212_143120_tmGpIb.pgm
          hist_20231212_143120_oliU2j.pgm
          hist_20231212_143120_ur48Nv.pgm
          hist_20231212_143120_rWXFiz.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_hs_ro270.ppm

Perform Operation: histogramStretchAll                [SUCCESS]
       Additional Files:
          hist_20231212_143120_3ZCPui.pgm
          hist_20231212_143120_Ll1I8P.pgm
          hist_20231212_143120_GqKgoO.pgm
          hist_20231212_143120_OUVttt.pgm
          hist_20231212_143120_m8RLYi.pgm
          hist_20231212_143120_GJ923F.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_1.ppm

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files:
          hist_20231212_150552_PVJXBY.pgm
          hist_20231212_150552_QsTCT7.pgm
          hist_20231212_150552_bNyJsB.pgm
          hist_20231212_150553_AzvbP2.pgm
          hist_20231212_150553_AWbHGA.pgm
          hist_20231212_150553_Rl2kJm.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_ro90_1.ppm

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files:
          hist_20231212_150553_qTOUCI.pgm
          hist_20231212_150553_SutQlF.pgm
          hist_20231212_150553_NonDtl.pgm
          hist_20231212_150553_1TOEN8.pgm
          hist_20231212_150553_apjNIB.pgm
          hist_20231212_150553_69MfPO.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_ro180_1.ppm

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files:
          hist_20231212_150553_8VEiEK.pgm
          hist_20231212_150553_F2l5iJ.pgm
          hist_20231212_150553_xRwmmr.pgm
          hist_20231212_150553_XfMgUX.pgm
          hist_20231212_150553_xx3y8K.pgm
          hist_20231212_150553_5fC9rU.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_ro270_1.ppm

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files:
          hist_20231212_150553_zKJWAG.pgm
          hist_20231212_150553_vgGRkW.pgm
          hist_20231212_150554_Th3OYT.pgm
          hist_20231212_150554_omOLA4.pgm
          hist_20231212_150554_7DKCHh.pgm
          hist_20231212_150554_NL7xXW.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_hsv.ppm

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files:
          hist_20231212_150554_T5zGMO.pgm
          hist_20231212_150554_k3eUyR.pgm
          hist_20231212_150554_L11RUd.pgm
          hist_20231212_150554_wfdQwV.pgm
          hist_20231212_150554_4cty3z.pgm
          hist_20231212_150554_eHfX2t.pgm
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_ro90_hsv.ppm

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files:
          hist_20231212_150554_eRPXpm.pgm
          hist_20231212_150554_Zzb1n1.pgm
          hist_20231212_150554_iZR531.pgm
          hist_20231212_150554_yZfMr7.pgm
          hist_20231212_150554_V7VGdZ.pgm
          hist_20231212_150554_KdjdVS.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_ro180_hsv.ppm

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files:
          hist_20231212_150554_TjrObe.pgm
          hist_20231212_150555_aZ2rl0.pgm
          hist_20231212_150555_bWjw7M.pgm
          hist_20231212_150555_YoXhS0.pgm
          hist_20231212_150555_LEWwYi.pgm
          hist_20231212_150555_TvtT69.pgm
Perform Operation: rotate                             [SUCCESS]
=*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*==*=
Source Image:baboon.ppm               Target Image:baboon_he_ro270_hsv.ppm

Perform Operation: histogramEqualizationAll           [SUCCESS]
       Additional Files:
          hist_20231212_150555_C21Pj0.pgm
          hist_20231212_150555_dFPQGp.pgm
          hist_20231212_150555_ODQTEI.pgm
          hist_20231212_150555_GgHGkv.pgm
          hist_20231212_150555_Fh1tvc.pgm
          hist_20231212_150555_Olpsss.pgm
Perform Operation: rotate                             [SUCCESS]
```

</dd>
</dl>

