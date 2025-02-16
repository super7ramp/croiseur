/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle.persistence;

import java.util.ArrayList;
import java.util.List;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;

/** The 'list puzzles' usecase. */
final class ListPuzzlesUsecase {

    /** The puzzle repository. */
    private final SafePuzzleRepository repository;

    /** The puzzle presenter. */
    private final PuzzlePresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the puzzle repository
     * @param presenterArg the puzzle presenter
     */
    ListPuzzlesUsecase(final SafePuzzleRepository repositoryArg, final PuzzlePresenter presenterArg) {
        repository = repositoryArg;
        presenter = presenterArg;
    }

    /** Processes the 'list puzzle' event. */
    void process() {
        final List<SavedPuzzle> puzzles = new ArrayList<>(repository.list());
        presenter.presentAvailablePuzzles(puzzles);
    }
}
