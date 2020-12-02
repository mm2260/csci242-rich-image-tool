package gui;

import gui.component.InteractiveScrollPane;
import gui.component.QuadTreeImageView;
import gui.component.graph.GraphLayout;
import gui.component.graph.geometry.Cell;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.RITQTCodec;
import model.RITQuadTree;
import model.util.ExceptionHandler;

import java.io.File;

/**
 * RIT GUI class. A interactive GUI to open and write quadtree representations of raw image data.
 * @author Nabeel Khan
 * @author Mohammed Mehboob
 */
public class RITGUI extends Application {

    /**
     * Main container.
     */
    private final VBox container;
    /**
     * Buffer to store various properties.
     */
    private final Buffer buffer = new Buffer();

    /**
     * Constructor.
     */
    public RITGUI() {
        this.container = new VBox();
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Rich Image Tool");

        this.addMenuBar();      // Add menu bar to window.
        this.addWorkingArea();  // Add working area.

        Scene scene = new Scene(container, 700,512);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Adds a menu bar with file operations.
     */
    private void addMenuBar() {
        // Create the menu bar variable
        MenuBar menuBar = new MenuBar();

        // Create the file menu
        Menu fileMenu = new Menu("File");
        MenuItem openFileMenuItem = new MenuItem("Open");
        MenuItem importFileMenuItem = new MenuItem("Import");

        // Export sub menu
        Menu exportFileMenu = new Menu("Export");
        MenuItem compressMenuItem = new MenuItem("to compressed file");
        MenuItem uncompressMenuItem = new MenuItem("to uncompressed file");

        // Add items
        exportFileMenu.getItems().addAll(compressMenuItem,uncompressMenuItem);

        // Exit button
        MenuItem exitMenuItem = new MenuItem("Exit");

        // Add items to file menu
        fileMenu.getItems().addAll(openFileMenuItem,importFileMenuItem,exportFileMenu);
        fileMenu.getItems().addAll(new SeparatorMenuItem(),exitMenuItem);

        // Add all menus to menu bar
        menuBar.getMenus().addAll(fileMenu);
        addToContainer(menuBar);

        // Set for exit menu option to close the window.
        exitMenuItem.setOnAction( e -> {System.exit(0);} );

        // FileChooser variable
        FileChooser fileChooser = new FileChooser();

        //
        openFileMenuItem.setOnAction( e -> {
            System.out.println("open file");
            fileChooser.setTitle("Open Compressed File");
            try {
                //
                fileChooser.getExtensionFilters().setAll( new FileChooser.ExtensionFilter("RIT","*.rit"));
                File file = fileChooser.showOpenDialog( menuBar.getScene().getWindow() );
                //
                buffer.inputPathProperty.setValue( file.getAbsolutePath() );
                //
                buffer.setQuadTree( RITQTCodec.openFile(file.getAbsolutePath()) );
            } catch (Exception exception ) { ExceptionHandler.handle(exception); }
        });

        //
        compressMenuItem.disableProperty().bind(buffer.imageLoadedIntoBuffer.not());
        //
        compressMenuItem.setOnAction( e -> {
            System.out.println("Export compressed file");
            fileChooser.setTitle("Export Compressed File");
            try {
                //
                fileChooser.getExtensionFilters().setAll( new FileChooser.ExtensionFilter("RIT","*.rit"));
                File file = fileChooser.showSaveDialog( menuBar.getScene().getWindow() );
                buffer.outputPathProperty.setValue(file.getAbsolutePath() );
                //
                RITQTCodec.exportCompressed( file.getAbsolutePath(), buffer.quadTree );
            } catch (Exception exception) {ExceptionHandler.handle(exception);}
        });

        //Show file open dialog.
        importFileMenuItem.setOnAction( e -> {
            System.out.println("import file");
            //
            fileChooser.getExtensionFilters().setAll( new FileChooser.ExtensionFilter("TXT","*.txt"));
            fileChooser.setTitle("Import Uncompressed File");
            try {
                File file = fileChooser.showOpenDialog( menuBar.getScene().getWindow() );
                //
                buffer.inputPathProperty.setValue( file.getAbsolutePath() );
                //
                buffer.setQuadTree( RITQTCodec.importFile(file.getAbsolutePath()) );
            } catch (Exception exception ) { ExceptionHandler.handle(exception); }
        });

        //Binding
        uncompressMenuItem.disableProperty().bind(buffer.imageLoadedIntoBuffer.not());
        //Open file save dialog.
        uncompressMenuItem.setOnAction( e -> {
            System.out.println("Export uncompressed file");
            fileChooser.setTitle("Export Un-Compressed File");
            try {
                //
                fileChooser.getExtensionFilters().setAll( new FileChooser.ExtensionFilter("TXT","*.txt"));
                File file = fileChooser.showSaveDialog( menuBar.getScene().getWindow() );
                buffer.outputPathProperty.setValue(file.getAbsolutePath() );
                //
                RITQTCodec.exportUncompressed( file.getAbsolutePath(), buffer.quadTree );
            } catch (Exception exception) {ExceptionHandler.handle(exception);}
        });
    }

    /**
     * Configure and Add the GUI working area.
     */
    private void addWorkingArea() {

        // Configure the tab area
        TabPane tabPane = new TabPane();
        configureTabPane(tabPane);

        // Configure the inspector area
        VBox inspector = new VBox(new Label("Inspector"), new Separator(Orientation.HORIZONTAL) );
        configureInspector(inspector);

        // Set up button functionality
        Button generateGraphButton = new Button("Generate Graph Representation");
        configureGenerateGraphButton(generateGraphButton, tabPane, inspector);

        // Add the two sections to the split
        SplitPane split = new SplitPane(tabPane, inspector );
        configureSplit(split);

        // Add the split
        addToContainer(split);
    }

    /**
     * Configure the tab pane in working area
     * @param tabPane tabPane variable
     */
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

    /**
     * Configure the inspector pane in working area
     * @param inspector the inspector container.
     */
    private void configureInspector(VBox inspector) {
        inspector.setPadding( new Insets(5, 5, 5, 5) );
        inspector.setSpacing(10);

        Label inputFileLabel = new Label("Input File:");
        Label outputFileLabel = new Label("Output File:");
        TextField inputTextField = new TextField();
        Button selectInputButton = new Button("Select Input");
        TextField outputTextField = new TextField();
        Button selectOutputButton = new Button("Select Output");

        selectInputButton.prefWidthProperty().bind(inspector.widthProperty());
        selectOutputButton.prefWidthProperty().bind(inspector.widthProperty());

        selectInputButton.setOnAction( e -> {
            chooseInputFile(inspector.getScene().getWindow());
        } );
        selectOutputButton.setOnAction( e -> {
            chooseOutputFile(inspector.getScene().getWindow());
        });

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
            } catch (Exception exception ) { ExceptionHandler.guiHandle(exception); }
        } );

        compressButton.prefWidthProperty().bind(inspector.widthProperty());
        compressButton.disableProperty().bind( (buffer.outputPathSetProperty
                                                .and(buffer.inputPathSetProperty)
                                               ).not()  );
        compressButton.setOnAction( e -> {
            try {
                compress(buffer.inputPathProperty.get(), buffer.outputPathProperty.get());
            } catch (Exception exception) { ExceptionHandler.guiHandle(exception); }
        } );

        uncompressButton.setOnAction( e -> {
            try {
                uncompress(buffer.inputPathProperty.get(), buffer.outputPathProperty.get());
            } catch (Exception exception) { ExceptionHandler.guiHandle(exception); }
        } );

        uncompressButton.prefWidthProperty().bind(inspector.widthProperty());
        uncompressButton.disableProperty().bind( (buffer.outputPathSetProperty
                                                .and(buffer.inputPathSetProperty)
                                               ).not()  );

        hintLabel1.setWrapText(true);
        hintLabel1.setFont(new Font("Arial", 10));
        hintLabel2.setWrapText(true);
        hintLabel2.setFont(new Font("Arial", 10));

        inspector.getChildren().addAll( inputFileLabel, inputTextField, selectInputButton, outputFileLabel, outputTextField, selectOutputButton );
        inspector.getChildren().addAll(hintLabel1, viewButton, hintLabel2, compressButton, uncompressButton);
        inspector.getChildren().add( new Separator(Orientation.HORIZONTAL) );
    }

    /**
     * Configure functionality for generate graph button.
     * @param generateGraphButton button variable
     * @param tabPane container for all tabs
     * @param inspector inspector container.
     */
    private void configureGenerateGraphButton(Button generateGraphButton, TabPane tabPane, VBox inspector) {

        Label hintLabel1 = new Label("HINT: to generate representations, an image must already be loaded into the buffer.");
        hintLabel1.setWrapText(true);
        hintLabel1.setFont(new Font("Arial", 10));

        generateGraphButton.prefWidthProperty().bind(inspector.widthProperty());
        generateGraphButton.disableProperty().bind(buffer.imageLoadedIntoBuffer.not());

        generateGraphButton.setOnAction( e -> {
            Tab graphTab = new Tab("Graph Represenation");

            GraphLayout graphLayout = new GraphLayout( new Cell(buffer.quadTree.getRoot()) );
            tabPane.getTabs().add(graphTab);
            tabPane.getSelectionModel().select(graphTab);
            VBox viewport = new InteractiveScrollPane(graphLayout).getCentered();
            graphTab.setContent( viewport );
        } );

        inspector.getChildren().addAll(hintLabel1,generateGraphButton);
    }

    /**
     * Configure split in working area
     * @param split split variable
     */
    private void configureSplit(SplitPane split) {
        split.setDividerPositions(0.70, 0.30);
        VBox.setVgrow(split, Priority.ALWAYS);
    }

    /**
     * Add a node to primary container.
     * @param node node to be added
     */
    private void addToContainer(Node node) {
        this.container.getChildren().add(node);
    }

    /**
     * Buffer class to contain observables. Used for various binding purposes.
     */
    private static class Buffer {
        /**
         * Input set property
         */
        BooleanProperty inputPathSetProperty = new SimpleBooleanProperty(false);
        /**
         * Input Path
         */
        StringProperty inputPathProperty = new SimpleStringProperty("<null>");
        /**
         * Output set property
         */
        BooleanProperty outputPathSetProperty = new SimpleBooleanProperty(false);
        /**
         * Output path
         */
        StringProperty outputPathProperty = new SimpleStringProperty("<null>");
        /**
         * Image Object Property
         */
        ObjectProperty<Image> imageObjectProperty = new SimpleObjectProperty<>(new Image("checkerboard.jpg"));
        /**
         * Image Loaded Bool
         */
        BooleanProperty imageLoadedIntoBuffer = new SimpleBooleanProperty(false);
        /**
         * Quad Tree variable
         */
        RITQuadTree quadTree = null;

        /**
         * Constructor for a new buffer.
         */
        public Buffer() {
            inputPathSetProperty.bind( inputPathProperty.isNotEqualTo("<null>") );
            outputPathSetProperty.bind( outputPathProperty.isNotEqualTo("<null>") );
        }

        /**
         * Set the buffer's quad tree
         * @param quadTree quad tree variable to be set in the buffer
         */
        public void setQuadTree(RITQuadTree quadTree) {
            this.quadTree = quadTree;
            //Affect all observers
            updateProperties();
        }

        /**
         * Update all properties related to the quad tree when the quad tree is changed / set.
         */
        private void updateProperties() {
            imageObjectProperty.setValue(new QuadTreeImageView(quadTree).getImage() );
            imageLoadedIntoBuffer.setValue(true);
        }
    }

    private static void compress(String inputFilePath, String outputFilePath) throws Exception {

        if ( !inputFilePath.endsWith(".txt") || !outputFilePath.endsWith(".rit")  ) {
            throw new RuntimeException("Incorrect file types for compression.");
        }

        System.out.println("Compressing: "+inputFilePath);
        RITQuadTree quadTree = RITQTCodec.importFile(inputFilePath);    //
        System.out.println("QTree: "+quadTree.getRepresentation());     //
        RITQTCodec.exportCompressed(outputFilePath, quadTree);          //
        System.out.printf("Compressed image size: %d%n", quadTree.getRepresentation().size());
        double rawImageSize = quadTree.getImageData().getLength();
        double compressedSize = quadTree.getRepresentation().size();
        System.out.println("Raw image size: "+rawImageSize);
        System.out.println("Compressed image size: "+compressedSize);
        double compressionRatio = 1 - compressedSize/rawImageSize;
        System.out.printf("Compression %%%.1f",compressionRatio*100);
    }

    private static void uncompress(String inputFilePath, String outputFilePath) throws Exception {

        if ( !outputFilePath.endsWith(".txt") || !inputFilePath.endsWith(".rit")  ) {
            throw new RuntimeException("Incorrect file types for compression.");
        }

        System.out.println("Uncompressing: "+ inputFilePath);
        RITQuadTree quadTree = RITQTCodec.openFile(inputFilePath);  //
        System.out.printf("QTree: %s%n", quadTree.getImageData());  //
        RITQTCodec.exportUncompressed(outputFilePath, quadTree);    //
    }

    private void chooseInputFile(Window window) {
        // FileChooser variable
        FileChooser fileChooser = new FileChooser();
        System.out.println("open file");
        fileChooser.setTitle("Open File");
        try {
            fileChooser.getExtensionFilters().setAll( new FileChooser.ExtensionFilter("RIT","*.rit"),
                                                      new FileChooser.ExtensionFilter("TXT","*.txt"));
            File file = fileChooser.showOpenDialog( window );
            buffer.inputPathProperty.setValue( file.getAbsolutePath() );
        } catch (Exception exception ) { ExceptionHandler.guiHandle(exception); }
    }
    private void chooseOutputFile(Window window) {
        // FileChooser variable
        FileChooser fileChooser = new FileChooser();
        System.out.println("output file");
        fileChooser.setTitle("Save File");
        try {
            fileChooser.getExtensionFilters().setAll( new FileChooser.ExtensionFilter("RIT","*.rit"),
                                                      new FileChooser.ExtensionFilter("TXT","*.txt"));
            File file = fileChooser.showSaveDialog( window );
            buffer.outputPathProperty.setValue( file.getAbsolutePath() );
        } catch (Exception exception ) { ExceptionHandler.guiHandle(exception); }
    }
}