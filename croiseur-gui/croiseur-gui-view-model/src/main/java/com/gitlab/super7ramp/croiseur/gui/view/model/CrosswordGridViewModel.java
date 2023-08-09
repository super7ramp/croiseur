/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.gui.view.model.slot.SlotOutline;
import com.gitlab.super7ramp.croiseur.gui.view.model.slot.SlotsViewModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static java.util.Comparator.comparingInt;
import static java.util.function.Predicate.not;

/**
 * The crossword grid view model.
 */
public final class CrosswordGridViewModel {

    /**
     * The area under work, which consists of the last focused box and the slot (either horizontal
     * or vertical) it belongs to.
     */
    // TODO refactor: On-the-fly current slot computation can be avoided now there is a SlotsViewModel
    private final class WorkingArea {

        /**
         * A listener attached to each box model shading property. Allows to trigger slot position
         * re-computation when shading change.
         */
        private final class ShadingChangeListener implements ChangeListener<Boolean> {

            /** The coordinate of the listened box. */
            private final GridCoord listenedBoxCoordinate;

            /**
             * Constructs an instance.
             *
             * @param listenedBoxCoordinateArg the listened box coordinate
             */
            ShadingChangeListener(final GridCoord listenedBoxCoordinateArg) {
                listenedBoxCoordinate = listenedBoxCoordinateArg;
            }

            @Override
            public void changed(final ObservableValue<? extends Boolean> observable,
                                final Boolean wasShaded, final Boolean isShaded) {
                final GridCoord current = currentBoxPosition.get();
                if (current != null &&
                    ((currentSlotVertical.get() &&
                      current.column() == listenedBoxCoordinate.column()) ||
                     (!currentSlotVertical.get() &&
                      current.row() == listenedBoxCoordinate.row()))) {
                    recomputeCurrentSlotPositions();
                }
            }
        }

        /**
         * A listener attached to each box model content property. Allows to trigger slot content
         * re-computation when content change.
         */
        private final class ContentChangeListener implements InvalidationListener {

            /** The coordinate of the listened box. */
            private final GridCoord listenedBoxCoordinate;

            /**
             * Constructs an instance.
             *
             * @param listenedBoxCoordinateArg the listened box coordinate
             */
            ContentChangeListener(final GridCoord listenedBoxCoordinateArg) {
                listenedBoxCoordinate = listenedBoxCoordinateArg;
            }

            @Override
            public void invalidated(final Observable observable) {
                if (!inhibitContentChangeEvents &&
                    currentSlotPositions.contains(listenedBoxCoordinate)) {
                    recomputeCurrentSlotContent();
                }
            }
        }

        /** A comparator to sort the boxes. */
        private static final Comparator<GridCoord> COMPARING_BY_COLUMN_THEN_BY_ROW =
                comparingInt(GridCoord::column).thenComparing(GridCoord::row);

        /**
         * The position of the box being worked on. The value it contains is {@code null} if no box
         * has been focused or the last focused box has been deleted.
         */
        private final ObjectProperty<GridCoord> currentBoxPosition;

        /**
         * The current slot, described by the positions of the boxes it contains. The list value is
         * empty if no slot is focused.
         */
        private final ReadOnlyListWrapper<GridCoord> currentSlotPositions;

        /**
         * The current slot as a pattern. Empty boxes are replaced by dots ('.'). An empty string if
         * {@link #currentSlotPositions} is empty.
         */
        private final ReadOnlyStringWrapper currentSlotContent;

        /** The orientation of the current slot (or the previous slot if current slot is empty). */
        private final BooleanProperty currentSlotVertical;

        /** Whether the current slot is unsolvable. */
        private final BooleanProperty currentSlotUnsolvable;

        /**
         * Constructs an instance.
         */
        WorkingArea() {
            currentBoxPosition = new SimpleObjectProperty<>(this, "currentBoxPosition");
            currentSlotPositions = new ReadOnlyListWrapper<>(this, "currentSlotPositions",
                                                             FXCollections.observableArrayList());
            currentSlotContent = new ReadOnlyStringWrapper(this, "currentSlotContent", "");
            currentSlotVertical = new SimpleBooleanProperty(this, "currentSlotVertical");
            currentSlotUnsolvable = new SimpleBooleanProperty(this, "currentSlotUnsolvable");

            currentSlotPositions.addListener(this::onCurrentSlotPositionsChange);
            currentBoxPosition.addListener(this::onCurrentBoxChange);
            currentSlotVertical.addListener(observable -> recomputeCurrentSlotPositions());
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
                final MapChangeListener.Change<? extends GridCoord, ? extends CrosswordBoxViewModel> change) {
            if (change.wasAdded()) {
                if (change.getValueRemoved() != null) {
                    throw new UnsupportedOperationException(
                            "Replacing box models is not supported");
                }
                onBoxAdded(change.getKey(), change.getValueAdded());
            } else {
                /*
                 * Nothing to do: Boxes are not reachable any more and box change listeners will be
                 * garbage-collected.
                 */
            }
        }

