package org.CAP5400;

import nu.pattern.OpenCV;
import org.CAP5400.Parser.Parser;

public class Main {
    public static void main(String... args) throws Exception {
        OpenCV.loadLocally();
        Parser.performOperations("parameters.txt");

    }

}