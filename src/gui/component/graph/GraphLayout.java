package gui.component.graph;

import gui.component.graph.geometry.Cell;
import gui.component.graph.geometry.Edge;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;

public class GraphLayout extends Pane {

    HBox rootContainer;
    Cell root;

    public static final double INTER_NODE_GAP = 20;
    public static final double INTER_LEVEL_GAP = 30;

    private final List<Edge> edges = new ArrayList<>();

    public GraphLayout(Cell root) {
        this.rootContainer = new HBox();
        this.root = root;

        generate(root, rootContainer);

        VBox verticalRootContainer = new VBox(root, rootContainer);
        configureContainer(verticalRootContainer, rootContainer);

        getChildren().add(verticalRootContainer);
    }

    private void generate( Cell root, HBox container ) {

        if(root.isLeaf())
            return;

        for(Cell child : root.getCellChildren()) {

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

            hBox.setSpacing(INTER_NODE_GAP);
//            hBox.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null , null)));
        }
    }

    private void configureContainer(Node container, Node innerContainer) {

        if(container instanceof VBox && innerContainer instanceof HBox ) {
            VBox vBox = (VBox) container;
            HBox hBox = (HBox) innerContainer;

            vBox.setSpacing(INTER_LEVEL_GAP);
            vBox.setAlignment(Pos.CENTER);
            vBox.setMaxHeight( hBox.getHeight() );
//            vBox.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null , null)));
        }
    }

    public void showEdges() {
        showEdges(this.root);
        getChildren().addAll(edges);
    }

    private void showEdges(Cell root) {

        if(root.isLeaf()){
            return;
        }
        for( Cell child : root.getCellChildren() ) {
            if(child.isLeaf()) {
                edges.add( new Edge(root, child) );
            } else {
                edges.add(new Edge(root, child));
                showEdges(child);
            }
        }
    }
}