        /**
         * Registers box listeners.
         *
         * @param coordinate the coordinate of the added box
         * @param box        the added box
         */
        private void onBoxAdded(final GridCoord coordinate, final CrosswordBoxViewModel box) {
            box.shadedProperty().addListener(new ShadingChangeListener(coordinate));
            box.userContentProperty().addListener(new ContentChangeListener(coordinate));
        }

        /**
         * Processes a current box change.
         *
         * @param observable     the current box observable
         * @param oldBoxPosition the previous box position
         * @param newBoxPosition the current box position
         */
        private void onCurrentBoxChange(final ObservableValue<? extends GridCoord> observable,
                                        final GridCoord oldBoxPosition,
                                        final GridCoord newBoxPosition) {
            if (!currentSlotPositions.contains(newBoxPosition)) {
                recomputeCurrentSlotPositions();
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
            final GridCoord current = currentBoxPosition.get();
            if (current != null && !boxes.containsKey(current)) {
                currentBoxPosition.set(null);
            } else {
                recomputeCurrentSlotPositions();
            }
        }

        /**
         * Recomputes the {@link #currentSlotPositions}.
         */
        private void recomputeCurrentSlotPositions() {
            final GridCoord current = currentBoxPosition.get();
            if (current == null || boxes.get(current).isShaded()) {
                if (!currentSlotPositions.isEmpty()) {
                    currentSlotPositions.clear();
                }
            } else if (currentSlotVertical.get()) {
                recomputeVerticalCurrentSlotPositions();
            } else {
                recomputeCurrentHorizontalSlotPositions();
            }
        }

        /**
         * Recomputes the positions of current when it is horizontal.
         */
        private void recomputeCurrentHorizontalSlotPositions() {
            final GridCoord current = currentBoxPosition.get();
            final List<GridCoord> slotBoxes = new ArrayList<>();
            slotBoxes.add(current);
            for (int column = current.column() - 1; column >= 0; column--) {
                final GridCoord position = new GridCoord(column, current.row());
                final CrosswordBoxViewModel box = boxes.get(position);
                if (box.isShaded()) {
                    break;
                }
                slotBoxes.add(position);
            }
            for (int column = current.column() + 1; column < columnCount.get(); column++) {
                final GridCoord position = new GridCoord(column, current.row());
                final CrosswordBoxViewModel box = boxes.get(position);
                if (box.isShaded()) {
                    break;
                }
                slotBoxes.add(position);
            }
            slotBoxes.sort(COMPARING_BY_COLUMN_THEN_BY_ROW);
            currentSlotPositions.setAll(slotBoxes);
        }

        /**
         * Recomputes the positions of the current slot when it is vertical.
         */
        private void recomputeVerticalCurrentSlotPositions() {
            final GridCoord current = currentBoxPosition.get();
            final List<GridCoord> slotBoxes = new ArrayList<>();
            slotBoxes.add(current);
            for (int row = current.row() - 1; row >= 0; row--) {
                final GridCoord position = new GridCoord(current.column(), row);
                final CrosswordBoxViewModel box = boxes.get(position);
                if (box.isShaded()) {
                    break;
                }
                slotBoxes.add(position);
            }
            for (int row = current.row() + 1; row < rowCount.get(); row++) {
                final GridCoord position = new GridCoord(current.column(), row);
                final CrosswordBoxViewModel box = boxes.get(position);
                if (box.isShaded()) {
                    break;
                }
                slotBoxes.add(position);
            }
            slotBoxes.sort(COMPARING_BY_COLUMN_THEN_BY_ROW);
            currentSlotPositions.setAll(slotBoxes);
        }

        /**
         * Recomputes {@link #currentSlotContent} based on {@link #currentSlotPositions} and
         * {@link #boxes}.
         */
        private void recomputeCurrentSlotContent() {
            final StringBuilder contentBuilder = new StringBuilder(currentSlotPositions.size());
            for (final GridCoord position : currentSlotPositions) {
                final String letter = boxes.get(position).userContent();
                contentBuilder.append(letter.isEmpty() ? "." : letter);
            }
            currentSlotContent.set(contentBuilder.toString());
        }

        /**
         * Updates the box models on current slot positions change, effectively updating display.
         *
         * @param change the current slot positions change
         */
        private void onCurrentSlotPositionsChange(
                final ListChangeListener.Change<? extends GridCoord> change) {
            while (change.next()) {
                change.getRemoved().stream()
                      .map(boxes::get)
                      .filter(Objects::nonNull) // Box may have been removed from grid
                      .forEach(box -> {
                          box.deselect();
                          box.unsolvableProperty().unbind();
                          /*
                           * Clear the unsolvable status to avoid the information to become obsolete
                           * when crossing slots are modified: There is nothing to update the status
                           * of a non-selected slot.
                           */
                          box.solvable();
                      });
                change.getAddedSubList().stream()
                      .map(boxes::get)
                      .forEach(box -> {
                          box.select();
                          box.unsolvableProperty().bind(currentSlotUnsolvable);
                      });
                recomputeCurrentSlotContent();
            }
        }
    }

