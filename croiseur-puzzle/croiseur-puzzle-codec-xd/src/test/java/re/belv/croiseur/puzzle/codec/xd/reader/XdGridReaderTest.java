/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.reader;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static re.belv.croiseur.puzzle.codec.xd.model.XdGrid.Index.at;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import re.belv.croiseur.puzzle.codec.xd.model.XdGrid;

/**
 * Tests for {@link XdGridReader}.
 */
final class XdGridReaderTest {

    /** The reader under tests. */
    private final XdGridReader reader = new XdGridReader();

    @Test
    void readFilled() {
        final var rawGrid = """
                ABC
                DEF
                GHI
                """;

        final XdGrid xdGrid = reader.read(rawGrid);

        assertEquals(
                Map.of(
                        at(0, 0), "A", at(1, 0), "B", at(2, 0), "C", at(0, 1), "D", at(1, 1), "E", at(2, 1), "F",
                        at(0, 2), "G", at(1, 2), "H", at(2, 2), "I"),
                xdGrid.filled());
        assertEquals(emptySet(), xdGrid.blocks());
        assertEquals(emptySet(), xdGrid.nonFilled());
        assertEquals(emptySet(), xdGrid.spaces());
    }

    @Test
    void readBlocks() {
        final var rawGrid = """
                ###
                ###
                ###
                """;

        final XdGrid xdGrid = reader.read(rawGrid);

        assertEquals(emptyMap(), xdGrid.filled());
        assertEquals(
                Set.of(at(0, 0), at(1, 0), at(2, 0), at(0, 1), at(1, 1), at(2, 1), at(0, 2), at(1, 2), at(2, 2)),
                xdGrid.blocks());
        assertEquals(emptySet(), xdGrid.nonFilled());
        assertEquals(emptySet(), xdGrid.spaces());
    }

    @Test
    void readNonFilled() {
        final var rawGrid = """
                ...
                ...
                ...
                """;

        final XdGrid xdGrid = reader.read(rawGrid);

        assertEquals(emptyMap(), xdGrid.filled());
        assertEquals(emptySet(), xdGrid.blocks());
        assertEquals(
                Set.of(at(0, 0), at(1, 0), at(2, 0), at(0, 1), at(1, 1), at(2, 1), at(0, 2), at(1, 2), at(2, 2)),
                xdGrid.nonFilled());
        assertEquals(emptySet(), xdGrid.spaces());
    }

    @Test
    void readSpaces() {
        final var rawGrid = """
                ___
                ___
                ___
                """;

        final XdGrid xdGrid = reader.read(rawGrid);

        assertEquals(emptyMap(), xdGrid.filled());
        assertEquals(emptySet(), xdGrid.blocks());
        assertEquals(emptySet(), xdGrid.nonFilled());
        assertEquals(
                Set.of(at(0, 0), at(1, 0), at(2, 0), at(0, 1), at(1, 1), at(2, 1), at(0, 2), at(1, 2), at(2, 2)),
                xdGrid.spaces());
    }

    @Test
    void readMixed() {
        final var rawGrid =
                """
                A.#CDE___
                _F.#HIJ__
                __K.#M.._
                ___..##..
                """;

        final XdGrid xdGrid = reader.read(rawGrid);

        assertEquals(
                Map.of(
                        at(0, 0), "A", at(3, 0), "C", at(4, 0), "D", at(5, 0), "E", at(1, 1), "F", at(4, 1), "H",
                        at(5, 1), "I", at(6, 1), "J", at(2, 2), "K", at(5, 2), "M"),
                xdGrid.filled());
        assertEquals(Set.of(at(2, 0), at(3, 1), at(4, 2), at(5, 3), at(6, 3)), xdGrid.blocks());
        assertEquals(
                Set.of(at(1, 0), at(2, 1), at(3, 2), at(6, 2), at(7, 2), at(3, 3), at(4, 3), at(7, 3), at(8, 3)),
                xdGrid.nonFilled());
        assertEquals(
                Stream.of(
                                at(6, 0), at(7, 0), at(8, 0), at(0, 1), at(7, 1), at(8, 1), at(0, 2), at(1, 2),
                                at(8, 2), at(0, 3), at(1, 3), at(2, 3))
                        .collect(toSet()),
                xdGrid.spaces());
    }
}
