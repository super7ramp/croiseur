package com.gitlab.super7ramp.crosswords.gui.viewmodel;

import com.gitlab.super7ramp.crosswords.gui.control.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.control.model.IntCoordinate2D;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * The crossword view model.
 */
// TODO shouldn't depend on gui-controls, move control models somewhere else?
public final class CrosswordGridViewModel {

    /** The boxes of the view. */
    private final MapProperty<IntCoordinate2D, CrosswordBox> boxes;

    /**
     * Constructs an instance.
     */
    public CrosswordGridViewModel() {
        boxes = new SimpleMapProperty<>(this, "boxes", welcomeBoxes());
    }

    /**
     * Returns the default crossword grid view model.
     *
     * @return the default crossword grid view model
     */
    private static ObservableMap<IntCoordinate2D, CrosswordBox> welcomeBoxes() {
        final ObservableMap<IntCoordinate2D, CrosswordBox> welcomeBoxes =
                FXCollections.observableHashMap();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                welcomeBoxes.put(new IntCoordinate2D(i, j), new CrosswordBox());
            }
        }
        return welcomeBoxes;
    }

    /**
     * Returns the boxes.
     *
     * @return the boxes
     */
    public MapProperty<IntCoordinate2D, CrosswordBox> boxes() {
        return boxes;
    }

}