    /** The maximum number of rows or columns. */
    private static final int MAX_ROW_COLUMN_COUNT = 20;

    /** The number of columns of the welcome grid. */
    private static final int WELCOME_GRID_COLUMN_COUNT = 6;

    /** The number of rows of the welcome grid. */
    private static final int WELCOME_GRID_ROW_COUNT = 7;

    /** The boxes of the view. */
    private final ObservableMap<GridCoord, CrosswordBoxViewModel> boxes;

    /** An unmodifiable updating copy of the boxes, wrapped in a read-only property. */
    private final ReadOnlyMapWrapper<GridCoord, CrosswordBoxViewModel> boxesProperty;

    /** The number of columns. */
    private final ReadOnlyIntegerWrapper columnCount;

    /** The number of rows. */
    private final ReadOnlyIntegerWrapper rowCount;

    /** The slots view model, based on this grid. */
    private final SlotsViewModel slotsViewModel;

    /** The working area. */
    private final WorkingArea workingArea;

    /** A boolean to inhibit content change events when grid is being modified word by word. */
    private boolean inhibitContentChangeEvents;

    /**
     * Constructs an instance.
     *
     * @param boxesArg       the initial grid
     * @param columnCountArg grid column count
     * @param rowCountArg    the grid row count
     */
    private CrosswordGridViewModel(final Map<GridCoord, CrosswordBoxViewModel> boxesArg,
                                   final int columnCountArg, final int rowCountArg) {
        boxes = FXCollections.observableMap(boxesArg);
        boxesProperty = new ReadOnlyMapWrapper<>(this, "boxes",
                                                 FXCollections.unmodifiableObservableMap(boxes));
        columnCount = new ReadOnlyIntegerWrapper(this, "columnCount", columnCountArg);
        rowCount = new ReadOnlyIntegerWrapper(this, "rowCount", rowCountArg);
        slotsViewModel = new SlotsViewModel(boxes, columnCount, rowCount);
        workingArea = new WorkingArea();
    }

    /**
     * Creates a new grid view model without any boxes.
     *
     * @return a new grid view model without any boxes
     */
    public static CrosswordGridViewModel newGrid() {
        return new CrosswordGridViewModel(new HashMap<>(), 0, 0);
    }

    /**
     * Creates a grid view model representing a 6x7 grid.
     *
     * @return a grid view model representing a 6x7 grid
     */
    public static CrosswordGridViewModel welcomeGrid() {
        final Map<GridCoord, CrosswordBoxViewModel> welcomeBoxes = new HashMap<>();
        for (int i = 0; i < WELCOME_GRID_COLUMN_COUNT; i++) {
            for (int j = 0; j < WELCOME_GRID_ROW_COUNT; j++) {
                welcomeBoxes.put(new GridCoord(i, j), new CrosswordBoxViewModel());
            }
        }
        return new CrosswordGridViewModel(welcomeBoxes, 6, 7);
    }

    /**
     * Returns the boxes property.
     * <p>
     * Returned property is read-only and the Map value it contains is unmodifiable. Use
     * {@link #addColumn()}, {@link #addRow()}, {@link #deleteLastColumn()},
     * {@link #deleteLastRow()} or {@link #clear()} to modify the grid structure.
     *
     * @return the boxes property
     */
    public ReadOnlyMapProperty<GridCoord, CrosswordBoxViewModel> boxesProperty() {
        return boxesProperty.getReadOnlyProperty();
    }

