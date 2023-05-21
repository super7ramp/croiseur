/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.Comparator.comparingInt;

/**
 * The crossword view model.
 */
public final class CrosswordGridViewModel {

    /**
     * The area under work, which consists of the currently focused box and the slot (either
     * horizontal or vertical) it belongs to.
     */
    private final class WorkingArea {

        /**
         * A listener attached to each box model shading property. Allows to trigger slot
         * re-computation when shading change.
         */
        private final class ShadingChangeListener implements ChangeListener<Boolean> {

            /** The coordinate of the listened box. */
            private final GridPosition listenedBoxCoordinate;

            /**
             * Constructs an instance.
             *
             * @param listenedBoxCoordinateArg the listened box coordinate
             */
            ShadingChangeListener(final GridPosition listenedBoxCoordinateArg) {
                listenedBoxCoordinate = listenedBoxCoordinateArg;
            }

            @Override
            public void changed(final ObservableValue<? extends Boolean> observable,
                                final Boolean wasShaded,
                                final Boolean isShaded) {
                final GridPosition current = currentBoxPosition.get();
                if (current != null &&
                    ((isCurrentSlotVertical.get() && current.x() == listenedBoxCoordinate.x()) ||
                     (!isCurrentSlotVertical.get() && current.y() == listenedBoxCoordinate.y()))) {
                    recomputeSlotPositions();
                }
            }
        }

        /**
         * The position of the box being worked on. The value it contains is {@code null} if no box
         * has been focused or the last focused box has been deleted.
         */
        private final ObjectProperty<GridPosition> currentBoxPosition;

        /**
         * The current slot, described by the positions of the boxes it contains. The list value is
         * empty if no slot is focused.
         */
        private final ReadOnlyListWrapper<GridPosition> currentSlotPositions;

        /** The orientation of the current slot (or the previous slot if current slot is empty). */
        private final BooleanProperty isCurrentSlotVertical;

        /** A cache of listeners on the shading property of boxes. */
        private final Map<GridPosition, ChangeListener<Boolean>> boxShadingListeners;

        /**
         * Constructs an instance.
         */
        WorkingArea() {
            currentBoxPosition = new SimpleObjectProperty<>(this, "currentBoxPosition");
            currentSlotPositions = new ReadOnlyListWrapper<>(this, "currentSlotPositions",
                                                             FXCollections.observableArrayList());
            isCurrentSlotVertical = new SimpleBooleanProperty(this, "isCurrentSlotVertical");
            boxShadingListeners = new HashMap<>();

            currentBoxPosition.addListener(this::onCurrentBoxChange);
            currentSlotPositions.addListener(this::onSlotChange);
            isCurrentSlotVertical.addListener(observable -> recomputeSlotPositions());
            columnCount.addListener(this::onDimensionChange);
            rowCount.addListener(this::onDimensionChange);
            for (final Map.Entry<GridPosition, CrosswordBoxViewModel> box : boxes.entrySet()) {
                final ChangeListener<Boolean> listener = new ShadingChangeListener(box.getKey());
                boxShadingListeners.put(box.getKey(), listener);
                box.getValue().shadedProperty().addListener(listener);
            }
            boxes.addListener(this::onGridChange);
        }

        private void onGridChange(
                final MapChangeListener.Change<? extends GridPosition, ? extends CrosswordBoxViewModel> change) {
            if (change.wasAdded()) {
                if (change.getValueRemoved() != null) {
                    throw new UnsupportedOperationException(
                            "Replacing box models is not supported");
                }
                final GridPosition coordinate = change.getKey();
                final ChangeListener<Boolean> listener = new ShadingChangeListener(coordinate);
                boxShadingListeners.put(coordinate, listener);
                change.getValueAdded().shadedProperty().addListener(listener);
            } else {
                final ChangeListener<Boolean> listener =
                        boxShadingListeners.remove(change.getKey());
                change.getValueRemoved().shadedProperty().removeListener(listener);
            }
        }

        /**
         * Processes a current box change.
         *
         * @param observable     the current box observable
         * @param oldBoxPosition the previous box position
         * @param newBoxPosition the current box position
         */
        private void onCurrentBoxChange(final ObservableValue<? extends GridPosition> observable,
                                        final GridPosition oldBoxPosition,
                                        final GridPosition newBoxPosition) {
            if (!currentSlotPositions.contains(newBoxPosition)) {
                recomputeSlotPositions();
            } else {
                // Current box has moved to a box of the same slot, nothing to do.
            }
        }

