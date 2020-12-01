package model.util;

import model.util.exception.IllegalPixelValueException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionHandler {

    public static void handle(Exception e) {

        System.out.println("===========================================================");
        if(e instanceof FileNotFoundException) {
            System.out.printf("\t==>FATAL ERROR: [FileNotFoundException]%n\t\t--corresponding message: %s",e.getMessage());
            System.out.printf("%nPlease make sure that the path specified is valid: %n\t\ti.e.\tdoes the file exist? %n\t\t\t\tdoes the file's parent directory exist/is it valid?");

            System.exit(-1);
        }
        else if (e instanceof IOException) {
            System.out.printf("\t==>FATAL ERROR: [IOException]%n\t\t--corresponding message: %s",e.getMessage());
            System.out.printf("%nPlease make sure that the path specified is valid: %n\t\ti.e.\tdoes the file exist? %n\t\t\t\tdoes the file's parent directory exist/is it valid?");

            System.exit(-1);
        }
        else if (e instanceof IllegalPixelValueException) {
            System.out.printf("\t==>FATAL ERROR: [IllegalPixelValue]%n\t\t--corresponding message: %s",e.getMessage());
            System.exit(-1);
        }
    }

}