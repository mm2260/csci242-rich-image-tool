package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

//TODO: add gui documentation
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class RITGUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        //Constants
        File INITIAL_DIRECTORY = new File("src");
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 600,600);


        //Text Fields
        Text name = new Text("RICH IMAGE TOOL\n");
        name.setFont(Font.font("times", FontWeight.BOLD, FontPosture.REGULAR, 20));
        Text inputFileName = new Text("Selected input file: Please select");
        Text outputFileName = new Text("Selected output file: Please select");



        //Button with reset functionality
        Button home = new Button("Reset");
        home.setMinSize(200,50);
        home.setOnAction((event)->{
            stage.hide();
            try {
                start(new Stage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //Button with compress function
        Button compress = new Button("Compress Image");
        compress.setMinSize(200,50);

        //Button with uncompress functionality
        Button uncompress = new Button("Uncompress Image");
        uncompress.setMinSize(200,50);

        //Button to view graph
        Button graph= new Button("Graph");
        graph.setMinSize(200,50);


        //Store input and output files in array
        ArrayList<File> inputFiles = new ArrayList<>();

        //Button to select input file
        Button inputFile = new Button("Select input file");
        inputFile.setMinSize(200,50);
        inputFile.setOnAction((event)->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Input File");
            fileChooser.setInitialDirectory(INITIAL_DIRECTORY);
            inputFiles.add(0,fileChooser.showOpenDialog(stage));
            inputFileName.setText("Selected input file: " + inputFiles.get(0));
            System.out.println("File: " + inputFiles.get(0).getName() + ".");
        });


        //Button to select output file
        Button outputFile = new Button("Select output file");
        outputFile.setMinSize(200,50);
        outputFile.setOnAction((event)->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Output File");
            fileChooser.setInitialDirectory(INITIAL_DIRECTORY);
            System.out.println(inputFiles.size());
            if(inputFiles.size()==1){
                inputFiles.add(1,fileChooser.showOpenDialog(stage));
                System.out.println("File: " + inputFiles.get(0).getName() + ".");
                outputFileName.setText("Selected output file: " + inputFiles.get(1));
            }else{
                outputFileName.setText("Selected output file: Select input file first");
            }

        });


        //add buttons to side bar
        VBox sideBar = new VBox();
        sideBar.getChildren().addAll(home,compress,uncompress,graph,inputFile,outputFile);


        //Add text fields to bottom rows
        VBox filesSelected = new VBox();
        filesSelected.getChildren().addAll(inputFileName,outputFileName);


        //Set top bar
        HBox topBar = new HBox();
        topBar.setAlignment(Pos.CENTER);
        topBar.getChildren().addAll(name);

        //Set borderPane
        borderPane.setLeft(sideBar);
        borderPane.setTop(topBar);
        borderPane.setBottom(filesSelected);





        stage.setScene(scene);


        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
