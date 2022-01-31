package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Dictionary;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.lib.core.Slot;
import com.gitlab.super7ramp.crosswords.solver.lib.dictionary.CachedDictionaryWriter;
import com.gitlab.super7ramp.crosswords.solver.lib.elimination.EliminationSpaceWriter;
import com.gitlab.super7ramp.crosswords.solver.lib.grid.Grid;
import com.gitlab.super7ramp.crosswords.solver.lib.history.HistoryWriter;

import java.util.Collection;

/**
 * The state of the crossword puzzle, including caches used for resolution.
 */
final class Crossword {

    /** The grid (the variables). */
    private final Grid grid;

    /** The candidates (potential values). */
    private final CachedDictionaryWriter dictionary;

    /** The eliminated candidates (values marked as no-goods). */
    private final EliminationSpaceWriter eliminationSpace;

    /** The assignment history. */
    private final HistoryWriter history;

    /**
     * Constructor.
     *
     * @param aGrid              a grid
     * @param aDictionary        a dictionary
     * @param anEliminationSpace an elimination space
     * @param aHistory           an history
     */
    private Crossword(final Grid aGrid, final CachedDictionaryWriter aDictionary,
                      final EliminationSpaceWriter anEliminationSpace,
                      final HistoryWriter aHistory) {
        grid = aGrid;
        dictionary = aDictionary;
        eliminationSpace = anEliminationSpace;
        history = aHistory;
    }

    /**
     * Creates new {@link Crossword} from API.
     *
     * @param puzzleDefinition   the puzzle definition
     * @param externalDictionary the external dictionary service
     * @return a new {@link Crossword}
     */
    static Crossword create(final PuzzleDefinition puzzleDefinition,
                            final Dictionary externalDictionary) {

        final Grid grid = Grid.create(puzzleDefinition);
        final Collection<Slot> slots = grid.puzzle().slots();

        final EliminationSpaceWriter eliminationSpace = EliminationSpaceWriter.create();
        final CachedDictionaryWriter dictionary = CachedDictionaryWriter.create(externalDictionary,
                slots, eliminationSpace);

        final HistoryWriter history = HistoryWriter.create();

        return new Crossword(grid, dictionary, eliminationSpace, history);
    }

    /**
     * @return the {@link Grid}
     */
    Grid grid() {
        return grid;
    }

    /**
     * @return the {@link CachedDictionaryWriter}
     */
    CachedDictionaryWriter dictionary() {
        return dictionary;
    }

    /**
     * @return the {@link EliminationSpaceWriter}
     */
    EliminationSpaceWriter eliminationSpace() {
        return eliminationSpace;
    }

    /**
     * @return the {@link HistoryWriter}
     */
    HistoryWriter history() {
        return history;
    }

}
