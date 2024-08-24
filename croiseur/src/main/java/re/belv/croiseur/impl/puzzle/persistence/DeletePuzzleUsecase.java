/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle.persistence;

import re.belv.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;

/**
 * The 'delete puzzle' usecase.
 *
 * <p>In practice, just boilerplate around {@link SafePuzzleRepository} which manages error handling and presentation
 * for all services relying on the puzzle repository.
 */
final class DeletePuzzleUsecase {

    /** The puzzle repository. */
    private final SafePuzzleRepository repository;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the repository
     */
    DeletePuzzleUsecase(final SafePuzzleRepository repositoryArg) {
        repository = repositoryArg;
    }

    /**
     * Processes the deletion request for given puzzle id.
     *
     * @param puzzleId the puzzle id
     */
    void process(final long puzzleId) {
        // The repository does it all
        repository.delete(puzzleId);
    }
}
