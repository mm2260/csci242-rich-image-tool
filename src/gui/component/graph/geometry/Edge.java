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
        this(source, target, new Point2D(0,0));
    }

    public Edge(Cell source, Cell target, Point2D offset) {

        line = new Line();

//        System.out.println(target);

        Bounds srcBound = source.localToScene( source.getBoundsInLocal() );
        Bounds targetBound = target.localToScene( target.getBoundsInLocal() );

        line.startXProperty().setValue(srcBound.getCenterX()-offset.getX() );
        line.startYProperty().setValue(srcBound.getMaxY()-offset.getY() );

        line.endXProperty().setValue(targetBound.getCenterX()-offset.getX());
        line.endYProperty().setValue(targetBound.getMinY()-1-offset.getY());

        getChildren().add(line);
    }
}
