package gui;

import gui.component.InteractiveScrollPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

//TODO: add gui viewer documentation
public class RITViewer extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Image test = new Image("test.jpg");
        InteractiveScrollPane viewport = new InteractiveScrollPane( new ImageView(test) );

        Scene scene = new Scene(viewport, 512, 512);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}