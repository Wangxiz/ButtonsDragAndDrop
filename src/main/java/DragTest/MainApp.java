package DragTest;

import DragTest.model.Type;
import DragTest.model.TypeWrapper;
import DragTest.view.DragTestController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.prefs.Preferences;

public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;
    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Type> types = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public MainApp() {
        // Try to load last opened person file.
        File file = getTypesFilePath();
        if (file != null) {
            loadButtonTypesFromFile(file);
        }
    }

    /**
     * Returns the data as an observable list of Persons.
     * @return
     */
    public ObservableList<Type> getTypes() {
        return types;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {
        ObservableList<Screen> screenList = Screen.getScreens();
        System.out.println("Screens Count: " + screenList.size());
        // Print the details of all screens
        for(Screen screen: screenList) {
            System.out.println(screen);
        }

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
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            primaryStage.setX(visualBounds.getMinX());
            primaryStage.setY(visualBounds.getMinY());
            primaryStage.setWidth(visualBounds.getWidth());
            primaryStage.setHeight(visualBounds.getHeight());
//            primaryStage.setResizable(false);
//            primaryStage.setFullScreen(true);

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

            String style = "-fx-background-color: " + color + ";";

            button.setStyle(style);
            button.setText(color);

            // A browser.
            WebView  webView = new WebView();
            WebEngine webEngine = webView.getEngine();
            webEngine.loadContent("<h3>Note:</h3> This is a " + color + " Button");

            Tooltip  tooltip = new Tooltip();
            tooltip.setPrefSize(200, 100);
            tooltip.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            // Set tooltip content
            tooltip.setGraphic(webView);
            button.setTooltip(tooltip);
            button.setOnMouseClicked(event -> {
                System.out.println(primaryStage.getScene().getFocusOwner());
                System.out.println("Hello, " + color + " Wanting!");
            });

            return button;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public File getTypesFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("dragFilePath", null);

        if(filePath != null) return new File(filePath);
        else return null;
    }

    public void setTypesFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);

        if(file != null) prefs.put("dragFilePath", file.getPath());
        else prefs.remove("dragFilePath");
    }

    public void saveButtonTypesToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(TypeWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            TypeWrapper wrapper = new TypeWrapper();
            wrapper.setTypes(types);
            m.marshal(wrapper, file);

            setTypesFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not save data to file:\n" + file.getPath());
            alert.showAndWait();
        }
    }

    public void loadButtonTypesFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(TypeWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            TypeWrapper wrapper = (TypeWrapper) um.unmarshal(file);
            types.clear();
            types.addAll(wrapper.getTypes());

            setTypesFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Could not load data from file:\n" + file.getPath());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
