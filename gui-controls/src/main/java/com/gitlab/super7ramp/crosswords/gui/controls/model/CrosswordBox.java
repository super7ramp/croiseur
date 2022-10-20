package com.gitlab.super7ramp.crosswords.gui.controls.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Crossword box model.
 */
public final class CrosswordBox {

    /** Whether the box is shaded or not. */
    private final BooleanProperty shaded;

    /** The content of the box. */
    private final StringProperty content;

    /**
     * Constructs an instance.
     */
    public CrosswordBox() {
        shaded = new SimpleBooleanProperty(this, "shaded", false);
        content = new SimpleStringProperty(this, "content", "");
    }

    /**
     * Returns the boolean property indicating whether the box is shaded.
     *
     * @return the boolean property indicating whether the box is shaded
     */
    public BooleanProperty shadedProperty() {
        return shaded;
    }

    /**
     * Returns the content as string property.
     *
     * @return the content as string property
     */
    public StringProperty contentProperty() {
        return content;
    }


}