    /**
     * Returns the value of the boxes map property at given position.
     *
     * @param position the position of the box
     * @return the box at given position
     */
    public CrosswordBoxViewModel box(final GridCoord position) {
        return boxesProperty.get(position);
    }

    /**
     * The column count property.
     * <p>
     * Transient incomplete columns are not counted, i.e. value is updated when grid is consistent.
     *
     * @return the column count property
     */
    public ReadOnlyIntegerProperty columnCountProperty() {
        return columnCount.getReadOnlyProperty();
    }

    /**
     * Returns the value of the column count property.
     *
     * @return the value of the column count property.
     */
    public int columnCount() {
        return columnCount.get();
    }

    /**
     * The row count property.
     * <p>
     * Transient incomplete rows are not counted, i.e. value is updated when grid is consistent.
     *
     * @return the row count property
     */
    public ReadOnlyIntegerProperty rowCountProperty() {
        return rowCount.getReadOnlyProperty();
    }

    /**
     * Returns the value of the column count property.
     *
     * @return the value of the row count property
     */
    public int rowCount() {
        return rowCount.get();
    }

    /**
     * The across (= horizontal) slots.
     *
     * @return the across slots property
     */
    public ReadOnlyListProperty<SlotOutline> acrossSlotsProperty() {
        return slotsViewModel.acrossSlotsProperty();
    }

    /**
     * The down (= vertical) slots.
     *
     * @return the down slots property
     */
    public ReadOnlyListProperty<SlotOutline> downSlotsProperty() {
        return slotsViewModel.downSlotsProperty();
    }

    /**
     * The current box position property. The value it contains is {@code null} if no box has been
     * focused or the last focused box has been deleted.
     *
     * @return the current box
     */
    public ObjectProperty<GridCoord> currentBoxPositionProperty() {
        return workingArea.currentBoxPosition;
    }

    /**
     * Sets the value of current box position property.
     *
     * @param position the value to set
     */
    public void currentBoxPosition(final GridCoord position) {
        workingArea.currentBoxPosition.set(position);
    }

    /**
     * Gets the value of current box position property.
     *
     * @return the value of current box position property
     */
    public GridCoord currentBoxPosition() {
        return workingArea.currentBoxPosition.get();
    }

    /**
     * The current slot positions property. The list value is empty if no slot is selected.
     *
     * @return the current slot positions
     */
    public ReadOnlyListProperty<GridCoord> currentSlotPositionsProperty() {
        return workingArea.currentSlotPositions.getReadOnlyProperty();
    }

    /**
     * The current slot content property.
     * <p>
     * The String value is empty if no slot is selected. Any non-filled box content of the current
     * slot is replaced by a dot ('.').
     *
     * @return the current slot content property
     */
    public ReadOnlyStringProperty currentSlotContentProperty() {
        return workingArea.currentSlotContent.getReadOnlyProperty();
    }

    /**
     * The value of the current slot content property.
     *
     * @return the current slot content
     */
    public String currentSlotContent() {
        return workingArea.currentSlotContent.get();
    }

    /**
     * Sets the value of the current slot content property.
     * <p>
     * Note that the current slot property is a read-only property, as it is internally computed
     * from the grid content. This method indirectly sets the current slot property by modifying the
     * grid box contents. Thus, this method will generate several change events on the grid.
     * <p>
     * This method will not generate more than one change event for the current slot property
     * though, as a special care is made to avoid that.
     *
     * @param value the new value
     * @throws NullPointerException     if given value is {@code null}
     * @throws IllegalArgumentException if given value is not of the length of the current slot
     */
    public void currentSlotContent(final String value) {
        Objects.requireNonNull(value);
        if (value.length() != workingArea.currentSlotContent.length().get()) {
            throw new IllegalArgumentException("Given value does not match current slot size");
        }
        inhibitContentChangeEvents = true;
        for (int i = 0; i < value.length(); i++) {
            boxes.get(workingArea.currentSlotPositions.get(i))
                 .userContent(value.substring(i, i + 1));
        }
        inhibitContentChangeEvents = false;
        workingArea.recomputeCurrentSlotContent();
    }

    /**
     * The orientation property of the current slot (or the previous slot if current slot is
     * empty).
     *
     * @return the orientation of the current slot
     */
    public BooleanProperty currentSlotVerticalProperty() {
        return workingArea.currentSlotVertical;
    }

    /**
     * Sets the orientation property of the current slot to vertical.
     */
    public void currentSlotVertical() {
        workingArea.currentSlotVertical.set(true);
    }

