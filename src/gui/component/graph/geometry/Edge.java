package gui.component.graph.geometry;

import gui.component.graph.GraphLayout;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;

public class Edge extends Group {

    Line line;

    public Edge(Cell source, Cell target) {

        line = new Line();

        System.out.println(target);

        Bounds srcBound = source.localToScene( source.getBoundsInLocal() );
        Bounds targetBound = target.localToScene( target.getBoundsInLocal() );

        line.startXProperty().setValue(srcBound.getCenterX() );
        line.startYProperty().setValue(srcBound.getMaxY() );

        line.endXProperty().setValue(targetBound.getCenterX());
        line.endYProperty().setValue(targetBound.getMinY()-1);

        getChildren().add(line);
    }

}
