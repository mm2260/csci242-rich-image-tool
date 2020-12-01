package gui;

import gui.component.InteractiveScrollPane;
import gui.component.QuadTreeImageView;
import gui.component.graph.GraphLayout;
import gui.component.graph.geometry.Cell;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.RITQTCodec;
import model.RITQuadTree;
import model.util.ExceptionHandler;

import java.io.File;

public class RITGUI extends Application {

    private final VBox container;
    private final Buffer buffer = new Buffer();

    public RITGUI() {
        this.container = new VBox();
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Rich Image Tool");

        this.addMenuBar();
        this.addWorkingArea();

        Scene scene = new Scene(container, 700,512);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void addMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem openFileMenuItem = new MenuItem("Open");
        MenuItem importFileMenuItem = new MenuItem("Import");
        MenuItem exportFileMenuItem = new MenuItem("Export");
        fileMenu.getItems().addAll(openFileMenuItem,importFileMenuItem,exportFileMenuItem);

        menuBar.getMenus().addAll(fileMenu);
        addToContainer(menuBar);

        FileChooser fileChooser = new FileChooser();

        openFileMenuItem.setOnAction( e -> {
            System.out.println("open file");
            fileChooser.setTitle("Open Compressed File");
            try {
                fileChooser.getExtensionFilters().setAll( new FileChooser.ExtensionFilter("RIT","*.rit"));
                File file = fileChooser.showOpenDialog( menuBar.getScene().getWindow() );
                buffer.inputPathProperty.setValue( file.getAbsolutePath() );
                buffer.setQuadTree( RITQTCodec.openFile(file.getAbsolutePath()) );
            } catch (Exception exception ) { ExceptionHandler.handle(exception); }
        });

        importFileMenuItem.setOnAction( e -> {
            System.out.println("import file");
            fileChooser.getExtensionFilters().setAll( new FileChooser.ExtensionFilter("TXT","*.txt"));
            fileChooser.setTitle("Import Uncompressed File");
            try {
                File file = fileChooser.showOpenDialog( menuBar.getScene().getWindow() );
                buffer.inputPathProperty.setValue( file.getAbsolutePath() );
                buffer.setQuadTree( RITQTCodec.importFile(file.getAbsolutePath()) );
            } catch (Exception exception ) { ExceptionHandler.handle(exception); }
        });
    }

    private void addWorkingArea() {

        TabPane tabPane = new TabPane();
        configureTabPane(tabPane);

        VBox inspector = new VBox(new Label("Inspector"), new Separator(Orientation.HORIZONTAL) );
        configureInspector(inspector);

        Button generateGraphButton = new Button("Generate Graph Representation");
        configureGenerateGraphButton(generateGraphButton, tabPane, inspector);

        SplitPane split = new SplitPane(tabPane, inspector );
        configureSplit(split);

        addToContainer(split);
    }

    private void configureTabPane(TabPane tabPane) {
        Tab imageViewerTab = new Tab("Image View");
        imageViewerTab.setClosable(false);

        ImageView imageView = new ImageView();
        imageView.imageProperty().bind( this.buffer.imageObjectProperty );

        InteractiveScrollPane viewport = new InteractiveScrollPane( imageView );
        imageViewerTab.setContent( viewport.getCentered() );

        tabPane.getTabs().addAll(imageViewerTab);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
    }

