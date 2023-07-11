/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.impl.puzzle.repository.SafePuzzleRepository;

/**
 * The 'save new' puzzle usecase.
 * <p>
 * In practice, just boilerplate around {@link SafePuzzleRepository} which manages error handling
 * and presentation for all services relying on the puzzle repository.
 */
final class SaveNewPuzzleUsecase {

    /** The puzzle repository. */
    private final SafePuzzleRepository repository;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the repository
     */
    SaveNewPuzzleUsecase(final SafePuzzleRepository repositoryArg) {
        repository = repositoryArg;
    }

    /**
     * Processes the 'save new' puzzle request.
     *
     * @param puzzle the puzzle to save
     */
    void process(final Puzzle puzzle) {
        repository.create(puzzle);
        // SafePuzzleRepository.create() handles presentation for both success and error cases
    }
}
