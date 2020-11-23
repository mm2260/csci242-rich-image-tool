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
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class RITViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //TODO: Error handling

        //Set title
        stage.setTitle("RITViewer");

        //get parameters
        Parameters params = getParameters();
        String fileName = params.getRaw().get(0);

        //TODO: Parse through the file and set image size.
        //Load file into a scanner and generate a grayscale image from its data.
        InputStream fileInput = new FileInputStream( fileName );
        Scanner fileScanner = new Scanner( fileInput );
        InputStream validationInputStream = new FileInputStream( fileName );
        Scanner validationScanner = new Scanner( validationInputStream );

        //Validation parse through input file, calculate size.
        int size = 0;
        while( validationScanner.hasNextInt() ) {
            ++size;
            System.out.println("SIZE: "+ size + " VALUE: " + validationScanner.nextInt());
        }

        System.out.println("FINAL SIZE: "+ (int)( Math.sqrt(size)) );

        int imageWidth, imageHeight;
        imageHeight = imageWidth = (int)( Math.sqrt(size) ) ;

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
