package DragTest.view;

import DragTest.MainApp;
import DragTest.model.ButtonType;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class DragTestController {
    // Reference to the main application
    private MainApp mainApp;
    static final DataFormat BUTTON_TYPE = new DataFormat("ButtonType");

    public DragTestController() {}

    private Color[] colors = {
            Color.YELLOW,
            Color.BLUE,
            Color.GREEN,
            Color.BLACK,
            Color.GRAY
    };

    @FXML
    private TableView<ButtonType> buttonTable;

    @FXML
    private TableColumn<ButtonType, String> buttonTypeColumn;

    @FXML
    private GridPane grid;

//    @FXML
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
        buttonTypeColumn.setStyle("-fx-alignment: CENTER;");

        // source
        buttonTable.setOnDragDetected(event -> {
            int index = buttonTable.getSelectionModel().getFocusedIndex();
            System.out.println("Select: " + index);
            // Initiate a drag-and-drop gesture
            Dragboard dragboard = buttonTable.startDragAndDrop(TransferMode.COPY_OR_MOVE);

            // Put the the selected items to the dragboard
            ClipboardContent content = new ClipboardContent();
            content.put(BUTTON_TYPE, index);

            dragboard.setContent(content);
            event.consume();
        });

        buttonTable.setOnDragDone(event -> {
            System.out.println("Source Done!");
            event.consume();
        });

        for(GridPane pane: panes) {
            pane.setOnDragDropped(event -> {
                boolean dragCompleted = false;
                System.out.println("Target dropped!");
                // Transfer the data to the target
                Dragboard dragboard = event.getDragboard();

                if (dragboard.hasContent(BUTTON_TYPE)) {
                    int index = (int) dragboard.getContent(BUTTON_TYPE);
                    Button button = mainApp.newButton(index);
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
                if (event.getGestureSource() != pane && dragboard.hasContent(BUTTON_TYPE)) {
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                event.consume();
            });

            pane.setOnDragDone(event -> {
                System.out.println("Target Done!");
                event.consume();
            });
        }
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        buttonTable.setItems(mainApp.getButtonTypes());
    }
}
