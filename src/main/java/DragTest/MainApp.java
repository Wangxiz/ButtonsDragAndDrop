package DragTest;

import DragTest.model.ButtonType;
import DragTest.view.DragTestController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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

//        this.primaryStage.getIcons().add(new Image("images/address_books.png"));
        try {
            // Load root layout from fxml file.
            // Create the FXMLLoader
            FXMLLoader loader = new FXMLLoader();
            // Path to the FXML File
            String fxmlDocPath = "src/main/java/DragTest/view/dragtest.fxml";
            FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);

            // Create the Pane and all Details
            rootLayout = loader.load(fxmlStream);

            // Show the scene containing the root layout.
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

    public Button newButton(int index) {
        try {
            FXMLLoader loader = new FXMLLoader();
            // Path to the FXML File
            String fxmlDocPath = "src/main/java/DragTest/view/button.fxml";
            FileInputStream fxmlStream = new FileInputStream(fxmlDocPath);
            Button button = loader.load(fxmlStream);
            String bk_color;
            switch (index) {
                case 0: bk_color = "-fx-background-color: yellow;"; break;
                case 1: bk_color = "-fx-background-color: blue;"; break;
                case 2: bk_color = "-fx-background-color: green;"; break;
                case 3: bk_color = "-fx-background-color: black;"; break;
                case 4: bk_color = "-fx-background-color: gray;"; break;
                default: bk_color = "-fx-background-color: yellow;";
            }
            button.setStyle(bk_color);
            return button;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
