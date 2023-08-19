/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.repository.filesystem.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleClues;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleGrid;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdClue;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdClues;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdCrossword;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdGrid;
import com.gitlab.super7ramp.croiseur.puzzle.codec.xd.model.XdMetadata;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link PuzzleConverter}.
 */
final class PuzzleConverterTest {

    @Test
    void toDomain() throws PuzzleConversionException {
        final XdMetadata xdMetadata =
                new XdMetadata.Builder().title("Example Grid")
                                        .author("Toto")
                                        .editor("Croiseur")
                                        .date(LocalDate.of(2023, 6, 20))
                                        .otherProperty("x-croiseur-revision", "3")
                                        .build();
        final XdGrid xdGrid =
                new XdGrid.Builder().filled(XdGrid.Index.at(0, 0), 'A')
                                    .filled(XdGrid.Index.at(1, 0), 'B')
                                    .filled(XdGrid.Index.at(2, 0), 'C')
                                    .block(XdGrid.Index.at(0, 1))
                                    .nonFilled(XdGrid.Index.at(1, 1))
                                    .nonFilled(XdGrid.Index.at(2, 1))
                                    .build();
        final XdClues xdClues = new XdClues.Builder()
                .across(1, "A clue.", "ABC")
                .down(1, "", "A..")
                .build();
        final XdCrossword persistenceCrosswordModel = new XdCrossword(xdMetadata, xdGrid, xdClues);

        final SavedPuzzle domainCrosswordModel =
                PuzzleConverter.toDomain(42L, persistenceCrosswordModel);

        assertEquals(42L, domainCrosswordModel.id());
        final PuzzleDetails details = domainCrosswordModel.details();
        assertEquals("Example Grid", details.title());
        assertEquals("Toto", details.author());
        assertEquals("Croiseur", details.editor());
        assertEquals(Optional.of(LocalDate.of(2023, 6, 20)), details.date());

        final PuzzleGrid grid = domainCrosswordModel.grid();
        assertEquals(3, grid.width());
        assertEquals(2, grid.height());
        assertEquals(Set.of(GridPosition.at(0, 1)), grid.shaded());
        assertEquals(Map.of(GridPosition.at(0, 0), 'A', GridPosition.at(1, 0), 'B',
                            GridPosition.at(2, 0), 'C'), grid.filled());

        final PuzzleClues clues = domainCrosswordModel.clues();
        assertEquals(List.of("A clue."), clues.across());
        assertEquals(List.of(""), clues.down());
    }

    @Test
    void toPersistence() {
        final PuzzleDetails details = new PuzzleDetails("Example Grid", "Toto", "Croiseur", "",
                                                        Optional.of(LocalDate.of(2023, 6, 20)));
        final PuzzleGrid grid =
                new PuzzleGrid.Builder().width(3)
                                        .height(2)
                                        .fill(GridPosition.at(0, 0), 'A')
                                        .fill(GridPosition.at(1, 0), 'B')
                                        .fill(GridPosition.at(2, 0), 'C')
                                        .shade(GridPosition.at(0, 1)).build();
        final PuzzleClues clues = new PuzzleClues(List.of("A clue."), Collections.emptyList());
        final Puzzle puzzle = new Puzzle(details, grid, clues);
        final SavedPuzzle domainCrosswordModel = new SavedPuzzle(1L, puzzle, 3);

        final XdCrossword persistenceCrosswordModel =
                PuzzleConverter.toPersistence(domainCrosswordModel);

        final XdMetadata metadata = persistenceCrosswordModel.metadata();
        assertEquals("Example Grid", metadata.title().orElse(""));
        assertEquals("Toto", metadata.author().orElse(""));
        assertEquals("Croiseur", metadata.editor().orElse(""));
        assertEquals(Optional.empty(), metadata.copyright());
        assertEquals(Optional.of(LocalDate.of(2023, 6, 20)), metadata.date());

        final XdClues xdClues = persistenceCrosswordModel.clues();
        assertEquals(List.of(new XdClue(1, "A clue.", "ABC")), xdClues.acrossClues());
        assertEquals(Collections.emptyList(), xdClues.downClues());
    }
}
