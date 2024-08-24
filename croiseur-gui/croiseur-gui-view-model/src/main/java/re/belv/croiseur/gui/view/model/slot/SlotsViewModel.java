/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model.slot;

import static re.belv.croiseur.gui.view.model.GridCoord.at;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Predicate;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.value.ObservableIntegerValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import re.belv.croiseur.gui.view.model.CrosswordBoxViewModel;
import re.belv.croiseur.gui.view.model.GridCoord;

/** The slots view model. */
public final class SlotsViewModel {

    /** A predicate to filter boxes; Slot of a single box will be ignored. */
    private static final Predicate<SlotOutline> AT_LEAST_TWO_BOXES = s -> s.length() >= 2;

    /** The across slots. */
    private final ReadOnlyListWrapper<SlotOutline> acrossSlots;

    /** The across slots filtered to exclude single box slots. */
    private final ObservableList<SlotOutline> longAcrossSlots;

    /** The down slots */
    private final ReadOnlyListWrapper<SlotOutline> downSlots;

    /** The across slots filtered to exclude single box slots. */
    private final ObservableList<SlotOutline> longDownSlots;

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
     *
     * <p>All parameters are only listened; No modification will be attempted.
     *
     * @param boxes the grid boxes
     * @param columnCount the grid column count
     * @param rowCount the grid row count
     */
    public SlotsViewModel(
            final ObservableMap<GridCoord, CrosswordBoxViewModel> boxes,
            final ObservableIntegerValue columnCount,
            final ObservableIntegerValue rowCount) {
        acrossSlots = new ReadOnlyListWrapper<>(
                this, "acrossSlots", initialAcrossSlots(boxes, columnCount.get(), rowCount.get()));
        longAcrossSlots = acrossSlots.getReadOnlyProperty().filtered(AT_LEAST_TWO_BOXES);

        downSlots = new ReadOnlyListWrapper<>(
                this, "downSlots", initialDownSlots(boxes, columnCount.get(), rowCount.get()));
        longDownSlots = downSlots.getReadOnlyProperty().filtered(AT_LEAST_TWO_BOXES);

        acrossSlotsShadedBoxProcessor = new AcrossSlotShadedBoxProcessor(acrossSlots);
        downSlotsShadedBoxProcessor = new DownSlotShadedBoxProcessor(downSlots);
        acrossSlotsLightenedBoxProcessor = new AcrossSlotsLightenedBoxProcessor(acrossSlots);
        downSlotsLightenedBoxProcessor = new DownSlotsLightenedBoxProcessor(downSlots);

        boxes.forEach((coord, box) -> box.shadedProperty().addListener(observable -> onBoxShadingChange(box, coord)));
        boxes.addListener((MapChangeListener<GridCoord, CrosswordBoxViewModel>) change -> {
            if (change.wasAdded()) {
                final GridCoord coord = change.getKey();
                final CrosswordBoxViewModel box = change.getValueAdded();
                box.shadedProperty().addListener(observable -> onBoxShadingChange(box, coord));
            }
        });

        final var columnCountChangeProcessor = new ColumnCountChangeProcessor(downSlots, acrossSlots);
        columnCount.addListener((observable, oldCount, newCount) ->
                columnCountChangeProcessor.process(oldCount.intValue(), newCount.intValue(), rowCount.get()));

        final var rowCountChangeProcessor = new RowCountChangeProcessor(downSlots, acrossSlots);
        rowCount.addListener((observable, oldCount, newCount) ->
                rowCountChangeProcessor.process(oldCount.intValue(), newCount.intValue(), columnCount.get()));
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
     * Returns the across slot containing the given coordinates.
     *
     * @param coord the coordinates to look for
     * @return the across slot containing these coordinates
     */
    public Optional<SlotOutline> acrossSlotContaining(final GridCoord coord) {
        return acrossSlots.stream()
                .takeWhile(s -> s.offset <= coord.row())
                .filter(s -> s.contains(coord))
                .findFirst();
    }

    /**
     * The across slots which contain at least 2 boxes.
     *
     * @return the across slots which contain at least 2 boxes.
     */
    public ObservableList<SlotOutline> longAcrossSlots() {
        return longAcrossSlots;
    }

    /**
     * Returns the index of the slot in {@link #longAcrossSlots} containing the given coordinates.
     *
     * @param coord the coordinates to look for
     * @return the index of the slot in {@link #longAcrossSlots} containing these coordinates
     */
    public OptionalInt indexOfLongAcrossSlotContaining(final GridCoord coord) {
        final var it = longAcrossSlots.listIterator();
        while (it.hasNext()) {
            final var slot = it.next();
            if (slot.contains(coord)) {
                return OptionalInt.of(it.previousIndex());
            }
            if (slot.offset > coord.row() || slot.offset == coord.row() && slot.end > coord.column()) {
                break;
            }
        }
        return OptionalInt.empty();
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
     * Returns the down slot containing the given coordinates.
     *
     * @param coord the coordinates to look for
     * @return the down slot containing these coordinates
     */
    public Optional<SlotOutline> downSlotContaining(final GridCoord coord) {
        return downSlots.stream()
                .takeWhile(s -> s.offset <= coord.column())
                .filter(s -> s.contains(coord))
                .findFirst();
    }

    /**
     * The down slots which contain at least 2 boxes.
     *
     * @return the down slots which contain at least 2 boxes.
     */
    public ObservableList<SlotOutline> longDownSlots() {
        return longDownSlots;
    }

    /**
     * Returns the index of the slot in {@link #longDownSlots} containing the given coordinates.
     *
     * @param coord the coordinates to look for
     * @return the index of the slot in {@link #longDownSlots} containing these coordinates
     */
    public OptionalInt indexOfLongDownSlotContaining(final GridCoord coord) {
        final var it = longDownSlots.listIterator();
        while (it.hasNext()) {
            final var slot = it.next();
            if (slot.contains(coord)) {
                return OptionalInt.of(it.previousIndex());
            }
            if (slot.offset > coord.column() || slot.offset == coord.column() && slot.end > coord.row()) {
                break;
            }
        }
        return OptionalInt.empty();
    }

    /**
     * Creates the initial across slot list.
     *
     * @param boxes the initial boxes
     * @param columnCount the initial column count
     * @param rowCount the initial row count
     * @return the initial across slot list
     */
    private static ObservableList<SlotOutline> initialAcrossSlots(
            final Map<GridCoord, CrosswordBoxViewModel> boxes, final int columnCount, final int rowCount) {
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
     * @param boxes the initial boxes
     * @param columnCount the initial column count
     * @param rowCount the initial row count
     * @return the initial down slot list
     */
    private static ObservableList<SlotOutline> initialDownSlots(
            final Map<GridCoord, CrosswordBoxViewModel> boxes, final int columnCount, final int rowCount) {
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
     * @param box the changed box
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
