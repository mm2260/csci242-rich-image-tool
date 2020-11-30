package gui;

import gui.component.InteractiveScrollPane;
import gui.component.QuadTreeImageView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.RITQTCodec;
import model.RITQuadTree;

//TODO: add gui viewer documentation
public class RITViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("RITViewer");

        RITQuadTree quadTree = RITQTCodec.importFile( getParameters().getRaw().get(0) );
        ImageView diagram = new QuadTreeImageView( quadTree );
        InteractiveScrollPane viewport = new InteractiveScrollPane( diagram );

        Scene scene = new Scene(viewport, 512,  512);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}