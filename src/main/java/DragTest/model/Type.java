package DragTest.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Type {
    private final StringProperty typeName;

    public Type() {
        this("");
    }

    public Type(String name) {
        typeName = new SimpleStringProperty(name);
    }

    public String getTypeName() {
        return typeName.get();
    }

    public void setTypeName(String name) {
        this.typeName.set(name);
    }

    public StringProperty typeNameProperty() {
        return typeName;
    }

    @Override
    public String toString() {
        return typeName.get();
    }
}
