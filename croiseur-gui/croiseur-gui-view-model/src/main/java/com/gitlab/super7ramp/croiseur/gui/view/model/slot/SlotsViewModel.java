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
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.gitlab.super7ramp.croiseur.gui.view.model.GridCoord.at;

/**
 * The slots view model.
 */
public final class SlotsViewModel {

    /** The across slots. Filter out slots of length less than 2. */
    private final ReadOnlyListWrapper<SlotOutline> acrossSlots;

    /** The down slots. Filter out slots of length less than 2. */
    private final ReadOnlyListWrapper<SlotOutline> downSlots;

    /** Across slots updater, processing box transitions to shaded state. */
    private final AcrossSlotShadedBoxProcessor acrossSlotsShadedBoxProcessor;

    /** Down slots updater, processing box transitions to shaded state. */
    private final DownSlotShadedBoxProcessor downSlotsShadedBoxProcessor;

    /** Across slots updater, processing box transition to lightened state. */
    private final AcrossSlotsLightenedBoxProcessor acrossSlotsLightenedBoxProcessor;

    /** Down slots updater, processing box transition to lightened state. */
    private final DownSlotsLightenedBoxProcessor downSlotsLightenedBoxProcessor;

    /**
     * Constructs an instance.
     * <p>
     * All parameters are only listened; No modification will be attempted.
     *
     * @param boxes       the grid boxes
     * @param columnCount the grid column count
     * @param rowCount    the grid row count
     */
    public SlotsViewModel(final ObservableMap<GridCoord, CrosswordBoxViewModel> boxes,
                          final ObservableIntegerValue columnCount,
                          final ObservableIntegerValue rowCount) {
        acrossSlots =
                new ReadOnlyListWrapper<>(this, "acrossSlots",
                                          initialAcrossSlots(boxes, columnCount.get(),
                                                             rowCount.get()));

        downSlots = new ReadOnlyListWrapper<>(this, "downSlots",
                                              initialDownSlots(boxes, columnCount.get(),
                                                               rowCount.get()));

        acrossSlotsShadedBoxProcessor = new AcrossSlotShadedBoxProcessor(acrossSlots);
        downSlotsShadedBoxProcessor = new DownSlotShadedBoxProcessor(downSlots);
        acrossSlotsLightenedBoxProcessor = new AcrossSlotsLightenedBoxProcessor(acrossSlots);
        downSlotsLightenedBoxProcessor = new DownSlotsLightenedBoxProcessor(downSlots);

        boxes.forEach(
                (coord, box) -> box.shadedProperty()
                                   .addListener(observable -> onBoxShadingChange(box, coord)));
        boxes.addListener((MapChangeListener<GridCoord, CrosswordBoxViewModel>) change -> {
            if (change.wasAdded()) {
                final GridCoord coord = change.getKey();
                final CrosswordBoxViewModel box = change.getValueAdded();
                box.shadedProperty().addListener(observable -> onBoxShadingChange(box, coord));
            }
        });

        final var columnCountChangeProcessor =
                new ColumnCountChangeProcessor(downSlots, acrossSlots);
        columnCount.addListener(
                (observable, oldCount, newCount) -> columnCountChangeProcessor.process(
                        oldCount.intValue(), newCount.intValue(), rowCount.get()));

        final var rowCountChangeProcessor = new RowCountChangeProcessor(downSlots, acrossSlots);
        rowCount.addListener(
                (observable, oldCount, newCount) -> rowCountChangeProcessor.process(
                        oldCount.intValue(), newCount.intValue(), columnCount.get()));
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
    private static ObservableList<SlotOutline> initialAcrossSlots(
            final Map<GridCoord, CrosswordBoxViewModel> boxes, final int columnCount,
            final int rowCount) {
        final List<SlotOutline> initialAcrossSlots = new LinkedList<>();
        for (int row = 0; row < rowCount; row++) {
            int columnStart = 0;
            for (int column = columnStart; column < columnCount; column++) {
                if (boxes.get(at(column, row)).isShaded()) {
                    if (column - columnStart > 0) {
                        initialAcrossSlots.add(SlotOutline.across(columnStart, column, row));
                    }
                    columnStart = column + 1;
                }
            }
            if (columnCount - columnStart > 0) {
                initialAcrossSlots.add(SlotOutline.across(columnStart, columnCount, row));
            }
        }
        return FXCollections.observableList(initialAcrossSlots);
    }

    /**
     * Creates the initial down slot list.
     *
     * @param boxes       the initial boxes
     * @param columnCount the initial column count
     * @param rowCount    the initial row count
     * @return the initial down slot list
     */
    private static ObservableList<SlotOutline> initialDownSlots(
            final Map<GridCoord, CrosswordBoxViewModel> boxes,
            final int columnCount, final int rowCount) {
        final List<SlotOutline> initialDownSlots = new LinkedList<>();
        for (int column = 0; column < columnCount; column++) {
            int rowStart = 0;
            for (int row = rowStart; row < rowCount; row++) {
                if (boxes.get(at(column, row)).isShaded()) {
                    if (row - rowStart > 0) {
                        initialDownSlots.add(SlotOutline.down(rowStart, row, column));
                    }
                    rowStart = row + 1;
                }
            }
            if (rowCount - rowStart > 0) {
                initialDownSlots.add(SlotOutline.down(rowStart, rowCount, column));
            }
        }
        return FXCollections.observableList(initialDownSlots);
    }

    /**
     * Processes a box shading change.
     *
     * @param box   the changed box
     * @param coord the coordinate of the changed box
     */
    private void onBoxShadingChange(final CrosswordBoxViewModel box, final GridCoord coord) {
        if (box.isShaded()) {
            acrossSlotsShadedBoxProcessor.process(coord);
            downSlotsShadedBoxProcessor.process(coord);
        } else {
            acrossSlotsLightenedBoxProcessor.process(coord);
            downSlotsLightenedBoxProcessor.process(coord);
        }
    }
}
