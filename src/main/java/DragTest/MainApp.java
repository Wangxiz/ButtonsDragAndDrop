package DragTest;

import DragTest.model.ButtonType;
import DragTest.view.DragTestController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;

public class MainApp extends Application {
    private Stage primaryStage;
    private AnchorPane rootLayout;
    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<ButtonType> buttonTypes = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
        // Add some sample data
        buttonTypes.add(new ButtonType("Yellow"));
        buttonTypes.add(new ButtonType("Blue"));
        buttonTypes.add(new ButtonType("Green"));
        buttonTypes.add(new ButtonType("Black"));
        buttonTypes.add(new ButtonType("Gray"));
    }

    /**
     * Returns the data as an observable list of Persons.
     * @return
     */
    public ObservableList<ButtonType> getButtonTypes() {
        return buttonTypes;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("DragTest App");

        this.primaryStage.getIcons().add(new Image("images/drag.png"));
        try {
            FXMLLoader loader = new FXMLLoader();
            String fxmlDocPath = "src/main/java/DragTest/view/dragtest.fxml";
            FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
            rootLayout = loader.load(fxmlStream);
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // Give the controller access to the main app.
            DragTestController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Button newButton(String color) {
        try {
            FXMLLoader loader = new FXMLLoader();
            String fxmlDocPath = "src/main/java/DragTest/view/button.fxml";
            FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
            Button button = loader.load(fxmlStream);

            String style =
                    "-fx-background-color: " + color + ";";
//                            +
//                    "-fx-alignment: center;" +
//                    "-fx-text-alignment: center;" +
//                    "-fx-text-fill: rgba(255, 255, 255);" +
//                    "-fx-font-size: 20px;";
            button.setStyle(style);
            button.setText(color);
//            button.setTextAlignment(TextAlignment.CENTER);

            // A browser.
            WebView  webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.loadContent("<h3>Note:</h3> This is a " + color + " Button");

            Tooltip  tooltip = new Tooltip();
            tooltip.setPrefSize(300, 180);
            tooltip.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            // Set tooltip content
            tooltip.setGraphic(webView);
            button.setTooltip(tooltip);

            return button;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
