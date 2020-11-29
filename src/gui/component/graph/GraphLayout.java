package gui.component.graph;

import gui.component.graph.geometry.Cell;
import gui.component.graph.geometry.Edge;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class GraphLayout extends Pane {

    private static final double INTER_NODE_GAP = 20;
    private static final double INTER_LEVEL_GAP = 30;

    private final List<Edge> edges = new ArrayList<>();

    public GraphLayout(Cell root) {

        HBox rootContainer = new HBox();
        generate(root, rootContainer);

        VBox verticalRootContainer = new VBox(root, rootContainer);
        configureContainer(verticalRootContainer, rootContainer);

        getChildren().addAll(edges);
        getChildren().add(verticalRootContainer);
    }

    public List<Edge> getEdges() { return edges; }

    private void generate( Cell root, HBox container ) {
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

            edges.add( new Edge(root, child) );
            configureContainer(container);
        }
    }

    private void configureContainer(Node container) {

        if(container instanceof HBox ) {
            HBox hBox = (HBox) container;
            hBox.setSpacing(INTER_NODE_GAP);
        }
    }

    private void configureContainer(Node container, Node innerContainer) {

        if(container instanceof VBox && innerContainer instanceof HBox ) {
            VBox vBox = (VBox) container;
            HBox hBox = (HBox) innerContainer;

            vBox.setSpacing(INTER_LEVEL_GAP);
            vBox.setAlignment(Pos.CENTER);
            vBox.setMaxHeight( hBox.getHeight() );
        }
    }

}
