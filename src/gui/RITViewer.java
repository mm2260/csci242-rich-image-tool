package gui;

import gui.component.InteractiveScrollPane;
import gui.component.graph.GraphLayout;
import gui.component.graph.geometry.Cell;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.RITQTCodec;
import model.RITQTNode;
import model.RITQuadTree;

import java.util.Scanner;

//TODO: add gui viewer documentation
public class RITViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("RITViewer");

        RITQuadTree quadTree = RITQTCodec.openFile("images/compressed/simple32x32.rit");

        ImageView diagram = new ImageView( generateGrayscaleImage(quadTree.getImageData(), 15) );
        GraphLayout graph = new GraphLayout( new Cell(quadTree.getRoot()) );

        InteractiveScrollPane viewport = new InteractiveScrollPane( new VBox(diagram, graph));
        Scene scene = new Scene(viewport, 512, 512);
        stage.setScene(scene);

        stage.show();
        graph.showEdges();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private Image generateGrayscaleImage(RITQuadTree.DataArray imageData, int pixelSize) {
        int width, height;
        width=height=imageData.getSize();

        WritableImage grayscaleImage = new WritableImage(width*pixelSize, height*pixelSize);
        PixelWriter pixelWriter = grayscaleImage.getPixelWriter();
        for( int y = 0; y < height; ++y ) {
            for ( int x = 0; x < width; ++x ) {
                //Get pixel values from file one-by-one.
                Color color = Color.grayRgb( imageData.get(y,x) );
                for (int dy = 0; dy < pixelSize; dy++) {
                    for (int dx = 0; dx < pixelSize; dx++) {
                        pixelWriter.setColor(x * pixelSize + dx, y * pixelSize + dy, color);
                    }
                }
            }
        }
        return grayscaleImage;
    }
}