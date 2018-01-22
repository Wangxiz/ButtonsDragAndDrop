package DragTest.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ButtonType {
    private final StringProperty typeName;

    public ButtonType() {
        this(null);
    }

    public ButtonType(String name) {
        typeName = new SimpleStringProperty(name);
    }

    public String getTypeName() {
        return typeName.get();
    }

    public void setTypename(String name) {
        typeName.set(name);
    }

    public StringProperty typeNameProperty() {
        return typeName;
    }
}
