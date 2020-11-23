package gui;

import gui.components.ImageExplorerView;
import gui.utils.FxConstants;
import gui.utils.FxUtils;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.InputStream;
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

        //TODO: Parse through the file and set image size + validate final size & if values are clamped.
        //Load file into a scanner and generate a grayscale image from its data.
        InputStream fileInput = new FileInputStream( fileName );
        Scanner fileScanner = new Scanner( fileInput );
        InputStream validationInputStream = new FileInputStream( fileName );
        Scanner validationScanner = new Scanner( validationInputStream );

        //Validation parse through input file, calculate size.
        int imageWidth, imageHeight;
        int size = 0;
        while( validationScanner.hasNextInt() ) {
            validationScanner.nextInt();
            ++size;
        }
        imageHeight = imageWidth = (int)( Math.sqrt(size) ) ;
        ImageExplorerView explorerView = new ImageExplorerView( new ImageView(
                FxUtils.generateGrayscaleImage(fileScanner, imageWidth, imageHeight) )
                );

        BorderPane root = new BorderPane();
        root.setCenter( explorerView.getImageView() );

        explorerView.getImageView().fitHeightProperty().bind(root.heightProperty());
        explorerView.getImageView().fitWidthProperty().bind(root.widthProperty());


//        //Create basic scene for testing purposes.
//        BorderPane root = new BorderPane();
//        root.setBackground( new Background( new BackgroundFill(FxConstants.ColorPalette.gray2,
//                                                CornerRadii.EMPTY, Insets.EMPTY)));
//        root.setCenter(imageView);
//        root.getCenter().minHeight(0);
//
//        //Adding bottom panel
//        Label sliderLabel = new Label("Zoom:");
//        sliderLabel.setTextFill( Color.WHITE );
//        Slider slider = new Slider();
//        HBox bottomPanel = new HBox( sliderLabel, slider );
//        bottomPanel.setBackground( new Background( new BackgroundFill(FxConstants.ColorPalette.gray1,
//                                       CornerRadii.EMPTY, Insets.EMPTY)));
//        bottomPanel.setPadding(new Insets(15, 12, 15, 12));
//        bottomPanel.setSpacing(10);
//        bottomPanel.setAlignment( Pos.CENTER_RIGHT );
//        root.setBottom(bottomPanel);

        stage.setScene( new Scene( root, 512, 512 ) );
        stage.setMinWidth(256);     //Set minimum window width.
        stage.setMinHeight(360);    //Set minimum window height.
        stage.show(); //Display the stage.

    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
