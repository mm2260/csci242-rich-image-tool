package model.util;

import javafx.scene.control.Alert;
import model.util.exception.IllegalImageSpecification;
import model.util.exception.IllegalPixelValueException;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ExceptionHandler {

    public static void handle(Exception e) {

        System.out.println("===========================================================");
        if(e instanceof FileNotFoundException) {
            System.out.printf("\t==>FATAL ERROR: [FileNotFoundException]%n\t\t--corresponding message: %s",e.getMessage());
            System.out.printf("%nPlease make sure that the path specified is valid: %n\t\ti.e.\tdoes the file exist? %n\t\t\t\tdoes the file's parent directory exist/is it valid?");
            e.printStackTrace();
            System.exit(-1);
        }
        else if (e instanceof IOException) {
            System.out.printf("\t==>FATAL ERROR: [IOException]%n\t\t--corresponding message: %s",e.getMessage());
            System.out.printf("%nPlease make sure that the path specified is valid: %n\t\ti.e.\tdoes the file exist? %n\t\t\t\tdoes the file's parent directory exist/is it valid?");
            e.printStackTrace();
            System.exit(-1);
        }
        else if (e instanceof IllegalPixelValueException) {
            System.out.printf("\t==>FATAL ERROR: [IllegalPixelValue]%n\t\t--corresponding message: %s",e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
        else if (e instanceof IllegalImageSpecification) {
            System.out.printf("\t==>FATAL ERROR: [IllegalImageSpecification]%n\t\t--corresponding message: %s",e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
        else {
            System.out.println(e);
            e.printStackTrace();
        }
    }

public static void guiHandle(Exception e) {

        StringBuffer message = new StringBuffer();
        Alert a = new Alert(Alert.AlertType.ERROR);

        System.out.println("===========================================================");
        if(e instanceof FileNotFoundException) {
            message.append(String.format("\t==>FATAL ERROR: [FileNotFoundException]%n\t\t--corresponding message: %s",e.getMessage()));
            message.append(String.format("%nPlease make sure that the path specified is valid: %n\t\ti.e.\tdoes the file exist? %n\t\t\t\tdoes the file's parent directory exist/is it valid?"));
            a.setContentText(message.toString());
            a.show();
            return;
        }
        else if (e instanceof IOException) {
            message.append(String.format("\t==>FATAL ERROR: [IOException]%n\t\t--corresponding message: %s",e.getMessage()));
            message.append(String.format(("%nPlease make sure that the path specified is valid: %n\t\ti.e.\tdoes the file exist? %n\t\t\t\tdoes the file's parent directory exist/is it valid?")));
            a.setContentText(message.toString());
            a.show();
            return;
        }
        else if (e instanceof IllegalPixelValueException) {
            message.append(String.format("\t==>FATAL ERROR: [IllegalPixelValue]%n\t\t--corresponding message: %s",e.getMessage()));
            a.setContentText(message.toString());
            a.show();
            return;
        }
        else if (e instanceof IllegalImageSpecification) {
            message.append(String.format("\t==>FATAL ERROR: [IllegalImageSpecification]%n\t\t--corresponding message: %s",e.getMessage()));
            a.setContentText(message.toString());
            a.show();
            return;
        }
        else {
            message.append( e.getMessage() );
            message.append(e.toString());
            a.setContentText(message.toString());
            a.show();
            return;
        }
    }
}