/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle.persistence;

import re.belv.croiseur.common.puzzle.ChangedPuzzle;
import re.belv.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;

/**
 * The 'save changed puzzle' usecase.
 *
 * <p>In practice, just boilerplate around {@link SafePuzzleRepository} which manages error handling and presentation
 * for all services relying on the puzzle repository.
 */
final class SaveChangedPuzzleUsecase {

    /** The puzzle repository. */
    private final SafePuzzleRepository repository;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the puzzle repository
     */
    SaveChangedPuzzleUsecase(final SafePuzzleRepository repositoryArg) {
        repository = repositoryArg;
    }

    /**
     * Processes the 'save changed puzzle' request.
     *
     * @param puzzle the puzzle to save
     */
    void process(final ChangedPuzzle puzzle) {
        repository.update(puzzle);
        // SafePuzzleRepository.update() handles presentation for both success and error cases
    }
}
