package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.InputStream;

public class RITGUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {


        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 600,600);



        //Buttons
        Button home = new Button("Home");
        home.setMinSize(200,50);

        Button compress = new Button("Compress Image");
        compress.setMinSize(200,50);

        Button uncompress = new Button("Uncompress Image");
        uncompress.setMinSize(200,50);

        Button viewWithNodes = new Button("View with RITQNodes");
        viewWithNodes.setMinSize(200,50);

        Button inputFile = new Button("Select input file");
        inputFile.setMinSize(200,50);

        Button outputFile = new Button("Select output file");
        outputFile.setMinSize(200,50);

        //add buttons to side bar
        VBox sideBar = new VBox();
        sideBar.getChildren().addAll(compress,uncompress,viewWithNodes, inputFile, outputFile);

        //file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        //fileChooser.showOpenDialog(stage);


        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER);

        //Text
        Text name = new Text("RICH IMAGE TOOL\n");
        name.setFont(Font.font("times", FontWeight.BOLD, FontPosture.REGULAR, 20));
        topBar.getChildren().addAll(name);
        borderPane.setLeft(sideBar);
        borderPane.setTop(topBar);



        stage.setScene(scene);


        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