    /**
     * The unsolvable current slot property.
     *
     * @return the unsolvable current slot property.
     */
    public BooleanProperty currentSlotUnsolvableProperty() {
        return workingArea.currentSlotUnsolvable;
    }

    /**
     * Sets the value of the unsolvable current slot property to {@code true}.
     */
    public void currentSlotUnsolvable() {
        workingArea.currentSlotUnsolvable.set(true);
    }

    /**
     * Creates an empty column at the right of the grid.
     */
    public void addColumn() {
        final int newColumnIndex = columnCount.get();
        if (newColumnIndex >= MAX_ROW_COLUMN_COUNT) {
            return;
        }
        final int currentRowCount = rowCount.get();
        for (int row = 0; (row < currentRowCount) || (row == 0); row++) {
            final GridCoord coordinate = new GridCoord(newColumnIndex, row);
            boxes.put(coordinate, new CrosswordBoxViewModel());
        }
        columnCount.set(columnCount.get() + 1);
        if (currentRowCount == 0) {
            // Adding first column implicitly adds first row
            rowCount.set(1);
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
        final int currentColumnCount = columnCount.get();
        for (int column = 0; (column < currentColumnCount) || (column == 0); column++) {
            final GridCoord coordinate = new GridCoord(column, newRowIndex);
            boxes.put(coordinate, new CrosswordBoxViewModel());
        }
        rowCount.set(rowCount.get() + 1);
        if (currentColumnCount == 0) {
            // Adding first row implicitly adds first column
            columnCount.set(1);
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
        final int currentColumnCount = columnCount.get();
        for (int column = 0; column < currentColumnCount; column++) {
            final GridCoord coordinate = new GridCoord(column, deletedRowIndex);
            boxes.remove(coordinate);
        }
        if (oldRowCount == 1) {
            // Deleting very last row implicitly deletes all columns
            clear();
        } else {
            rowCount.set(oldRowCount - 1);
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
        final int currentRowCount = rowCount.get();
        for (int row = 0; row < currentRowCount; row++) {
            final GridCoord coordinate = new GridCoord(deletedColumnIndex, row);
            boxes.remove(coordinate);
        }
        if (oldColumnCount == 1) {
            // Deleting very last column implicitly deletes all rows
            clear();
        } else {
            columnCount.set(oldColumnCount - 1);
        }
    }

    /**
     * Resizes the grid model to the given dimensions.
     *
     * @param desiredColumnCount the desired number of columns
     * @param desiredRowCount    the desired number of rows
     * @throws IllegalArgumentException if any dimension is negative
     */
    public void resizeTo(final int desiredColumnCount, final int desiredRowCount) {
        if (desiredColumnCount < 0 || desiredRowCount < 0) {
            throw new IllegalArgumentException(
                    "Invalid negative resize dimension: " + desiredColumnCount + "x" +
                    desiredRowCount);
        }
        while (columnCount.get() < desiredColumnCount) {
            addColumn();
        }
        while (columnCount.get() > desiredColumnCount) {
            deleteLastColumn();
        }
        while (rowCount.get() < desiredRowCount) {
            addRow();
        }
        while (rowCount.get() > desiredRowCount) {
            deleteLastRow();
        }
    }

    /**
     * Clears the entire grid, including its structure, i.e. box nodes are removed from the grid.
     */
    public void clear() {
        boxes.clear();
        columnCount.set(0);
        rowCount.set(0);
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
     * Resets the boxes matching the given predicate.
     *
     * @param predicate filters the boxes to be reset
     */
    private void resetContent(final Predicate<CrosswordBoxViewModel> predicate) {
        boxes.values().stream()
             .filter(predicate)
             .forEach(this::resetBox);
    }

    /**
     * Resets the grid content (only content filled by solver).
     * <p>
     * This method preserves the structure of the grid, box nodes are not removed.
     */
    public void resetContentLettersFilledBySolverOnly() {
        boxes.values().forEach(box -> box.solverContent(""));
    }

    /**
     * Resets the model to defaults.
     */
    public void reset() {
        workingArea.currentBoxPosition.set(null);
        boxes.values().forEach(this::resetBox);
        resizeTo(WELCOME_GRID_COLUMN_COUNT, WELCOME_GRID_ROW_COUNT);
    }

    /**
     * Resets the given box.
     *
     * @param box the box to reset
     */
    private void resetBox(final CrosswordBoxViewModel box) {
        box.userContent("");
        box.solverContent("");
        box.lighten();
        if (!box.isSelected()) {
            box.solvable();
        } else {
            // Solvable status is bound to solvable status of selected slot.
        }
    }
}
