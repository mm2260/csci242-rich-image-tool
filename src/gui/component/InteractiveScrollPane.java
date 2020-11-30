package gui.component;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Scale;

//TODO: add interactive scroll pane documentation
//TODO: add references
public class InteractiveScrollPane extends ScrollPane {

    private static final int MAX_SCALE = 50;
    private static final double MIN_SCALE = 0.05;

    Group innerRoot;
    Scale scaleTransform;
    Node content;

    DoubleProperty scale;
    double delta = 0.1;

    public InteractiveScrollPane(Node content) {
        pannableProperty().set(true);

        this.content = content;
        this.innerRoot = new Group();
        Group contentGroup = new Group();
        contentGroup.getChildren().add(innerRoot);
        innerRoot.getChildren().add(content);
        setContent(contentGroup);

        this.scaleTransform = new Scale();
        this.scale = new SimpleDoubleProperty(1.0);
        scaleTransform.xProperty().bind(scale);
        scaleTransform.yProperty().bind(scale);
        innerRoot.getTransforms().add(scaleTransform);

        innerRoot.setOnScroll(onScrollEventHandler);

    }

    private final EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent scrollEvent) {

            if(scrollEvent.getDeltaY() < 0 ) {
                scale.setValue( clamp(scale.get()-delta, MAX_SCALE, MIN_SCALE) );
            } else {
                scale.setValue( clamp(scale.get()+delta, MAX_SCALE, MIN_SCALE) );
            }

            scrollEvent.consume();
        }
    };

    private Image resample(Image input, int scaleFactor) {
        final int W = (int) input.getWidth();
        final int H = (int) input.getHeight();
        final int S = scaleFactor;

        WritableImage output = new WritableImage(
                W * S,
                H * S
        );

        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                final int argb = reader.getArgb(x, y);
                for (int dy = 0; dy < S; dy++) {
                    for (int dx = 0; dx < S; dx++) {
                        writer.setArgb(x * S + dx, y * S + dy, argb);
                    }
                }
            }
        }

        return output;
    }

    private double clamp(double value, double max, double min) {
        if( value > max ) {
            return max;
        } else if ( value < min ) {
            return min;
        } else {
            return value;
        }
    }
}
