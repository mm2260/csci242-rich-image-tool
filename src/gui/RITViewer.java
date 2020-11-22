package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
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

        //Load file into a scanner and generate a grayscale image from its data.
        InputStream fileInput = new FileInputStream(getParameters().getRaw().get(0));
        Scanner fileScanner = new Scanner( fileInput );
//        Scanner fileScanner = new Scanner( RITViewer.class.getResourceAsStream("/uncompressed/smileyface256x256.txt") );
        ImageView imageView = new ImageView( FxUtils.generateGrayscaleImage(fileScanner, imageWidth, imageHeight) );

        //Create basic scene for testing purposes.
        BorderPane root = new BorderPane();
        root.setBackground( new Background( new BackgroundFill(Constants.ColorPalette.gray2,
                                                CornerRadii.EMPTY, Insets.EMPTY)));
        root.setCenter(imageView);

        //Adding bottom panel
        Label sliderLabel = new Label("Zoom:");
        sliderLabel.setTextFill( Color.WHITE );
        Slider slider = new Slider();
        HBox bottomPanel = new HBox( sliderLabel, slider );
        bottomPanel.setBackground( new Background( new BackgroundFill(Constants.ColorPalette.gray1,
                                       CornerRadii.EMPTY, Insets.EMPTY)));
        bottomPanel.setPadding(new Insets(15, 12, 15, 12));
        bottomPanel.setSpacing(10);
        bottomPanel.setAlignment( Pos.CENTER_RIGHT );
        root.setBottom(bottomPanel);

        stage.setScene( new Scene( root, 512, 512 ) );
        stage.setMinWidth(256);     //Set minimum window width.
        stage.setMinHeight(360);    //Set minimum window height.
        stage.show(); //Display the stage.

    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
