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

