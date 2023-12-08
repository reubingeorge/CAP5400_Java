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


public class Parser {
    private static Integer getNumberOfParameterForMethod(Class<?> clazz, String methodName) throws Exception{
        var methods = clazz.getDeclaredMethods();
        for(var method : methods){
            if(method.getName().equals(methodName)){
                return method.getParameterCount();
            }
        }
        throw new NoSuchMethodException("The method `" + methodName + "` was not found in `" + clazz.getName() + "`");
    }

    private static Class<?> [] getParameterTypes(Class<?> clazz, String methodName) throws Exception {
        var numParameters = getNumberOfParameterForMethod(clazz, methodName);
        var types = new Class<?>[numParameters-1];
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

    private static Object convertToType(Class<?> targetType, String value) throws Exception {
        if (targetType == String.class) {
            return value;
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value);
        } else if (targetType == float.class || targetType == Float.class) {
            return Float.parseFloat(value);
        }
        throw new Exception("Type `" + targetType.getName() + "` cannot be used for conversion");
    }

    private static Method findMethod(Class<?> clazz, String methodName) throws NoSuchMethodException {
        var methods = clazz.getDeclaredMethods();
        for (var method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        throw new NoSuchMethodException("The method `" + methodName + "` was not found in `" + clazz.getName() + "`");
    }

    public static void performOperations(String parameterFile) {

        try{
            var path = Paths.get(parameterFile);
            var lines = Files.readAllLines(path);
            for(var line : lines){
                try {
                    var tokens = new LinkedList<>(Arrays.stream(line.split(" ")).toList());
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
                            e1.printStackTrace();
                            System.out.printf("\033[1m%15s\033[0m%s\n","Reason: ", e1.getCause().getMessage());
                        }
                    }
                    sourceImage.save(targetImageName);
                }
                catch (Exception e2){
                    e2.printStackTrace();
                    System.out.printf("\033[1m%15s\033[0m%s\n","Error: ", e2.getCause().getMessage());
                }

            }
        }
        catch (Exception e3){
            e3.printStackTrace();
        }
    }
}
