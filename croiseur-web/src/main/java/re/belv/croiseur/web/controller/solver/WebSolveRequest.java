/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.web.controller.solver;

import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.api.solver.SolveRequest;
import re.belv.croiseur.common.puzzle.PuzzleGrid;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

/**
 * Implementation of {@link SolveRequest}, adapting the input data model to core data model.
 */
final class WebSolveRequest implements SolveRequest {

    /** The grid to solve. */
    private final PuzzleGrid grid;

    /** The name of the solver run. */
    private final String runName;

    /**
     * Constructs an instance.
     *
     * @param gridArg    the grid to solve
     * @param runNameArg the name of the solver run
     */
    WebSolveRequest(final PuzzleGrid gridArg, final String runNameArg) {
        grid = gridArg;
        runName = runNameArg;
    }

    @Override
    public Collection<DictionaryIdentifier> dictionaries() {
        return Collections.emptyList();
    }

    @Override
    public Optional<Random> dictionariesShuffle() {
        return Optional.empty();
    }

    @Override
    public PuzzleGrid grid() {
        return grid;
    }

    @Override
    public Optional<String> solver() {
        return Optional.empty();
    }

    @Override
    public String solverRun() {
        return runName;
    }

    @Override
    public SolverProgressNotificationMethod progress() {
        return SolverProgressNotificationMethod.PERIODICAL;
    }

    @Override
    public boolean withClues() {
        return false;
    }

    @Override
    public boolean savePuzzle() {
        return false;
    }
}
