/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import re.belv.croiseur.api.puzzle.persistence.PuzzlePatch;
import re.belv.croiseur.common.puzzle.ChangedPuzzle;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.PuzzleClues;
import re.belv.croiseur.common.puzzle.PuzzleDetails;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;

/** The 'patch and save' puzzle usecase. */
final class PatchAndSavePuzzleUsecase {

    /** The puzzle repository. */
    private final SafePuzzleRepository repository;

    /** The presenter. */
    private final PuzzlePresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the puzzle repository
     * @param presenterArg the presenter
     */
    PatchAndSavePuzzleUsecase(final SafePuzzleRepository repositoryArg, final PuzzlePresenter presenterArg) {
        presenter = presenterArg;
        repository = repositoryArg;
    }

    /**
     * Processes the given puzzle patch.
     *
     * @param id the puzzle id
     * @param patch the puzzle patch
     */
    void process(final long id, final PuzzlePatch patch) {
        final Optional<SavedPuzzle> optSavedPuzzle = repository.query(id);
        if (optSavedPuzzle.isEmpty()) {
            presenter.presentPuzzleRepositoryError("Failed to update puzzle: Cannot find saved puzzle with id " + id);
            return;
        }
        final ChangedPuzzle changedPuzzle = patch(patch, optSavedPuzzle.get());
        repository.update(changedPuzzle);
        // SafePuzzleRepository.update() handles presentation for both success and error cases
    }

    /**
     * Applies given {@link PuzzlePatch} to the given {@link SavedPuzzle}.
     *
     * @param savedPuzzle the saved puzzle to patch
     * @param modification the patch to apply
     * @return the patched puzzle as a {@link ChangedPuzzle}
     */
    private static ChangedPuzzle patch(final PuzzlePatch modification, final SavedPuzzle savedPuzzle) {
        final PuzzleDetails details = patch(savedPuzzle.details(), modification);
        final PuzzleGrid grid = modification.modifiedGrid().orElseGet(savedPuzzle::grid);
        final PuzzleClues clues = patch(savedPuzzle.clues(), modification);
        final Puzzle modifiedPuzzleData = new Puzzle(details, grid, clues);
        return savedPuzzle.modifiedWith(modifiedPuzzleData);
    }

    /**
     * Applies given {@link PuzzlePatch} to the given {@link PuzzleDetails}.
     *
     * @param original the original {@link PuzzleDetails}
     * @param patch the patch to apply
     * @return the patched {@link PuzzleDetails}
     */
    private static PuzzleDetails patch(final PuzzleDetails original, final PuzzlePatch patch) {
        final String title = patch.modifiedTitle().orElseGet(original::title);
        final String author = patch.modifiedAuthor().orElseGet(original::author);
        final String editor = patch.modifiedEditor().orElseGet(original::editor);
        final String copyright = patch.modifiedCopyright().orElseGet(original::copyright);
        final Optional<LocalDate> date = patch.modifiedDate().or(original::date);
        return new PuzzleDetails(title, author, editor, copyright, date);
    }

    /**
     * Applies given {@link PuzzlePatch} to the given {@link PuzzleClues}.
     *
     * @param original the original {@link PuzzleClues}
     * @param patch the patch to apply
     * @return the patched {@link PuzzleClues}
     */
    private static PuzzleClues patch(final PuzzleClues original, final PuzzlePatch patch) {
        final List<String> acrossClues = patch.modifiedAcrossClues().orElseGet(original::across);
        final List<String> downClues = patch.modifiedDownClues().orElseGet(original::down);
        return new PuzzleClues(acrossClues, downClues);
    }
}
