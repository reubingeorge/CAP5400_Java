<h3>CAP 5400: Java Implementation</h3>
<dl>
    <dt><h4>Installation</h4></dt>
    <dd>
        <p>The jar file can only be executed on a system with Java 17+</p>
        <code>$ java -version</code>
        <p>The output of the command should look something like this:</p>
        <code>openjdk version "17.0.9" 2023-10-17</code>
        <code>OpenJDK Runtime Environment (build 17.0.9+9-Ubuntu-122.04)</code>
        <code>OpenJDK 64-Bit Server VM (build 17.0.9+9-Ubuntu-122.04, mixed mode, sharing)</code>
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
            <li><code>func</code> - function name to be applied</li>
            <li><code>x</code> - initial row, this means the first pixel in the leftmost position to begin the ROI 
                calculation</li>
            <li><code>y</code> - initial column, means first pixel in the top most position to begin the ROI 
                calculation.</li>
            <li><code>Sx</code> - total number of pixels in the x-axis</li>
            <li><code>Sy</code> - total number of pixels in the y-axis</li>
            <li><code>p1, p2, . . . ,pX</code> - parameters needed for function. Can range from 1 parameter through X 
                number parameters depending on the function</li>
        </ol>
    </dd>
</dl>

