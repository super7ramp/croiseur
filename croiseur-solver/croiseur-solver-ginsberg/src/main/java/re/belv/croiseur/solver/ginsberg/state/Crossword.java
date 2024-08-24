/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.state;

import java.util.Collection;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.solver.ginsberg.Dictionary;
import re.belv.croiseur.solver.ginsberg.core.Slot;
import re.belv.croiseur.solver.ginsberg.dictionary.CachedDictionaryWriter;
import re.belv.croiseur.solver.ginsberg.elimination.EliminationSpaceWriter;
import re.belv.croiseur.solver.ginsberg.grid.Grid;
import re.belv.croiseur.solver.ginsberg.history.HistoryWriter;
import re.belv.croiseur.solver.ginsberg.lookahead.ProbePuzzle;

/**
 * The state of the crossword puzzle, including caches used for resolution.
 */
public final class Crossword {

    /** The grid (the variables). */
    private final Grid grid;

    /** A copy of the grid that can be used for look-ahead. */
    private final ProbePuzzle probe;

    /** The candidates (potential values). */
    private final CachedDictionaryWriter dictionary;

    /** The eliminated candidates (values marked as no-goods). */
    private final EliminationSpaceWriter eliminationSpace;

    /** The assignment history. */
    private final HistoryWriter history;

    /**
     * Constructor.
     *
     * @param gridArg              a grid
     * @param dictionaryArg        a dictionary
     * @param eliminationSpaceArg an elimination space
     * @param historyArg           a history
     */
    private Crossword(
            final Grid gridArg,
            final CachedDictionaryWriter dictionaryArg,
            final EliminationSpaceWriter eliminationSpaceArg,
            final HistoryWriter historyArg) {
        grid = gridArg;
        probe = new ProbePuzzle(gridArg.puzzle(), dictionaryArg, eliminationSpaceArg);
        dictionary = dictionaryArg;
        eliminationSpace = eliminationSpaceArg;
        history = historyArg;
    }

    /**
     * Creates new {@link Crossword} from API.
     *
     * @param puzzleGrid   the puzzle definition
     * @param externalDictionary the external dictionary service
     * @return a new {@link Crossword}
     */
    public static Crossword create(final PuzzleGrid puzzleGrid, final Dictionary externalDictionary) {

        final Grid grid = Grid.create(puzzleGrid);
        final Collection<Slot> slots = grid.puzzle().slots();

        final EliminationSpaceWriter eliminationSpace = EliminationSpaceWriter.create();
        final CachedDictionaryWriter dictionary =
                CachedDictionaryWriter.create(externalDictionary, slots, eliminationSpace);

        final HistoryWriter history = HistoryWriter.create();

        return new Crossword(grid, dictionary, eliminationSpace, history);
    }

    /**
     * @return the {@link Grid}
     */
    public Grid grid() {
        return grid;
    }

    /**
     * @return the copy of the puzzle used for look-ahead
     */
    public ProbePuzzle probePuzzle() {
        return probe;
    }

    /**
     * @return the {@link CachedDictionaryWriter}
     */
    public CachedDictionaryWriter dictionary() {
        return dictionary;
    }

    /**
     * @return the {@link EliminationSpaceWriter}
     */
    public EliminationSpaceWriter eliminationSpace() {
        return eliminationSpace;
    }

    /**
     * @return the {@link HistoryWriter}
     */
    public HistoryWriter history() {
        return history;
    }
}
