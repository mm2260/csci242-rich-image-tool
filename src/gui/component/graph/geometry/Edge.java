package gui.component.graph.geometry;

import gui.component.graph.GraphLayout;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class Edge extends Group {

    Line line;

    public Edge(Cell source, Cell target, Pane ancestor ) {
        line = new Line();

        ObjectBinding<Bounds> SourceBoundsInPaneBinding = getNodeBoundsInPaneBinding(source, ancestor);
        ObjectBinding<Bounds> TargetBoundsInPaneBinding = getNodeBoundsInPaneBinding(target, ancestor);

        line.startXProperty().bind(Bindings.createDoubleBinding(
                () -> SourceBoundsInPaneBinding.get().getCenterX(),
                TargetBoundsInPaneBinding));
        line.startYProperty().bind(Bindings.createDoubleBinding(
                () -> SourceBoundsInPaneBinding.get().getMinY()+Cell.CELL_SIZE,
                TargetBoundsInPaneBinding));

        line.endXProperty().bind(Bindings.createDoubleBinding(
                () -> TargetBoundsInPaneBinding.get().getCenterX(),
                TargetBoundsInPaneBinding));
        line.endYProperty().bind(Bindings.createDoubleBinding(
                () -> TargetBoundsInPaneBinding.get().getMinY(),
                TargetBoundsInPaneBinding));

        getChildren().add(line);
    }

    private ObjectBinding<Bounds> getNodeBoundsInPaneBinding(Node node, Pane ancestor) {
        return Bindings.createObjectBinding( () -> {
                    Bounds nodeLocal = node.getBoundsInLocal();
                    Bounds nodeScene = node.localToScene(nodeLocal);
                    Bounds nodePane = ancestor.sceneToLocal(nodeScene);
                    return nodePane ;
                }, node.boundsInLocalProperty(), node.localToSceneTransformProperty(),
                ancestor.localToSceneTransformProperty());
    }
}
