/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle.persistence;

import re.belv.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;

/**
 * The 'delete all puzzles' usecase.
 *
 * <p>In practice, just boilerplate around {@link SafePuzzleRepository} which manages error handling and presentation
 * for all services relying on the puzzle repository.
 */
final class DeleteAllPuzzlesUsecase {

    /** The repository. */
    private final SafePuzzleRepository repository;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the puzzle repository
     */
    DeleteAllPuzzlesUsecase(final SafePuzzleRepository repositoryArg) {
        repository = repositoryArg;
    }

    /** Processes the 'delete all puzzles' event. */
    void process() {
        repository.deleteAll();
        // SafePuzzleRepository.deleteAll() handles presentation for both success and error cases
    }
}
