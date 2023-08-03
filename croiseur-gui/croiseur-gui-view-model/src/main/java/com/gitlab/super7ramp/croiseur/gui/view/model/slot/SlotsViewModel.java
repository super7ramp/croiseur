/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.slot;

import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.GridCoord;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.ArrayList;
import java.util.List;

import static com.gitlab.super7ramp.croiseur.gui.view.model.GridCoord.at;

/**
 * The slots view model.
 */
public final class SlotsViewModel {

    /** The minimal slot length; Slot of a single letter will be ignored. */
    private static final int MIN_SLOT_LENGTH = 2;

    /** The grid boxes. */
    private final ObservableMap<GridCoord, CrosswordBoxViewModel> boxes;

    /** The grid column count. */
    private final ObservableIntegerValue columnCount;

    /** The grid row count. */
    private final ObservableIntegerValue rowCount;

    /** The across slots. */
    private final ReadOnlyListWrapper<SlotOutline> acrossSlots;

    /** The down slots. */
    private final ReadOnlyListWrapper<SlotOutline> downSlots;

    /**
     * Constructs an instance.
     * <p>
     * All parameters are only listened; No modification will be attempted.
     *
     * @param boxesArg       the grid boxes
     * @param columnCountArg the grid column count
     * @param rowCountArg    the grid row count
     */
    public SlotsViewModel(final ObservableMap<GridCoord, CrosswordBoxViewModel> boxesArg,
                          final ObservableIntegerValue columnCountArg,
                          final ObservableIntegerValue rowCountArg) {
        boxes = boxesArg;
        columnCount = columnCountArg;
        rowCount = rowCountArg;

        acrossSlots = new ReadOnlyListWrapper<>(this, "acrossSlots",
                                                FXCollections.observableArrayList());
        evaluateAcrossSlots();
        downSlots = new ReadOnlyListWrapper<>(this, "downSlots",
                                              FXCollections.observableArrayList());
        evaluateDownSlots();

        boxes.forEach((coord, box) -> box.shadedProperty().addListener(observable -> evaluate()));
        columnCount.addListener(observable -> evaluate());
        rowCount.addListener(observable -> evaluate());
    }

    /**
     * The across slots.
     *
     * @return the across slots
     */
    public ReadOnlyListProperty<SlotOutline> acrossSlotsProperty() {
        return acrossSlots.getReadOnlyProperty();
    }

    /**
     * The down slots.
     *
     * @return the down slots
     */
    public ReadOnlyListProperty<SlotOutline> downSlotsProperty() {
        return downSlots.getReadOnlyProperty();
    }

    private void evaluate() {
        evaluateAcrossSlots();
        evaluateDownSlots();
    }

    private void evaluateAcrossSlots() {
        final List<SlotOutline> newAcrossSlots = new ArrayList<>();
        for (int row = 0; row < rowCount.get(); row++) {
            int columnStart = 0;
            for (int column = columnStart; column < columnCount.get(); column++) {
                if (box(at(column, row)).isShaded()) {
                    if (column - columnStart >= MIN_SLOT_LENGTH) {
                        newAcrossSlots.add(SlotOutline.across(columnStart, column, row));
                    }
                    columnStart = column + 1;
                }
            }
            if (columnCount.get() - columnStart >= MIN_SLOT_LENGTH) {
                newAcrossSlots.add(SlotOutline.across(columnStart, columnCount.get(), row));
            }
        }
        acrossSlots.setAll(newAcrossSlots);
    }

    private void evaluateDownSlots() {
        final List<SlotOutline> newDownSlots = new ArrayList<>();
        for (int column = 0; column < columnCount.get(); column++) {
            int rowStart = 0;
            for (int row = rowStart; row < rowCount.get(); row++) {
                if (box(at(column, row)).isShaded()) {
                    if (row - rowStart >= MIN_SLOT_LENGTH) {
                        newDownSlots.add(SlotOutline.down(rowStart, row, column));
                    }
                    rowStart = row + 1;
                }
            }
            if (rowCount.get() - rowStart >= MIN_SLOT_LENGTH) {
                newDownSlots.add(SlotOutline.down(rowStart, rowCount.get(), column));
            }
        }
        downSlots.setAll(newDownSlots);
    }

    private CrosswordBoxViewModel box(final GridCoord coord) {
        return boxes.get(coord);
    }
}
