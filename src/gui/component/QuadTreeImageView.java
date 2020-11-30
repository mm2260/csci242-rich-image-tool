package gui.component;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import model.RITQuadTree;

public class QuadTreeImageView extends ImageView {

    public QuadTreeImageView(RITQuadTree quadTree) {
        this(quadTree, resampleAmount(quadTree.getImageData().getSize()));
    }

    public QuadTreeImageView(RITQuadTree quadTree, int pixelSize) {
        this.setImage( generateGrayscaleImage(quadTree.getImageData(),pixelSize ));
    }

    private Image generateGrayscaleImage(RITQuadTree.DataArray imageData, int pixelSize) {
        int width, height;
        width=height=imageData.getSize();

        WritableImage grayscaleImage = new WritableImage(width*pixelSize, height*pixelSize);
        PixelWriter pixelWriter = grayscaleImage.getPixelWriter();
        for( int y = 0; y < height; ++y ) {
            for ( int x = 0; x < width; ++x ) {
                //Get pixel values from file one-by-one.
                Color color = Color.grayRgb( imageData.get(y,x) );
                for (int dy = 0; dy < pixelSize; dy++) {
                    for (int dx = 0; dx < pixelSize; dx++) {
                        pixelWriter.setColor(x * pixelSize + dx, y * pixelSize + dy, color);
                    }
                }
            }
        }
        return grayscaleImage;
    }

    private static int resampleAmount( int size ) {
        if (size<=8){ return 50; }
        else if (size>8 && size<=32) { return 15; }
        else if (size>32 && size<=128) { return 10; }
        else { return 2; }
    }
}
