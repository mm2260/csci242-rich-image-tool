package gui;

import gui.component.InteractiveScrollPane;
import gui.component.graph.GraphLayout;
import gui.component.graph.geometry.Cell;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.RITQTCodec;
import model.RITQTNode;
import model.RITQuadTree;

//TODO: add gui viewer documentation
public class RITViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("RITViewer");

        RITQuadTree quadTree = RITQTCodec.openFile("images/compressed/simple32x32.rit");
//        RITQuadTree quadTree = RITQTCodec.openFile(getParameters().getRaw().get(0));

        RITQTNode root = quadTree.getRoot();

        if(root.getLowerLeft()==null)
            System.exit(-1);

        Cell rootCell = new Cell(quadTree.getRoot());
        GraphLayout graph = new GraphLayout(rootCell);

        InteractiveScrollPane viewport = new InteractiveScrollPane(graph);
        Scene scene = new Scene( viewport, 512, 512 );
        stage.setScene(scene);

        stage.show();
        graph.showEdges();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}