    private void configureInspector(VBox inspector) {
        inspector.setPadding( new Insets(5, 5, 5, 5) );
        inspector.setSpacing(10);

        Label inputFileLabel = new Label("Input File:");
        Label outputFileLabel = new Label("Output File:");
        TextField inputTextField = new TextField();
        TextField outputTextField = new TextField();

        Button viewButton = new Button("View");
        Button compressButton = new Button("Compress");
        Button uncompressButton = new Button("Uncompress");

        Label hintLabel1 = new Label("HINT: to view, input field must be set.");
        Label hintLabel2 = new Label("HINT: to compress and uncompress, both input and output fields must be set.");

        inputTextField.textProperty().bindBidirectional(this.buffer.inputPathProperty);
        outputTextField.textProperty().bindBidirectional(this.buffer.outputPathProperty);

        viewButton.prefWidthProperty().bind(inspector.widthProperty());
        viewButton.disableProperty().bind(buffer.inputPathSetProperty.not() );

        viewButton.setOnAction( e -> {
            try {
                String path = buffer.inputPathProperty.get();
                if (path.charAt(path.length() - 2) == 'x') {
                    buffer.setQuadTree(RITQTCodec.importFile(buffer.inputPathProperty.get()));
                } else {
                    buffer.setQuadTree(RITQTCodec.openFile(buffer.inputPathProperty.get()));
                }
            } catch (Exception exception ) { ExceptionHandler.handle(exception); }
        } );

        compressButton.prefWidthProperty().bind(inspector.widthProperty());
        compressButton.disableProperty().bind( (buffer.outputPathSetProperty
                                                .and(buffer.inputPathSetProperty)
                                               ).not()  );

        uncompressButton.prefWidthProperty().bind(inspector.widthProperty());
        uncompressButton.disableProperty().bind( (buffer.outputPathSetProperty
                                                .and(buffer.inputPathSetProperty)
                                               ).not()  );

        hintLabel1.setWrapText(true);
        hintLabel1.setFont(new Font("Arial", 10));
        hintLabel2.setWrapText(true);
        hintLabel2.setFont(new Font("Arial", 10));

        inspector.getChildren().addAll( inputFileLabel, inputTextField, outputFileLabel, outputTextField );
        inspector.getChildren().addAll(hintLabel1, viewButton, hintLabel2, compressButton, uncompressButton);
        inspector.getChildren().add( new Separator(Orientation.HORIZONTAL) );
    }

    private void configureGenerateGraphButton(Button generateGraphButton, TabPane tabPane, VBox inspector) {

        generateGraphButton.prefWidthProperty().bind(inspector.widthProperty());
        generateGraphButton.setOnAction( e -> {
            Tab graphTab = new Tab("Graph Represenation");

            GraphLayout graphLayout = new GraphLayout( new Cell(buffer.quadTree.getRoot()) );
            tabPane.getTabs().add(graphTab);
            tabPane.getSelectionModel().select(graphTab);
            VBox viewport = new InteractiveScrollPane(graphLayout).getCentered();
            graphTab.setContent( viewport );
        } );

        inspector.getChildren().add(generateGraphButton);
    }

    private void configureSplit(SplitPane split) {
        split.setDividerPositions(0.70, 0.30);
        VBox.setVgrow(split, Priority.ALWAYS);
    }

    private void addToContainer(Node node) {
        this.container.getChildren().add(node);
    }

//    private static void exportFile(ActionEvent actionEvent) {
//        System.out.println("export file");
//
//        MenuItem menuItem = (MenuItem)actionEvent.getTarget();
//        ContextMenu menu = menuItem.getParentPopup();
//        Scene scene = menu.getScene();
//
//        File file = showFileChooser(dialogType.EXPORT_COMPRESSED, scene.getWindow() );
//        buffer.outputPathProperty.setValue(file.getAbsolutePath());
//    }
//
//    private enum dialogType {
//        OPEN,
//        IMPORT,
//        EXPORT_COMPRESSED,
//        EXPORT_UNCOMPRESSED
//    }
//
//    private static File showFileChooser(dialogType type, Window stage) {
//        FileChooser fileChooser = new FileChooser();
//        switch (type) {
//            case OPEN -> fileChooser.setTitle("Open Compressed File");
//            case IMPORT -> fileChooser.setTitle("Import Un-Compressed File");
//            case EXPORT_COMPRESSED -> {
//                fileChooser.setTitle("Export Compressed Data to File");
//                return fileChooser.showSaveDialog(stage);
//            }
//            case EXPORT_UNCOMPRESSED -> {
//                fileChooser.setTitle("Export Un-Compressed Data to File");
//                return fileChooser.showSaveDialog(stage);
//            }
//        }
//        return fileChooser.showOpenDialog(stage);
//    }

    private static class Buffer {
        BooleanProperty inputPathSetProperty = new SimpleBooleanProperty(false);
        StringProperty inputPathProperty = new SimpleStringProperty("<null>");
        BooleanProperty outputPathSetProperty = new SimpleBooleanProperty(false);
        StringProperty outputPathProperty = new SimpleStringProperty("<null>");
        ObjectProperty<Image> imageObjectProperty = new SimpleObjectProperty<>(new Image("test.jpg"));
        RITQuadTree quadTree = null;

        public Buffer() {
            inputPathSetProperty.bind( inputPathProperty.isNotEqualTo("<null>") );
            outputPathSetProperty.bind( outputPathProperty.isNotEqualTo("<null>") );
        }
        public void setQuadTree(RITQuadTree quadTree) {
            this.quadTree = quadTree;
            updateProperties();
        }

        private void updateProperties() {
            imageObjectProperty.setValue(new QuadTreeImageView(quadTree).getImage() );
        }
    }
}