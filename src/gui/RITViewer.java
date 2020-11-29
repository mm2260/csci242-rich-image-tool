package gui;

import gui.component.InteractiveScrollPane;
import gui.component.graph.GraphLayout;
import gui.component.graph.geometry.Cell;
import gui.component.graph.geometry.Edge;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.RITQTCodec;
import model.RITQTNode;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

//TODO: add gui viewer documentation
public class RITViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("RITViewer");

//        Parameters params = getParameters();
//        String fileName = params.getRaw().get(0);
//        //TODO: Parse through the file and set image size + validate final size & if values are clamped.
//        //Load file into a scanner and generate a grayscale image from its data.
//        InputStream fileInput = new FileInputStream( fileName );
//        Scanner fileScanner = new Scanner( fileInput );
//
//        Image test = generateGrayscaleImage(fileScanner, 256, 256);
//        InteractiveScrollPane viewport = new InteractiveScrollPane( new ImageView(test) );

//        Scene scene = new Scene( viewport, 512, 512 );

        RITQTNode quadTree = RITQTCodec.Decoder.parse( new ArrayList<Integer>(Arrays.asList(
                                                       -1,155,-1, 255, 255, -1, 0, 33, 153, 20, 255,66,255)) );
        Cell graphRoot = new Cell(quadTree);
        GraphLayout graphLayout = new GraphLayout(graphRoot);
        Pane canvas = new Pane();
        canvas.getChildren().add(graphLayout);
        InteractiveScrollPane viewport = new InteractiveScrollPane(canvas);

        Scene scene = new Scene( viewport, 512, 512 );
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        Application.launch(args);
    }

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