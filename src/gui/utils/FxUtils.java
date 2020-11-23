package gui.utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Scanner;

public class FxUtils {
    public static Image generateGrayscaleImage(Scanner grayscaleValues, int imageWidth, int imageHeight) {
        WritableImage grayscaleImage = new WritableImage(imageWidth, imageHeight);
        PixelWriter pixelWriter = grayscaleImage.getPixelWriter();
        for( int y = 0; y < imageHeight; ++y ) {
            for ( int x = 0; x < imageWidth; ++x ) {
                //Get pixel values from file one-by-one.
                Color color = Color.grayRgb( grayscaleValues.nextInt() );
                pixelWriter.setColor(x,y,color);
            }
        }
        return grayscaleImage;
    }
}
