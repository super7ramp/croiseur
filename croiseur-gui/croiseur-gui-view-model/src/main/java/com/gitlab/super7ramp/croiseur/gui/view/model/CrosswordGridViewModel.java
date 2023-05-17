/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableMap;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
         * The position of the box being worked on. The value it contains is {@code null} if no box
         * is focused.
         */
        private final ObjectProperty<GridPosition> currentBoxPosition;

        /**
         * The current slot, described by the positions of the boxes it contains. The list value is
         * empty if no slot is focused.
         */
        private final ReadOnlyListWrapper<GridPosition> currentSlotPositions;

        /** The orientation of the current slot (or the previous slot if current slot is empty). */
        private final BooleanProperty isCurrentSlotVertical;

        /**
         * Constructs an instance.
         */
        WorkingArea() {
            currentBoxPosition = new SimpleObjectProperty<>(this, "currentBoxPosition");
            currentSlotPositions = new ReadOnlyListWrapper<>(this, "currentSlotPositions",
                                                             FXCollections.observableArrayList());
            isCurrentSlotVertical = new SimpleBooleanProperty(this, "isCurrentSlotVertical");

            currentBoxPosition.addListener(this::onCurrentBoxChange);
            currentSlotPositions.addListener(this::onSlotChange);
            isCurrentSlotVertical.addListener(observable -> recomputeSlotPositions());
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
            if (newBoxPosition == null) {
                currentSlotPositions.clear();
            } else if (!currentSlotPositions.contains(newBoxPosition)) {
                recomputeSlotPositions();
            } else {
                /*
                 * Current box has moved to a box of the same slot, make sure the old current
                 * position is correctly repainted.
                 */
                boxes.get(oldBoxPosition).setHighlighted(true);
            }
        }

        /**
         * Recomputes the {@link #currentSlotPositions}.
         */
        private void recomputeSlotPositions() {
            if (isCurrentSlotVertical.get()) {
                recomputeVerticalSlotPositions();
            } else {
                recomputeHorizontalSlotPositions();
            }
        }

        private void recomputeHorizontalSlotPositions() {
            final GridPosition current = currentBoxPosition.get();
            final Set<GridPosition> slotBoxes = new HashSet<>();
            slotBoxes.add(current);
            for (int column = current.x() - 1; column >= 0; column--) {
                final GridPosition position = new GridPosition(column, current.y());
                if (boxes.get(position).isShaded()) {
                    break;
                }
                slotBoxes.add(position);
            }
            for (int column = current.x() + 1; column < columnCount.get(); column++) {
                final GridPosition position = new GridPosition(column, current.y());
                if (boxes.get(position).isShaded()) {
                    break;
                }
                slotBoxes.add(position);
            }
            currentSlotPositions.setAll(slotBoxes);
        }

        private void recomputeVerticalSlotPositions() {
            final GridPosition current = currentBoxPosition.get();
            final Set<GridPosition> slotBoxes = new HashSet<>();
            slotBoxes.add(current);
            for (int row = current.y() - 1; row >= 0; row--) {
                final GridPosition position = new GridPosition(current.x(), row);
                if (boxes.get(position).isShaded()) {
                    break;
                }
                slotBoxes.add(position);
            }
            for (int row = current.y() + 1; row < rowCount.get(); row++) {
                final GridPosition position = new GridPosition(current.x(), row);
                if (boxes.get(position).isShaded()) {
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
                change.getList().stream()
                      .map(boxes::get)
                      .forEach(boxModel -> boxModel.setHighlighted(true));
                change.getRemoved().stream()
                      .map(boxes::get)
                      .filter(Objects::nonNull)
                      .forEach(boxModel -> boxModel.setHighlighted(false));
            }
        }
    }

    /** The boxes of the view. */
    private final MapProperty<GridPosition, CrosswordBoxViewModel> boxes;

    /** The number of columns. */
    private final IntegerProperty columnCount;

    /** The number of rows. */
    private final IntegerProperty rowCount;

    /** The working area. */
    private final WorkingArea workingArea;

    /**
     * Constructs an instance.
     *
     * @param boxesArg the initial grid
     */
    private CrosswordGridViewModel(
            final ObservableMap<GridPosition, CrosswordBoxViewModel> boxesArg) {
        boxes = new SimpleMapProperty<>(this, "boxes", boxesArg);
        columnCount = new SimpleIntegerProperty(this, "width");
        columnCount.bind(Bindings.createIntegerBinding(
                () -> boxes.keySet().stream().mapToInt(pos -> pos.x() + 1).max().orElse(0), boxes));
        rowCount = new SimpleIntegerProperty(this, "height");
        rowCount.bind(Bindings.createIntegerBinding(
                () -> boxes.keySet().stream().mapToInt(pos -> pos.y() + 1).max().orElse(0), boxes));
        workingArea = new WorkingArea();
    }

    /**
     * Creates a grid view model representing a 6x7 grid.
     *
     * @return a grid view model representing a 6x7 grid
     */
    public static CrosswordGridViewModel welcomeGrid() {
        final ObservableMap<GridPosition, CrosswordBoxViewModel> welcomeBoxes =
                FXCollections.observableHashMap();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                welcomeBoxes.put(new GridPosition(i, j), new CrosswordBoxViewModel());
            }
        }
        return new CrosswordGridViewModel(welcomeBoxes);
    }

    /**
     * Returns the boxes.
     *
     * @return the boxes
     */
    public MapProperty<GridPosition, CrosswordBoxViewModel> boxes() {
        return boxes;
    }

    /**
     * The position of the box being worked on. The value it contains is {@code null} if no box is
     * focused.
     *
     * @return the current box
     */
    public ObjectProperty<GridPosition> currentBoxPositionProperty() {
        return workingArea.currentBoxPosition;
    }

    /**
     * The current slot, described by the positions of the boxes it contains. The list value is
     * empty if no slot is focused.
     *
     * @return the current slot
     */
    public ReadOnlyListProperty<GridPosition> currentSlotPositionsProperty() {
        return workingArea.currentSlotPositions;
    }

    /**
     * The orientation of the current slot (or the previous slot if current slot is empty).
     *
     * @return the orientation of the current slot
     */
    public BooleanProperty isCurrentSlotVerticalProperty() {
        return workingArea.isCurrentSlotVertical;
    }
}
