package gui;

import gui.component.InteractiveScrollPane;
import gui.component.QuadTreeImageView;
import gui.component.graph.GraphLayout;
import gui.component.graph.geometry.Cell;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.RITQTCodec;
import model.RITQuadTree;

//TODO: add gui viewer documentation
public class RITViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("RITViewer");

        RITQuadTree quadTree = RITQTCodec.importFile( getParameters().getRaw().get(0) );
        ImageView diagram = new QuadTreeImageView( quadTree );
        GraphLayout graphLayout = new GraphLayout( new Cell(quadTree.getRoot()) );
        InteractiveScrollPane viewport = new InteractiveScrollPane(graphLayout);

        Scene scene = new Scene(viewport.getCentered(), 512,  512);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}