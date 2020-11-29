package gui;

import gui.utils.FxConstants;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.exceptions.InvalidImageSpecificationException;
import model.exceptions.ValueOutOfBoundsException;
import model.utils.Utils;

import java.io.FileNotFoundException;
import java.util.List;

import static javafx.application.Platform.exit;

public class RITViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //TODO: Error handling

        //Set title
        stage.setTitle("RITViewer");

        //get parameters
        Parameters params = getParameters();
        String fileName = params.getRaw().get(0);

        List<Integer> values = null;
        int size = 0;

        //Load in data from file to list of values, and set the size.
        try {
            values = Utils.readFileDataToList(fileName); //Load data.
            size = (int) Math.sqrt(values.size());  //Set size as side-length.
        } catch (FileNotFoundException e ) {
            System.out.println("[-] File not found");
            System.exit(-1);

        } catch (ValueOutOfBoundsException e) {
            System.exit(-1);

        } catch (InvalidImageSpecificationException e ) {
            System.exit(-1);
        }

        int progress = 0;
        int totalPixels = values.size();

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

                System.out.print("\rPROGRESS: " + (++progress*100)/totalPixels + "%" );
            }
        }
        System.out.println("\nLOADING...");
        System.out.println("INITIALIZING GUI...");

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
        StackPane.setAlignment( bottomPanel, Pos.BOTTOM_CENTER );

        stage.setScene( new Scene( root, 512, 512 ) );
        stage.setMinWidth(256);     //Set minimum window width.
        stage.setMinHeight(360);    //Set minimum window height.
        stage.show(); //Display the stage.

        System.out.println("GUI Initialized.");
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}