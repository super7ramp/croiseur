package com.gitlab.super7ramp.crosswords.gui.viewmodel;

import com.gitlab.super7ramp.crosswords.gui.fx.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.fx.model.IntCoordinate2D;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;

/**
 * The crossword view model.
 */
public final class CrosswordViewModel {

    /** The boxes of the view. */
    private final MapProperty<IntCoordinate2D, CrosswordBox> boxes;

    private final IntegerProperty height;

    private final IntegerProperty width;

    /**
     * Constructs an instance.
     */
    public CrosswordViewModel() {
        boxes = new SimpleMapProperty<>(this, "boxes", FXCollections.observableHashMap());

        // Initial model is a 5x5 grid because why not
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boxes.put(new IntCoordinate2D(i, j), new CrosswordBox());
            }
        }

        height = new SimpleIntegerProperty(this, "height", 5);
        width = new SimpleIntegerProperty(this, "width", 5);
    }

    /**
     * Returns the boxes.
     *
     * @return the boxes
     */
    public MapProperty<IntCoordinate2D, CrosswordBox> boxes() {
        return boxes;
    }

    public ReadOnlyIntegerProperty height() {
        return height;
    }

    public ReadOnlyIntegerProperty width() {
        return width;
    }


}
