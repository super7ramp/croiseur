/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.writer;

import java.util.Comparator;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import re.belv.croiseur.puzzle.codec.xd.model.XdGrid;

/**
 * Encodes {@link XdGrid} to its textual representation.
 */
final class XdGridWriter {

    /** Compares grid indexes by row then by column. */
    private static final Comparator<XdGrid.Index> SORTED_BY_ROW_THEN_BY_COLUMN =
            Comparator.comparingInt(XdGrid.Index::row).thenComparing(XdGrid.Index::column);

    /**
     * Constructs an instance.
     */
    XdGridWriter() {
        // Nothing to do.
    }

    /**
     * Writes the given grid to a string.
     *
     * @param grid the grid
     * @return the written string
     */
    String write(final XdGrid grid) {
        Objects.requireNonNull(grid);

        final SortedMap<XdGrid.Index, String> boxes = new TreeMap<>(SORTED_BY_ROW_THEN_BY_COLUMN);
        boxes.putAll(grid.filled());
        grid.nonFilled().forEach(index -> boxes.put(index, "."));
        grid.blocks().forEach(index -> boxes.put(index, "#"));
        grid.spaces().forEach(index -> boxes.put(index, "_"));
        final int lastColumn = boxes.lastKey().column();

        final StringBuilder sb = new StringBuilder();
        boxes.forEach((index, content) -> {
            sb.append(content);
            if (index.column() == lastColumn) {
                sb.append('\n');
            }
        });
        return sb.toString();
    }
}
