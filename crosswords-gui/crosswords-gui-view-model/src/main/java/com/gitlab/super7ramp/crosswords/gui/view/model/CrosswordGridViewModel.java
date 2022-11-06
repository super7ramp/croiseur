package com.gitlab.super7ramp.crosswords.gui.view.model;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * The crossword view model.
 */
public final class CrosswordGridViewModel {

    /** The boxes of the view. */
    private final MapProperty<GridPosition, CrosswordBox> boxes;

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
    private static ObservableMap<GridPosition, CrosswordBox> welcomeBoxes() {
        final ObservableMap<GridPosition, CrosswordBox> welcomeBoxes =
                FXCollections.observableHashMap();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                welcomeBoxes.put(new GridPosition(i, j), new CrosswordBox());
            }
        }
        return welcomeBoxes;
    }

    /**
     * Returns the boxes.
     *
     * @return the boxes
     */
    public MapProperty<GridPosition, CrosswordBox> boxes() {
        return boxes;
    }

}
