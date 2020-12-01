package gui.component.graph.geometry;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.HashMap;

public class Edge extends Group {

    Line line;

//    https://stackoverflow.com/questions/44122895/javafx-bind-to-nested-position
    final static HashMap< Cell, ObjectBinding<Bounds> > bindingsMemo = new HashMap<>();
    static int i = 0;

    public Edge(Cell source, Cell target, Pane ancestor ) {
        line = new Line();

        ObjectBinding<Bounds> SourceBoundsInPaneBinding = getBoundsObjectBinding(source, ancestor);
        ObjectBinding<Bounds> TargetBoundsInPaneBinding = getBoundsObjectBinding(target, ancestor);

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

        System.out.println(++i);
        getChildren().add(line);
    }

    private ObjectBinding<Bounds> getBoundsObjectBinding(Cell node, Pane ancestor) {
        ObjectBinding<Bounds> NodeBoundsInPaneBinding;
        if (bindingsMemo.containsKey(node)) {
            NodeBoundsInPaneBinding = bindingsMemo.get(node);
        } else {
            NodeBoundsInPaneBinding = getNodeBoundsInPaneBinding(node, ancestor);
            bindingsMemo.put(node, NodeBoundsInPaneBinding);
        }
        return NodeBoundsInPaneBinding;
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
