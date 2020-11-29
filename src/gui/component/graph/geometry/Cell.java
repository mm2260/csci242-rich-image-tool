package gui.component.graph.geometry;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.RITQTNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cell extends Group {

    private final RITQTNode node;

    private List<Cell> children;

    public static final int CELL_SIZE = 30;
    public Node view;

    public Cell(RITQTNode node) {
        this.node = node;
        addCellChildren();

        setView();
        this.getChildren().add(this.view);
    }

    private void addCellChildren() {
        if(node.getVal()!=-1) {
            return;
        } else {
            Cell ul = new Cell( node.getUpperLeft()  );
            Cell ur = new Cell( node.getUpperRight() );
            Cell ll = new Cell( node.getLowerLeft()  );
            Cell lr = new Cell( node.getLowerRight() );
            this.children = new ArrayList<>(Arrays.asList(ul, ur, ll, lr) );
        }
    }

    private void setView() {
        int val = node.getVal();
        if(val!=-1) {
            Rectangle rect = new Rectangle( CELL_SIZE, CELL_SIZE );
            rect.setStroke(Color.BLACK);
            rect.setFill(Color.grayRgb(node.getVal()));
            VBox container = new VBox(rect, new Label(String.valueOf(node.getVal())));
            container.setAlignment(Pos.CENTER);
            this.view = container;
        } else {
            double size = CELL_SIZE / Math.sqrt(2);
            Rectangle rect = new Rectangle( size, size );
            rect.rotateProperty().setValue(45);
            rect.setStroke(Color.BLACK);
            rect.setFill(Color.grayRgb(33));
            this.view = rect;
        }
    }

    public List<Cell> getCellChildren() { return children; }

    public boolean isLeaf() { return this.children==null; }

}
