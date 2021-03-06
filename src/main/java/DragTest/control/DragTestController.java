package DragTest.control;

import DragTest.MainApp;
import DragTest.model.Type;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.Optional;

public class DragTestController {
    // Reference to the main application
    private MainApp mainApp;

    @FXML
    private TableView<Type> buttonTable;

    @FXML
    private TableColumn<Type, String> buttonTypeColumn;

    @FXML
    private ListView<Type> buttonList;

    private GridPane[] panes = new GridPane[12];
    @FXML
    private GridPane pane00;
    @FXML
    private GridPane pane01;
    @FXML
    private GridPane pane10;
    @FXML
    private GridPane pane11;
    @FXML
    private GridPane pane20;
    @FXML
    private GridPane pane21;
    @FXML
    private GridPane pane30;
    @FXML
    private GridPane pane31;
    @FXML
    private GridPane pane40;
    @FXML
    private GridPane pane41;
    @FXML
    private GridPane pane50;
    @FXML
    private GridPane pane51;

    public DragTestController() {}

    @FXML
    private void initialize() {
        panes[0] = pane00;
        panes[1] = pane01;
        panes[2] = pane10;
        panes[3] = pane11;
        panes[4] = pane20;
        panes[5] = pane21;
        panes[6] = pane30;
        panes[7] = pane31;
        panes[8] = pane40;
        panes[9] = pane41;
        panes[10] = pane50;
        panes[11] = pane51;

        // Initialize the person table with the two columns.
        buttonTypeColumn.setCellValueFactory(cellData -> cellData.getValue().typeNameProperty());

        // source
        buttonTable.setOnDragDetected(event -> {
            int index = buttonTable.getSelectionModel().getFocusedIndex();
            String color = mainApp.getTypes().get(index).getTypeName();

            // Initiate a drag-and-drop gesture
            Dragboard dragboard = buttonTable.startDragAndDrop(TransferMode.COPY_OR_MOVE);

            // Put the the selected items to the drag board
            ClipboardContent content = new ClipboardContent();
            content.putString(color);

            dragboard.setContent(content);
            event.consume();
        });

        // System.out.println("Source Done!");
        buttonTable.setOnDragDone(Event::consume);

        for(GridPane pane: panes) {
            pane.setOnDragDropped(event -> {
                boolean dragCompleted = false;
                // Transfer the data to the target
                Dragboard dragboard = event.getDragboard();

                if (dragboard.hasString()) {
                    String color = dragboard.getString();
                    Button button = mainApp.newButton(color);
                    button.setPrefSize(pane.getWidth(), pane.getHeight());
                    button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                    pane.getChildren().add(button);

                    // Data transfer is successful
                    dragCompleted = true;
                }

                // Data transfer is not successful
                event.setDropCompleted(dragCompleted);
                event.consume();
            });

            pane.setOnDragOver(event -> {
                Dragboard dragboard = event.getDragboard();
                if (event.getGestureSource() != pane && dragboard.hasString()) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });

            // System.out.println("Target Done!");
            pane.setOnDragDone(Event::consume);
        }
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        buttonTable.setItems(mainApp.getTypes());
        buttonList.setItems(mainApp.getTypes());
    }

    @FXML
    private void handleNew() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("New"); //設定對話框視窗的標題列文字
        textInputDialog.setHeaderText(null); //設定對話框視窗裡的標頭文字。若設為空字串，則表示無標頭
        textInputDialog.setContentText("Input a new Button Type:"); //設定對話框的訊息文字
        final Optional<String> opt = textInputDialog.showAndWait();
        String res;

        try {
            res = opt.get();
        } catch (final NoSuchElementException e) {
            res = null;
        }
        System.out.println(res);
        if(res != null) {
            Type type = new Type(res);
            mainApp.getTypes().add(type);
        }
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/types"));

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if (file != null) {
            mainApp.loadButtonTypesFromFile(file);
        }
    }

    @FXML
    private void handleSave() {
        File personFile = mainApp.getTypesFilePath();
        if (personFile != null) {
            mainApp.saveButtonTypesToFile(personFile);
        } else {
            handleSaveAs();
        }
    }

    @FXML
    private void handleSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("src/main/resources/types"));

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show save file dialog
        File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());

        if (file != null) {
            // Make sure it has the correct extension
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mainApp.saveButtonTypesToFile(file);
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    @FXML
    private void handlePress() {
        System.out.println(mainApp.getPrimaryStage().getScene().getFocusOwner());
    }

    @FXML
    private void handleList() {
        buttonTable.setVisible(false);
        buttonTable.toBack();
        buttonList.setVisible(true);
    }

    @FXML
    private void handleTable() {
        buttonList.setVisible(false);
        buttonList.toBack();
        buttonTable.setVisible(true);
    }
}
