package gui;

import gui.component.InteractiveScrollPane;
import gui.component.QuadTreeImageView;
import gui.component.graph.GraphLayout;
import gui.component.graph.geometry.Cell;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.RITQTCodec;
import model.RITQTNode;
import model.RITQuadTree;
import model.util.ExceptionHandler;

/**
 * Very basic GUI which displays image loaded in from raw image data.
 * Interactive: pannable and zoomable.
 * @author Nabeel Khan
 * @author Mohammed Mehboob
 */
public class RITViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        //
        if (getParameters().getRaw().size() != 1) {
            System.out.println("Usage: java RITViewer uncompressed.txt");
            System.exit(-1);
        }

        stage.setTitle("RITViewer");

        try {
            RITQuadTree quadTree = RITQTCodec.importFile( getParameters().getRaw().get(0) );
            ImageView diagram = new QuadTreeImageView( quadTree );                  //
            InteractiveScrollPane viewport = new InteractiveScrollPane(diagram);    //

            System.out.println(quadTree.getImageData().getSize());

            Scene scene = new Scene(viewport.getCentered(), 512,  512);
            stage.setScene(scene);

            stage.show();
        } catch (Exception e) { ExceptionHandler.guiHandle(e); }

    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}