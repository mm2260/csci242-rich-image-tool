package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Scanner;

public class RITViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        //Set title
        stage.setTitle("RITViewer");

        //earth256x256.txt
        int imageWidth = 256;
        int imageHeight = 256;

        URL url = RITViewer.class.getResource("/uncompressed/earth256x256.txt");
        File file = new File(url.toURI());



        //Load file into a scanner and generate a grayscale image from its data.
        Scanner fileScanner = new Scanner( RITViewer.class.getResourceAsStream("/uncompressed/earth256x256.txt") );
        ImageView imageView = new ImageView( generateGrayscaleImage(fileScanner, imageHeight, imageWidth) );

        //Create basic scene for testing purposes.
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(imageView);
        stage.setScene( new Scene(borderPane, 256, 256) );
        stage.show(); //Display the stage.

    }
    public static void main(String[] args) {
        Application.launch(args);
    }

    private Image generateGrayscaleImage(Scanner grayscaleValues, int imageWidth, int imageHeight) {
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
