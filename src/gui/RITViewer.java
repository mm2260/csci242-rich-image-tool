package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RITViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        //Set title
        stage.setTitle("RITViewer");

        //get parameters
        Parameters params = getParameters();
        String fileName = params.getRaw().get(0);

        System.out.println(fileName);

        //Load file into a scanner and generate a grayscale image from its data.
        Scanner fileScanner = new Scanner( RITViewer.class.getResourceAsStream(fileName) );
        List<Integer> inputList = new LinkedList<>();
        while(fileScanner.hasNext()){
            inputList.add(fileScanner.nextInt());
        }
        int imageWidth = (int) Math.sqrt(inputList.size());
        int imageHeight = (int) Math.sqrt(inputList.size());
        ImageView imageView = new ImageView( generateGrayscaleImage(inputList, imageWidth, imageHeight) );


        //Create basic scene for testing purposes.
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(imageView);
        stage.setScene( new Scene(borderPane, imageWidth, imageHeight) );
        stage.show(); //Display the stage.

    }
    public static void main(String[] args) {
        Application.launch(args);
    }

    private Image generateGrayscaleImage(List<Integer> inputList, int imageWidth, int imageHeight) {
        WritableImage grayscaleImage = new WritableImage(imageWidth, imageHeight);
        PixelWriter pixelWriter = grayscaleImage.getPixelWriter();
        for( int y = 0; y < imageHeight; ++y ) {
            for ( int x = 0; x < imageWidth; ++x ) {
                //Get pixel values from file one-by-one.
                Color color = Color.grayRgb( inputList.get(y*imageHeight+x));
                pixelWriter.setColor(x,y,color);
            }
        }
        return grayscaleImage;
    }
}
