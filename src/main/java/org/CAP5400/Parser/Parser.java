package org.CAP5400.Parser;

import org.CAP5400.Image.Image;
import org.CAP5400.Misc.Misc;
import org.CAP5400.RegionOfInterest.ROI;
import org.CAP5400.Toolbox.Toolbox;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

/**
 * This class is used to parse the parameter file and perform the operations on the images. It uses reflection to
 * invoke the methods in the Toolbox class. The parameter file is a text file that contains the following information:
 * <ul>
 *     <li>Source Image Name</li>
 *     <li>Target Image Name</li>
 *     <li>Number of ROIs</li>
 *     <li>Start X Coordinate of ROI</li>
 *     <li>Start Y Coordinate of ROI</li>
 *     <li>Total X Coordinate of ROI</li>
 *     <li>Total Y Coordinate of ROI</li>
 *     <li>Method Name</li>
 *     <li>Method Parameters</li>
 *     <li>The repetition ... {startX, startY, totalX, totalY, MethodName, Method Parameters}</li>
 * </ul>
 * <p><b>NOTE: </b>Ensure that there are <b><i>NO</i></b> overloaded methods in Toolbox. There will be unintended
 * consequences.</p>
 * @see Toolbox
 * @see ROI
 * @Author Reubin George
 */
public class Parser {
    /**
     * This method is used to get the number of parameters for a method.
     * @param clazz The class that contains the method.
     * @param methodName The name of the method.
     * @return The number of parameters for the method.
     * @throws Exception If the method is not found.
     */
    private static Integer getNumberOfParameterForMethod(Class<?> clazz, String methodName) throws Exception{
        var methods = clazz.getDeclaredMethods();
        for(var method : methods){
            if(method.getName().equals(methodName)){
                return method.getParameterCount();
            }
        }
        throw new NoSuchMethodException("The method `" + methodName + "` was not found in `" + clazz.getName() + "`");
    }

    /**
     * This method is used to get the parameter types for a method.
     * @param clazz The class that contains the method.
     * @param methodName The name of the method.
     * @return The parameter types for the method.
     * @throws Exception If the method is not found.
     */
    private static Class<?> [] getParameterTypes(Class<?> clazz, String methodName) throws Exception {
        var numParameters = getNumberOfParameterForMethod(clazz, methodName);
        var types = new Class<?>[numParameters - 1];
        var methods = clazz.getDeclaredMethods();
        for(var method : methods){
            if(method.getName().equals(methodName)){
                for(int i = 1; i < numParameters; i++){
                    types[i - 1] = method.getParameterTypes()[i];
                }
                return types;
            }
        }

        throw new NoSuchMethodException("The method `" + methodName + "` was not found in `" + clazz.getName() + "`");
    }

    /**
     * This method is used to invoke a method using reflection. It is used to invoke the methods in the Toolbox class.
     * @param instance The instance of the class that contains the method.
     * @param region The region of interest.
     * @param methodName The name of the method.
     * @param parameters The parameters for the method.
     * @throws Exception If the method is not found.
     */
    private static void invokeMethod(
            Object instance,
            ROI region,
            String methodName,
            String [] parameters) throws Exception{
        var method = findMethod(instance.getClass(), methodName);
        var numParams = getNumberOfParameterForMethod(instance.getClass(), methodName);
        var paramsForMethod = new Object[numParams];
        paramsForMethod[0] = region;
        var paramsTypesForMethod = getParameterTypes(instance.getClass(), methodName);

        if (parameters.length != numParams - 1) {
            throw new IllegalArgumentException("Wrong number of arguments for method " + methodName);
        }

        for (int i = 0; i < paramsTypesForMethod.length; i++){
            paramsForMethod[i + 1] = convertToType(paramsTypesForMethod[i], parameters[i]);
        }
        method.invoke(instance, paramsForMethod);

    }

