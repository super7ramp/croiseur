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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.gitlab.super7ramp.croiseur.gui.view.model.GridCoord.at;

/**
 * The slots view model.
 */
public final class SlotsViewModel {

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

    /** The processor of box shading events, updating across slots. */
    private final ShadedBoxProcessor acrossSlotsShadedBoxProcessor;

    /** The processor of box shading events, updating down slots. */
    private final ShadedBoxProcessor downSlotsShadedBoxProcessor;

    /** The processor of box enlightenment events, updating across slots. */
    private final LightenedBoxProcessor acrossSlotsLightenedBoxProcessor;

    /** The processor of box enlightenment events, updating down slots. */
    private final LightenedBoxProcessor downSlotsLightenedBoxProcessor;

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
                                                FXCollections.observableList(
                                                        initialAcrossSlots(boxesArg,
                                                                           columnCountArg.get(),
                                                                           rowCountArg.get())));
        downSlots = new ReadOnlyListWrapper<>(this, "downSlots",
                                              FXCollections.observableList(
                                                      initialDownSlots(boxesArg,
                                                                       columnCountArg.get(),
                                                                       rowCountArg.get())));

        acrossSlotsShadedBoxProcessor = new AcrossSlotShadedBoxProcessor(acrossSlots);
        downSlotsShadedBoxProcessor = new DownSlotShadedBoxProcessor(downSlots);
        acrossSlotsLightenedBoxProcessor =
                new AcrossSlotsLightenedBoxProcessor(acrossSlots, boxes, columnCount::get);
        downSlotsLightenedBoxProcessor =
                new DownSlotsLightenedBoxProcessor(downSlots, boxes, rowCount::get);

        boxes.forEach((coord, box) -> box.shadedProperty()
                                         .addListener(observable -> onShadedChange(coord,
                                                                                   box.isShaded())));
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

    /**
     * Creates the initial across slot list.
     *
     * @param boxes       the initial boxes
     * @param columnCount the initial column count
     * @param rowCount    the initial row count
     * @return the initial across slot list
     */
    private static List<SlotOutline> initialAcrossSlots(
            final Map<GridCoord, CrosswordBoxViewModel> boxes, final int columnCount,
            final int rowCount) {
        final List<SlotOutline> initialAcrossSlots = new LinkedList<>();
        for (int row = 0; row < rowCount; row++) {
            int columnStart = 0;
            for (int column = columnStart; column < columnCount; column++) {
                if (boxes.get(at(column, row)).isShaded()) {
                    if (column - columnStart >= SlotConstants.MIN_SLOT_LENGTH) {
                        initialAcrossSlots.add(SlotOutline.across(columnStart, column, row));
                    }
                    columnStart = column + 1;
                }
            }
            if (columnCount - columnStart >= SlotConstants.MIN_SLOT_LENGTH) {
                initialAcrossSlots.add(SlotOutline.across(columnStart, columnCount, row));
            }
        }
        return initialAcrossSlots;
    }

    /**
     * Creates the initial down slot list.
     *
     * @param boxes       the initial boxes
     * @param columnCount the initial column count
     * @param rowCount    the initial row count
     * @return the initial down slot list
     */
    private static List<SlotOutline> initialDownSlots(
            final Map<GridCoord, CrosswordBoxViewModel> boxes,
            final int columnCount, final int rowCount) {
        final List<SlotOutline> initialDownSlots = new LinkedList<>();
        for (int column = 0; column < columnCount; column++) {
            int rowStart = 0;
            for (int row = rowStart; row < rowCount; row++) {
                if (boxes.get(at(column, row)).isShaded()) {
                    if (row - rowStart >= SlotConstants.MIN_SLOT_LENGTH) {
                        initialDownSlots.add(SlotOutline.down(rowStart, row, column));
                    }
                    rowStart = row + 1;
                }
            }
            if (rowCount - rowStart >= SlotConstants.MIN_SLOT_LENGTH) {
                initialDownSlots.add(SlotOutline.down(rowStart, rowCount, column));
            }
        }
        return initialDownSlots;
    }

    /**
     * Processes a shading change.
     *
     * @param coord               the coordinates of the box which has changed
     * @param boxAtCoordNowShaded whether the box is now shaded
     */
    private void onShadedChange(final GridCoord coord, final boolean boxAtCoordNowShaded) {
        if (boxAtCoordNowShaded) {
            acrossSlotsShadedBoxProcessor.updateSlotsAfterShadingOf(coord);
            downSlotsShadedBoxProcessor.updateSlotsAfterShadingOf(coord);
        } else {
            acrossSlotsLightenedBoxProcessor.updateSlotsAfterEnlightenmentOf(coord);
            downSlotsLightenedBoxProcessor.updateSlotsAfterEnlightenmentOf(coord);
        }
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
                    if (column - columnStart >= SlotConstants.MIN_SLOT_LENGTH) {
                        newAcrossSlots.add(SlotOutline.across(columnStart, column, row));
                    }
                    columnStart = column + 1;
                }
            }
            if (columnCount.get() - columnStart >= SlotConstants.MIN_SLOT_LENGTH) {
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
                    if (row - rowStart >= SlotConstants.MIN_SLOT_LENGTH) {
                        newDownSlots.add(SlotOutline.down(rowStart, row, column));
                    }
                    rowStart = row + 1;
                }
            }
            if (rowCount.get() - rowStart >= SlotConstants.MIN_SLOT_LENGTH) {
                newDownSlots.add(SlotOutline.down(rowStart, rowCount.get(), column));
            }
        }
        downSlots.setAll(newDownSlots);
    }

    private CrosswordBoxViewModel box(final GridCoord coord) {
        return boxes.get(coord);
    }
}
