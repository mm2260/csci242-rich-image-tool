package gui;

import gui.component.InteractiveScrollPane;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

public class RITGUI extends Application {

    private final VBox container;
    private static final Buffer buffer = new Buffer();

    public RITGUI() {
        this.container = new VBox();
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Rich Image Tool");

        this.addMenuBar();
        this.addWorkingArea();

        Scene scene = new Scene(container, 612,512);
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private void addMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu file = new Menu("File");
        MenuItem openFile = new MenuItem("Open");
        MenuItem importFile = new MenuItem("Import");
        MenuItem exportFile = new MenuItem("Export");

        openFile.setOnAction(RITGUI::openFile);
        importFile.setOnAction(RITGUI::importFile);
        exportFile.setOnAction(RITGUI::exportFile);

        file.getItems().addAll(openFile,importFile,exportFile);

        menuBar.getMenus().addAll(file);
        addToContainer(menuBar);
    }

    private void addWorkingArea() {

        TabPane tabPane = new TabPane();
        configureTabPane(tabPane);

        VBox inspector = new VBox(new Label("Inspector"), new Separator(Orientation.HORIZONTAL) );
        inspector.setPadding( new Insets(5, 5, 5, 5) );
        inspector.setSpacing(10);

        Label inputFileLabel = new Label("Input File:");
        TextField inputTextField = new TextField();
        inputTextField.textProperty().bindBidirectional(this.buffer.inputPathProperty);
        Label outputFileLabel = new Label("Output File:");
        TextField outputTextField = new TextField();
        outputTextField.textProperty().bindBidirectional(this.buffer.outputPathProperty);

        inspector.getChildren().addAll( inputFileLabel, inputTextField, outputFileLabel, outputTextField );

        SplitPane split = new SplitPane(tabPane, inspector );
        split.setDividerPositions(0.75, 0.25);
        VBox.setVgrow(split, Priority.ALWAYS);

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

    private void addToContainer(Node node) {
        this.container.getChildren().add(node);
    }

    private static void openFile(ActionEvent actionEvent) {
        System.out.println("open file");

        MenuItem menuItem = (MenuItem)actionEvent.getTarget();
        ContextMenu menu = menuItem.getParentPopup();
        Scene scene = menu.getScene();

        File file = showFileChooser(dialogType.OPEN, scene.getWindow() );
        buffer.inputPathProperty.setValue( file.getAbsolutePath() );
    }

    private static void importFile(ActionEvent actionEvent) {
        System.out.println("import file");

        MenuItem menuItem = (MenuItem)actionEvent.getTarget();
        ContextMenu menu = menuItem.getParentPopup();
        Scene scene = menu.getScene();

        File file = showFileChooser(dialogType.IMPORT, scene.getWindow() );
        buffer.inputPathProperty.setValue(file.getAbsolutePath());
    }

    private static void exportFile(ActionEvent actionEvent) {
        System.out.println("export file");

        MenuItem menuItem = (MenuItem)actionEvent.getTarget();
        ContextMenu menu = menuItem.getParentPopup();
        Scene scene = menu.getScene();

        File file = showFileChooser(dialogType.EXPORT_COMPRESSED, scene.getWindow() );
        buffer.outputPathProperty.setValue(file.getAbsolutePath());
    }

    private enum dialogType {
        OPEN,
        IMPORT,
        EXPORT_COMPRESSED,
        EXPORT_UNCOMPRESSED
    }

    private static File showFileChooser(dialogType type, Window stage) {
        FileChooser fileChooser = new FileChooser();
        switch (type) {
            case OPEN -> fileChooser.setTitle("Open Compressed File");
            case IMPORT -> fileChooser.setTitle("Import Un-Compressed File");
            case EXPORT_COMPRESSED -> {
                fileChooser.setTitle("Export Compressed Data to File");
                return fileChooser.showSaveDialog(stage);
            }
            case EXPORT_UNCOMPRESSED -> {
                fileChooser.setTitle("Export Un-Compressed Data to File");
                return fileChooser.showSaveDialog(stage);
            }
        }
        return fileChooser.showOpenDialog(stage);
    }

    private static class Buffer {
        StringProperty inputPathProperty = new SimpleStringProperty("<null>");
        StringProperty outputPathProperty = new SimpleStringProperty("<null>");
        ObjectProperty<Image> imageObjectProperty = new SimpleObjectProperty<>(new Image("test.jpg"));
    }
}