        /**
         * Processes a column or row count change.
         *
         * @param observable   the observable
         * @param newDimension the new value
         * @param oldDimension the old value
         */
        private void onDimensionChange(final ObservableValue<? extends Number> observable,
                                       final Number oldDimension, final Number newDimension) {
            final GridPosition current = currentBoxPosition.get();
            if (current != null && !boxes.containsKey(current)) {
                currentBoxPosition.set(null);
            } else {
                recomputeSlotPositions();
            }
        }

        /**
         * Recomputes the {@link #currentSlotPositions}.
         */
        private void recomputeSlotPositions() {
            final GridPosition current = currentBoxPosition.get();
            if (current == null || boxes.get(current).isShaded()) {
                if (!currentSlotPositions.isEmpty()) {
                    currentSlotPositions.clear();
                }
            } else if (isCurrentSlotVertical.get()) {
                recomputeVerticalSlotPositions();
            } else {
                recomputeHorizontalSlotPositions();
            }
        }

        /**
         * Recomputes the positions of current when it is horizontal.
         */
        private void recomputeHorizontalSlotPositions() {
            final GridPosition current = currentBoxPosition.get();
            final List<GridPosition> slotBoxes = new ArrayList<>();
            slotBoxes.add(current);
            for (int column = current.x() - 1; column >= 0; column--) {
                final GridPosition position = new GridPosition(column, current.y());
                final CrosswordBoxViewModel box = boxes.get(position);
                if (box.isShaded()) {
                    break;
                }
                slotBoxes.add(position);
            }
            for (int column = current.x() + 1; column < columnCount.get(); column++) {
                final GridPosition position = new GridPosition(column, current.y());
                final CrosswordBoxViewModel box = boxes.get(position);
                if (box.isShaded()) {
                    break;
                }
                slotBoxes.add(position);
            }
            currentSlotPositions.setAll(slotBoxes);
        }

        /**
         * Recomputes the positions of the current slot when it is vertical.
         */
        private void recomputeVerticalSlotPositions() {
            final GridPosition current = currentBoxPosition.get();
            final List<GridPosition> slotBoxes = new ArrayList<>();
            slotBoxes.add(current);
            for (int row = current.y() - 1; row >= 0; row--) {
                final GridPosition position = new GridPosition(current.x(), row);
                final CrosswordBoxViewModel box = boxes.get(position);
                if (box.isShaded()) {
                    break;
                }
                slotBoxes.add(position);
            }
            for (int row = current.y() + 1; row < rowCount.get(); row++) {
                final GridPosition position = new GridPosition(current.x(), row);
                final CrosswordBoxViewModel box = boxes.get(position);
                if (box.isShaded()) {
                    break;
                }
                slotBoxes.add(position);
            }
            currentSlotPositions.setAll(slotBoxes);
        }

        /**
         * Updates the box models on slot change, effectively updating display.
         *
         * @param change the slot change
         */
        private void onSlotChange(final ListChangeListener.Change<? extends GridPosition> change) {
            while (change.next()) {
                change.getRemoved().stream()
                      .map(boxes::get)
                      .filter(Objects::nonNull) // Box may have been removed from grid
                      .forEach(boxModel -> boxModel.setHighlighted(false));
                change.getAddedSubList().stream()
                      .map(boxes::get)
                      .forEach(boxModel -> boxModel.setHighlighted(true));
            }
        }
    }

    /** A comparator to sort the boxes. */
    private static final Comparator<GridPosition> COMPARING_BY_LINE_THEN_BY_ROW =
            comparingInt(GridPosition::x).thenComparing(GridPosition::y);

    /** The sorted map backing {@link #boxes}. */
    private final SortedMap<GridPosition, CrosswordBoxViewModel> sortedBoxes;

    /** The boxes of the view. */
    private final MapProperty<GridPosition, CrosswordBoxViewModel> boxes;

    /** The number of columns. */
    private final ReadOnlyIntegerWrapper columnCount;

    /** The number of rows. */
    private final ReadOnlyIntegerWrapper rowCount;

    /** The working area. */
    private final WorkingArea workingArea;

