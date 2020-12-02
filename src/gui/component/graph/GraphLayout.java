package gui.component.graph;

import gui.component.graph.geometry.Cell;
import gui.component.graph.geometry.Edge;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.ArrayList;
import java.util.List;

/**
 * Generate graph representation of quadtree.
 * @author Mohammed Mehboob
 * @author Nabeel Khan
 */
public class GraphLayout extends Pane {

    HBox rootContainer;
    Cell root;

    int size=0;

    public static final double INTER_NODE_GAP = 20;
    public static final double INTER_LEVEL_GAP = 30;

    public GraphLayout(Cell root) {
        this.rootContainer = new HBox();
        this.root = root;

        generate(root, rootContainer);

        VBox verticalRootContainer = new VBox(root, rootContainer);
        System.out.println("GraphLayout: Configuring Nodes...");
        configureContainer(verticalRootContainer, rootContainer);
        System.out.println("GraphLayout: Begin Adding Nodes...");
        getChildren().add(verticalRootContainer);
        System.out.println("GraphLayout: Added Nodes.");

        Platform.runLater(() -> {
            System.out.println("GraphLayout: Begin Adding Edges... [Might take some time]");
            createEdges(root);
            System.out.println("GraphLayout: Added Edges.");
        });
    }

    private void generate( Cell root, HBox container ) {

        if(root.isLeaf()) {
            ++size;
            return;
        }

        for(Cell child : root.getCellChildren()) {
            ++size;
            if (child.isLeaf() ) {
                container.getChildren().add(child);
            } else {
                HBox innerContainer = new HBox();
                generate(child, innerContainer);
                VBox innerVerticalContainer = new VBox(child, innerContainer);
                container.getChildren().add( innerVerticalContainer );

                configureContainer(innerContainer);
                configureContainer(innerVerticalContainer, innerContainer);
            }

            configureContainer(container);
        }
    }

    private void configureContainer(Node container) {

        if(container instanceof HBox ) {
            HBox hBox = (HBox) container;
            hBox.setAlignment(Pos.TOP_CENTER);
            hBox.setSpacing(INTER_NODE_GAP);
//            hBox.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null , null)));
        }
    }

    private void configureContainer(Node container, Node innerContainer) {

        if(container instanceof VBox && innerContainer instanceof HBox ) {
            VBox vBox = (VBox) container;
            HBox hBox = (HBox) innerContainer;

            vBox.setSpacing(INTER_LEVEL_GAP);
            vBox.setPadding( new Insets( 5,5,5,5 ));
            vBox.setAlignment(Pos.TOP_CENTER);
            vBox.setMaxHeight( hBox.getHeight() );
//            vBox.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null , null)));
        }
    }

    private void createEdges(Cell root) {
        if(root.isLeaf()){
            return;
        }
        for( Cell child : root.getCellChildren() ) {
            if(child.isLeaf()) {
                getChildren().add( new Edge(root, child, this) );
            } else {
                getChildren().add(new Edge(root, child, this));
                createEdges(child);
            }
        }
    }
}