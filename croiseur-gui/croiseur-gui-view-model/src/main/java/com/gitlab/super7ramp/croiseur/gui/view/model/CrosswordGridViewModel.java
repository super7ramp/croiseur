/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
import java.util.function.Predicate;

import static java.util.Comparator.comparingInt;
import static java.util.function.Predicate.not;

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
         * A listener attached to each box model shading property. Allows to trigger slot position
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
         * A listener attached to each box model content property. Allows to trigger slot content
         * re-computation when content change.
         */
        private final class ContentChangeListener implements InvalidationListener {

            /** The coordinate of the listened box. */
            private final GridPosition listenedBoxCoordinate;

            /**
             * Constructs an instance.
             *
             * @param listenedBoxCoordinateArg the listened box coordinate
             */
            ContentChangeListener(final GridPosition listenedBoxCoordinateArg) {
                listenedBoxCoordinate = listenedBoxCoordinateArg;
            }

            @Override
            public void invalidated(final Observable observable) {
                if (currentSlotPositions.contains(listenedBoxCoordinate)) {
                    recomputeSlotContent();
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

        /**
         * The current slot as a pattern. Empty boxes are replaced by dots ('.'). An empty string if
         * {@link #currentSlotPositions} is empty.
         */
        private final ReadOnlyStringWrapper currentSlotContent;

        /** The orientation of the current slot (or the previous slot if current slot is empty). */
        private final BooleanProperty isCurrentSlotVertical;

        /** A cache of listeners on the shading property of boxes. */
        private final Map<GridPosition, ChangeListener<Boolean>> boxShadingListeners;

        /** A cache of listeners on the shading property of boxes. */
        private final Map<GridPosition, InvalidationListener> boxContentListeners;

        /**
         * Constructs an instance.
         */
        WorkingArea() {
            currentBoxPosition = new SimpleObjectProperty<>(this, "currentBoxPosition");
            currentSlotPositions = new ReadOnlyListWrapper<>(this, "currentSlotPositions",
                                                             FXCollections.observableArrayList());
            currentSlotContent = new ReadOnlyStringWrapper(this, "currentSlotContent", "");
            isCurrentSlotVertical = new SimpleBooleanProperty(this, "isCurrentSlotVertical");
            boxShadingListeners = new HashMap<>();
            boxContentListeners = new HashMap<>();

            currentSlotPositions.addListener(this::onSlotPositionsChange);
            currentBoxPosition.addListener(this::onCurrentBoxChange);
            isCurrentSlotVertical.addListener(observable -> recomputeSlotPositions());
            columnCount.addListener(this::onDimensionChange);
            rowCount.addListener(this::onDimensionChange);
            boxes.forEach(this::onBoxAdded);
            boxes.addListener(this::onGridChange);
        }

        /**
         * Processes a grid change (i.e. a box is added or removed).
         *
         * @param change the grid change
         */
        private void onGridChange(
                final MapChangeListener.Change<? extends GridPosition, ? extends CrosswordBoxViewModel> change) {
            if (change.wasAdded()) {
                if (change.getValueRemoved() != null) {
                    throw new UnsupportedOperationException(
                            "Replacing box models is not supported");
                }
                onBoxAdded(change.getKey(), change.getValueAdded());
            } else {
                onBoxRemoved(change.getKey(), change.getValueRemoved());
            }
        }

        /**
         * Registers box listeners.
         *
         * @param coordinate the coordinate of the added box
         * @param box        the added box
         */
        private void onBoxAdded(final GridPosition coordinate, final CrosswordBoxViewModel box) {
            final var shadingChangeListener = new ShadingChangeListener(coordinate);
            boxShadingListeners.put(coordinate, shadingChangeListener);
            box.shadedProperty().addListener(shadingChangeListener);

            final var contentChangeListener = new ContentChangeListener(coordinate);
            boxContentListeners.put(coordinate, contentChangeListener);
            box.contentProperty().addListener(contentChangeListener);
        }

        /**
         * Unregisters box listeners.
         *
         * @param coordinate the removed box coordinate
         * @param box        the removed box
         */
        private void onBoxRemoved(final GridPosition coordinate, final CrosswordBoxViewModel box) {
            final var shadingChangeListener = boxShadingListeners.remove(coordinate);
            box.shadedProperty().removeListener(shadingChangeListener);

            final var contentChangeListener = boxContentListeners.remove(coordinate);
            box.contentProperty().removeListener(contentChangeListener);
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
            slotBoxes.sort(COMPARING_BY_LINE_THEN_BY_ROW);
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
            slotBoxes.sort(COMPARING_BY_LINE_THEN_BY_ROW);
            currentSlotPositions.setAll(slotBoxes);
        }

        /**
         * Recomputes {@link #currentSlotContent} based on {@link #currentSlotPositions} and
         * {@link #boxes}.
         */
        private void recomputeSlotContent() {
            final StringBuilder contentBuilder = new StringBuilder(currentSlotPositions.size());
            for (final GridPosition position : currentSlotPositions) {
                final String letter = boxes.get(position).getContent();
                contentBuilder.append(letter.isEmpty() ? "." : letter);
            }
            currentSlotContent.set(contentBuilder.toString());
        }

        /**
         * Updates the box models on slot positions change, effectively updating display.
         *
         * @param change the slot change
         */
        private void onSlotPositionsChange(
                final ListChangeListener.Change<? extends GridPosition> change) {
            while (change.next()) {
                change.getRemoved().stream()
                      .map(boxes::get)
                      .filter(Objects::nonNull) // Box may have been removed from grid
                      .forEach(boxModel -> boxModel.setHighlighted(false));
                change.getAddedSubList().stream()
                      .map(boxes::get)
                      .forEach(boxModel -> boxModel.setHighlighted(true));
                recomputeSlotContent();
            }
        }
    }

    /** A comparator to sort the boxes. */
    private static final Comparator<GridPosition> COMPARING_BY_LINE_THEN_BY_ROW =
            comparingInt(GridPosition::x).thenComparing(GridPosition::y);

    /** The maximum number of rows or columns. */
    private static final int MAX_ROW_COLUMN_COUNT = 20;

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
        return columnCount.getReadOnlyProperty();
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
        return rowCount.getReadOnlyProperty();
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
     * The current slot positions property. The list value is empty if no slot is selected.
     *
     * @return the current slot positions
     */
    public ReadOnlyListProperty<GridPosition> currentSlotPositionsProperty() {
        return workingArea.currentSlotPositions.getReadOnlyProperty();
    }

    /**
     * The current slot content.
     * <p>
     * The String value is empty if no slot is selected. Any non-filled box content is replaced by a
     * dot ('.').
     *
     * @return the current slot content
     */
    public String currentSlotContent() {
        return workingArea.currentSlotContent.get();
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
     * Creates an empty column at the right of the grid.
     */
    public void addColumn() {
        final int newColumnIndex = columnCount.get();
        if (newColumnIndex >= MAX_ROW_COLUMN_COUNT) {
            return;
        }
        for (int row = 0; (row < rowCount.get()) || (row == 0); row++) {
            final GridPosition coordinate = new GridPosition(newColumnIndex, row);
            boxes.put(coordinate, new CrosswordBoxViewModel());
        }
    }

    /**
     * Creates an empty row at the bottom of the grid.
     */
    public void addRow() {
        final int newRowIndex = rowCount.get();
        if (newRowIndex >= MAX_ROW_COLUMN_COUNT) {
            return;
        }
        for (int column = 0; (column < columnCount.get()) || (column == 0); column++) {
            final GridPosition coordinate = new GridPosition(column, newRowIndex);
            boxes.put(coordinate, new CrosswordBoxViewModel());
        }
    }

    /**
     * Deletes the last row (reading top to bottom, so the row at the bottom of the grid).
     */
    public void deleteLastRow() {
        final int oldRowCount = rowCount.get();
        if (oldRowCount == 0) {
            return;
        }
        final int deletedRowIndex = oldRowCount - 1;
        for (int column = 0; column < columnCount.get(); column++) {
            final GridPosition coordinate = new GridPosition(column, deletedRowIndex);
            boxes.remove(coordinate);
        }
    }

    /**
     * Deletes the last column (reading left to right, so the column on the right of the grid).
     */
    public void deleteLastColumn() {
        final int oldColumnCount = columnCount.get();
        if (oldColumnCount == 0) {
            return;
        }
        final int deletedColumnIndex = oldColumnCount - 1;
        for (int row = 0; row < rowCount.get(); row++) {
            final GridPosition coordinate = new GridPosition(deletedColumnIndex, row);
            boxes.remove(coordinate);
        }
    }

    /**
     * Clears the entire grid, including its structure, i.e. box nodes are removed from the grid.
     */
    public void clear() {
        boxes.clear();
    }

    /**
     * Resets the grid content (both shaded and non-shaded boxes).
     * <p>
     * This method preserves the structure of the grid, box nodes are not removed.
     */
    public void resetContentAll() {
        resetContent(box -> true);
    }

    /**
     * Resets the grid content (only non-shaded boxes).
     * <p>
     * This method preserves the structure of the grid, box nodes are not removed.
     */
    public void resetContentLettersOnly() {
        resetContent(not(CrosswordBoxViewModel::isShaded));
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

    /**
     * Resets the boxes matching the given predicate.
     *
     * @param predicate filters the boxes to be reset
     * @see CrosswordBoxViewModel#resetExceptHighlight()
     */
    private void resetContent(final Predicate<CrosswordBoxViewModel> predicate) {
        boxes.values().stream().filter(predicate)
             .forEach(CrosswordBoxViewModel::resetExceptHighlight);
    }

}
