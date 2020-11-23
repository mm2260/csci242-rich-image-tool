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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
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
        size = imageHeight;
        List<Integer> values = new ArrayList<>();
        while (fileScanner.hasNextInt()){
            values.add(fileScanner.nextInt());
        }

        //Make GridPane
        GridPane gridPane = new GridPane();
        gridPane.getCellBounds(size, size);
        for(int i=0; i<size;i++){
            for(int j=0;j<size; j++){
                //Create Canvas and fill with required color
                final Canvas canvas = new Canvas(1,1);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setFill(Color.grayRgb(values.get(j*size+i)));
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                //Add to gridPane
                gridPane.add(canvas,  i, j, 1, 1);
            }
        }
        //
        Group workArea = new Group();
        workArea.getChildren().add(gridPane);

        //Adding bottom panel
        Label sliderLabel = new Label("Zoom:");
        sliderLabel.setTextFill(FxConstants.ColorPalette.gray1);
        Slider slider = new Slider(1, 5, 1);
        slider.setBlockIncrement(1);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);
        slider.setStyle("-fx-tick-label-fill: white;");

        slider.valueProperty().addListener( (observable, oldValue, newValue ) -> {
            for( Node cell : gridPane.getChildren() ) {
                Canvas canvas = (Canvas) cell;
                canvas.setHeight((Double) newValue);
                canvas.setWidth((Double) newValue);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }
        } );

        HBox bottomPanel = new HBox( sliderLabel, slider );
        bottomPanel.setBackground( new Background( new BackgroundFill(FxConstants.ColorPalette.secondary,
                                       CornerRadii.EMPTY, Insets.EMPTY)));
        bottomPanel.setPadding(new Insets(15, 12, 15, 12));
        bottomPanel.setSpacing(10);
        bottomPanel.setAlignment( Pos.CENTER_RIGHT );
        bottomPanel.setMaxHeight(40);

        BorderPane borderPane = new BorderPane();
        borderPane.setBackground( new Background( new BackgroundFill( FxConstants.ColorPalette.gray2,
                                                CornerRadii.EMPTY, Insets.EMPTY) ) );
        borderPane.setCenter(workArea);

        StackPane root = new StackPane( borderPane, bottomPanel );
        root.setAlignment( bottomPanel, Pos.BOTTOM_CENTER );

        stage.setScene( new Scene( root, 512, 512 ) );
        stage.setMinWidth(256);     //Set minimum window width.
        stage.setMinHeight(360);    //Set minimum window height.
        stage.show(); //Display the stage.

    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}

// <ALTERNATIVE WRITABLE IMAGE IMPLEMENTATION>

//        ImageView imageView = new ImageView( FxUtils.generateGrayscaleImage(fileScanner, imageWidth, imageHeight) );

//        ImageExplorerView explorerView = new ImageExplorerView( new ImageView(
//                FxUtils.generateGrayscaleImage(fileScanner, imageWidth, imageHeight) )
//                );
//
//        BorderPane root = new BorderPane();
//        root.setCenter( explorerView.getImageView() );
//
//        explorerView.getImageView().fitHeightProperty().bind(root.heightProperty());
//        explorerView.getImageView().fitWidthProperty().bind(root.widthProperty());


//Create basic scene for testing purposes.
//        BorderPane workArea = new BorderPane();
//        workArea.setBackground( new Background( new BackgroundFill(FxConstants.ColorPalette.gray2,
//                                                CornerRadii.EMPTY, Insets.EMPTY)));
//        workArea.setCenter(imageView);
//        workArea.getCenter().minHeight(0);