    /**
     * Constructs an instance.
     *
     * @param boxesArg the initial grid
     */
    private CrosswordGridViewModel(final SortedMap<GridPosition, CrosswordBoxViewModel> boxesArg) {
        sortedBoxes = boxesArg;
        boxes = new SimpleMapProperty<>(this, "boxes", FXCollections.observableMap(boxesArg));
        columnCount = new ReadOnlyIntegerWrapper(this, "width");
        rowCount = new ReadOnlyIntegerWrapper(this, "height");
        reevaluateDimensions();
        workingArea = new WorkingArea();
        boxes.addListener((InvalidationListener) observable -> reevaluateDimensions());
    }

    /**
     * Creates a new grid view model without any boxes.
     *
     * @return a new grid view model without any boxes
     */
    public static CrosswordGridViewModel newGrid() {
        final SortedMap<GridPosition, CrosswordBoxViewModel> map =
                new TreeMap<>(COMPARING_BY_LINE_THEN_BY_ROW);
        return new CrosswordGridViewModel(map);
    }

    /**
     * Creates a grid view model representing a 6x7 grid.
     *
     * @return a grid view model representing a 6x7 grid
     */
    public static CrosswordGridViewModel welcomeGrid() {
        final SortedMap<GridPosition, CrosswordBoxViewModel> welcomeBoxes =
                new TreeMap<>(COMPARING_BY_LINE_THEN_BY_ROW);
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                welcomeBoxes.put(new GridPosition(i, j), new CrosswordBoxViewModel());
            }
        }
        return new CrosswordGridViewModel(welcomeBoxes);
    }

    /**
     * Returns the boxes property.
     *
     * @return the boxes property
     */
    public MapProperty<GridPosition, CrosswordBoxViewModel> boxesProperty() {
        return boxes;
    }

    /**
     * Returns the column count property.
     * <p>
     * Incomplete columns are not counted.
     *
     * @return the column count property
     */
    public ReadOnlyIntegerProperty columnCountProperty() {
        return columnCount;
    }

    /**
     * Returns the column count.
     * <p>
     * Incomplete columns are not counted.
     *
     * @return the column count
     */
    public int columnCount() {
        return columnCount.get();
    }

    /**
     * Returns the row count property.
     * <p>
     * Incomplete rows are not counted.
     *
     * @return the row count property
     */
    public ReadOnlyIntegerProperty rowCountProperty() {
        return rowCount;
    }

    /**
     * Returns the row count.
     * <p>
     * Incomplete rows are not counted.
     *
     * @return the row count
     */
    public int rowCount() {
        return rowCount.get();
    }

    /**
     * The current box position property. The value it contains is {@code null} if no box has been
     * focused or the last focused box has been deleted.
     *
     * @return the current box
     */
    public ObjectProperty<GridPosition> currentBoxPositionProperty() {
        return workingArea.currentBoxPosition;
    }

    /**
     * The current slot property, described by the positions of the boxes it contains. The list
     * value is empty if no slot is focused.
     *
     * @return the current slot
     */
    public ReadOnlyListProperty<GridPosition> currentSlotPositionsProperty() {
        return workingArea.currentSlotPositions;
    }

    /**
     * The orientation property of the current slot (or the previous slot if current slot is
     * empty).
     *
     * @return the orientation of the current slot
     */
    public BooleanProperty isCurrentSlotVerticalProperty() {
        return workingArea.isCurrentSlotVertical;
    }

    /**
     * The orientation of the current slot (or the previous slot if current slot is empty).
     *
     * @return the orientation of the current slot
     */
    public boolean isCurrentSlotVertical() {
        return workingArea.isCurrentSlotVertical.get();
    }

    /**
     * Reevaluates column and row counts.
     */
    private void reevaluateDimensions() {
        if (isGridConsistent()) {
            if (sortedBoxes.isEmpty()) {
                columnCount.set(0);
                rowCount.set(0);
            } else {
                final GridPosition lastPosition = sortedBoxes.lastKey();
                columnCount.set(lastPosition.x() + 1);
                rowCount.set(lastPosition.y() + 1);
            }
        }
    }

    /**
     * Whether the grid can be considered consistent, i.e. not in the middle of adding a row or a
     * column.
     *
     * @return {@code true} if the grid can be considered consistent
     */
    private boolean isGridConsistent() {
        final boolean consistent;
        if (sortedBoxes.isEmpty()) {
            consistent = true;
        } else {
            final GridPosition lastPosition = sortedBoxes.lastKey();
            consistent = (lastPosition.x() + 1) * (lastPosition.y() + 1) == sortedBoxes.size();
        }
        return consistent;
    }
}
