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
    private Node view;
    private int depth;

    private List<Cell> children;

    public static final int CELL_SIZE = 30;

    public Cell(RITQTNode node) {
        this(node, 0);
    }

    private Cell(RITQTNode node, int depth) {
        this.node = node;
        this.depth = depth;
        addCellChildren();

        setView();
        this.getChildren().add(this.view);
    }

    private void addCellChildren() {
        if(node.getVal()!=-1) {
            return;
        } else {
            Cell ul = new Cell( node.getUpperLeft(), depth+1  );
            Cell ur = new Cell( node.getUpperRight(), depth+1 );
            Cell ll = new Cell( node.getLowerLeft(), depth+1  );
            Cell lr = new Cell( node.getLowerRight(), depth+1 );
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
            Rectangle rect = new Rectangle( CELL_SIZE, CELL_SIZE );
            rect.setStroke(Color.BLACK);
            rect.setFill(Color.RED);
            this.view = rect;
        }
    }

    public List<Cell> getCellChildren() { return children; }

    public boolean isLeaf() { return this.children==null; }

    @Override
    public String toString() {
        return String.format("DEPTH: %d, VALUE: %d",depth, node.getVal());
    }
}
