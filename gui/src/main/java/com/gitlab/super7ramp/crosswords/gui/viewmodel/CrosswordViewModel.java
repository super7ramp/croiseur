package com.gitlab.super7ramp.crosswords.gui.viewmodel;

import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.view.model.IntCoordinate2D;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

/**
 * The crossword view model.
 */
public final class CrosswordViewModel {

    /** The boxes of the view. */
    private final MapProperty<IntCoordinate2D, CrosswordBox> boxes;

    /**
     * Constructs an instance.
     */
    public CrosswordViewModel() {
        boxes = new SimpleMapProperty<>(this, "boxes", welcomeBoxes());
    }

    private static ObservableMap<IntCoordinate2D, CrosswordBox> welcomeBoxes() {
        final ObservableMap<IntCoordinate2D, CrosswordBox> welcomeBoxes =
                FXCollections.observableHashMap();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                welcomeBoxes.put(new IntCoordinate2D(i, j), new CrosswordBox());
            }
        }
        // TODO why shaded doesn't update the view?
        //welcomeBoxes.get(new IntCoordinate2D(0, 1)).shadedProperty().set(true);
        //welcomeBoxes.get(new IntCoordinate2D(2, 5)).shadedProperty().set(true);
        //welcomeBoxes.get(new IntCoordinate2D(4, 4)).shadedProperty().set(true);
        //welcomeBoxes.get(new IntCoordinate2D(0, 5)).shadedProperty().set(true);
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