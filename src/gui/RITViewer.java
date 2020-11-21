package gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.Math;

public class RITViewer extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        //Set title
        stage.setTitle("RITViewer");
        //Get fileInput
        Parameters params = getParameters();
        String filename = params.getRaw().get(0);
        FileReader fr = new FileReader(filename);
        Scanner scanner = new Scanner(fr);
        List<Integer> values = new ArrayList<>();
        while (scanner.hasNext()){
            values.add(scanner.nextInt());
        }
        int size = (int) Math.sqrt(values.size());

        //Make GridPane
        GridPane gridPane = new GridPane();
        gridPane.getCellBounds(size, size);
        for(int i=0; i<size;i++){
            for(int j=0;j<size; j++){
                //Create Canvas and fill with required color
                final Canvas canvas = new Canvas(1,1);
                GraphicsContext gc = canvas.getGraphicsContext2D();
                gc.setFill(Color.grayRgb(values.get(j*size+i)));
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                //Add to gridPane
                gridPane.add(canvas,  i, j, 1, 1);
            }
        }
        //
        Group root = new Group();
        root.getChildren().add(gridPane);
        Scene scene = new Scene(root, size, size, Color.BLACK);
        stage.setMinWidth(size);
        stage.setMinHeight(size);
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
