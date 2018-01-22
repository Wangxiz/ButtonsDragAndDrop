package DragTest.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ButtonController {
    @FXML
    Button button;

    @FXML
    private void initialize() {

    }

    @FXML
    void OnClick() {
        System.out.println("Hello, world!");
    }

    public Button getButton() {
        return button;
    }
}
