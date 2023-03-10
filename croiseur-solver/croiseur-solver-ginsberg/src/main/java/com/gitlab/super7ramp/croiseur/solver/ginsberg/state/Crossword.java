/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.state;

import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.Dictionary;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary.CachedDictionaryWriter;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.elimination.EliminationSpaceWriter;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.grid.Grid;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.history.HistoryWriter;

import java.util.Collection;

/**
 * The state of the crossword puzzle, including caches used for resolution.
 */
public final class Crossword {

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
     * @param aHistory           a history
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
    public static Crossword create(final PuzzleDefinition puzzleDefinition,
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
    public Grid grid() {
        return grid;
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