    /**
     * This method is used to convert a string to a given type.
     * @param targetType The type to convert the string to.
     * @param value The string to convert.
     * @return The converted object.
     * @throws Exception If the type cannot be used for conversion.
     */
    private static Object convertToType(Class<?> targetType, String value) throws Exception {
        if (targetType == String.class) {
            return value;
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        } else if (targetType == float.class || targetType == Float.class) {
            return Float.parseFloat(value);
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value);
        }
        throw new Exception("Type `" + targetType.getName() + "` cannot be used for conversion");
    }

    /**
     * This method is used to find a method in a class. It is used to find the methods in the Toolbox class.
     * @param clazz The class that contains the method.
     * @param methodName The name of the method.
     * @return The method.
     * @throws NoSuchMethodException If the method is not found.
     */
    private static Method findMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
        var methods = clazz.getDeclaredMethods();
        for (var method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new NoSuchMethodException("The method `" + methodName + "` was not found in `" + clazz.getName() + "`");
    }

    /**
     * This method is used to perform the operations on the images.
     * @param parameterFile The name of the parameter file.
     */
    public static void performOperations(String parameterFile) {
        try{
            var path = Paths.get(parameterFile);
            var lines = Files.readAllLines(path);
            for(var line : lines){
                try {
                    var tokens = new LinkedList<>(Arrays.asList(line.split(" ")));
                    System.out.println("=*=".repeat(35));
                    var sourceImageName = tokens.poll();
                    var sourceImage = new Image(sourceImageName);
                    var targetImageName = tokens.poll();
                    System.out.printf("\033[1m%s\033[0m%-25s\033[1m%s\033[0m%-25s\n\n",
                            "Source Image:", sourceImageName, "Target Image:", targetImageName);
                    var numROIs = Integer.parseInt(Objects.requireNonNull(tokens.poll()));
                    for(int i = 0; i < numROIs; i++){
                        try{
                            var startX = Integer.parseInt(Objects.requireNonNull(tokens.poll()));
                            var startY = Integer.parseInt(Objects.requireNonNull(tokens.poll()));
                            var totalX = Integer.parseInt(Objects.requireNonNull(tokens.poll()));
                            var totalY = Integer.parseInt(Objects.requireNonNull(tokens.poll()));
                            var region = new ROI(sourceImage, startX, startY, totalX, totalY);
                            var methodName = tokens.poll();
                            System.out.printf("\033[1m%s\033[0m%-35s","Perform Operation: ", methodName);
                            var numParams = getNumberOfParameterForMethod(Toolbox.class, methodName);
                            var methodParameters = new String[numParams - 1];
                            for(int j = 0; j < numParams - 1; j++){
                                methodParameters[j] = tokens.poll();
                            }

                            invokeMethod(new Toolbox(), region, methodName, methodParameters);
                            region.applyModifications();
                            System.out.println("["+"\033[1;32mSUCCESS\033[0m" + "]");

                            var additionalFiles = Misc.popContentsOfTrackerFile();
                            if(!additionalFiles.isEmpty()){
                                System.out.printf("\033[1m%25s\033[0m\n","Additional Files: ");
                                for(var additionalFile : additionalFiles){
                                    System.out.print(" ".repeat(10));
                                    System.out.println(additionalFile);
                                }
                            }
                            Misc.deleteFilesWithExtension("json"); // delete the json file used for histogram

                        }
                        catch (Exception e1){
                            System.out.println("["+"\033[1;31mFAILED\033[0m"+ "]");
                            System.out.printf("\033[1m%15s\033[0m%s\n","Reason: ", getErrorMessage(e1));
                        }
                    }
                    sourceImage.save(targetImageName);
                }
                catch (Exception e2){
                    System.out.printf("\033[1m%15s\033[0m%s\n","Error: ", getErrorMessage(e2));
                }

            }
        }
        catch (Exception e3){
            e3.printStackTrace();
        }
    }

    private static String getErrorMessage(Exception exception) {
        if (exception.getMessage() != null) {
            return exception.getMessage();
        } else {
            return exception.getCause().getMessage();
        }
    }
}
