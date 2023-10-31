/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.spi.presenter;

import re.belv.croiseur.common.dictionary.DictionaryProviderDetails;
import re.belv.croiseur.common.dictionary.ProvidedDictionaryDetails;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.spi.presenter.clue.ClueProviderDescription;
import re.belv.croiseur.spi.presenter.dictionary.DictionaryContent;
import re.belv.croiseur.spi.presenter.dictionary.DictionarySearchResult;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import re.belv.croiseur.spi.presenter.solver.SolverDescription;
import re.belv.croiseur.spi.presenter.solver.SolverInitialisationState;
import re.belv.croiseur.spi.presenter.solver.SolverProgress;
import re.belv.croiseur.spi.presenter.solver.SolverResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A {@link Presenter} which encapsulates other {@link Presenter}s and forwards method calls to all
 * of them.
 */
final class BroadcastingPresenter implements Presenter {

    /** The presenters. */
    private final Iterable<? extends Presenter> presenters;

    /**
     * Constructs an instance.
     *
     * @param presentersArg the presenters
     * @throws NullPointerException if given presenters iterable is {@code null}
     */
    BroadcastingPresenter(final Iterable<? extends Presenter> presentersArg) {
        presenters = Objects.requireNonNull(presentersArg,
                                            "Iterable passed to broadcasting presenter shall not be null");
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDetails> providers) {
        presenters.forEach(p -> p.presentDictionaryProviders(providers));
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDetails> dictionaries) {
        presenters.forEach(p -> p.presentDictionaries(dictionaries));
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        presenters.forEach(p -> p.presentDictionaryEntries(content));
    }

    @Override
    public void presentDictionarySearchResult(final DictionarySearchResult searchResult) {
        presenters.forEach(p -> p.presentDictionarySearchResult(searchResult));
    }

    @Override
    public void presentDefaultDictionary(final ProvidedDictionaryDetails defaultDictionary) {
        presenters.forEach(p -> p.presentDefaultDictionary(defaultDictionary));
    }

    @Override
    public void presentDictionaryError(final String error) {
        presenters.forEach(p -> p.presentDictionaryError(error));
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        presenters.forEach(p -> p.presentAvailableSolvers(solverDescriptions));
    }

    @Override
    public void presentSolverInitialisationState(
            final SolverInitialisationState solverInitialisationState) {
        presenters.forEach(p -> p.presentSolverInitialisationState(solverInitialisationState));
    }

    @Override
    public void presentSolverProgress(final SolverProgress progress) {
        presenters.forEach(p -> p.presentSolverProgress(progress));
    }

    @Override
    public void presentSolverResult(final SolverResult result) {
        presenters.forEach(p -> p.presentSolverResult(result));
    }

    @Override
    public void presentSolverError(final String error) {
        presenters.forEach(p -> p.presentSolverError(error));
    }

    @Override
    public void presentAvailablePuzzles(final List<SavedPuzzle> puzzles) {
        presenters.forEach(p -> p.presentAvailablePuzzles(puzzles));
    }

    @Override
    public void presentLoadedPuzzle(final SavedPuzzle puzzle) {
        presenters.forEach(p -> p.presentLoadedPuzzle(puzzle));
    }

    @Override
    public void presentPuzzleRepositoryError(final String error) {
        presenters.forEach(p -> p.presentPuzzleRepositoryError(error));
    }

    @Override
    public void presentSavedPuzzle(final SavedPuzzle puzzle) {
        presenters.forEach(p -> p.presentSavedPuzzle(puzzle));
    }

    @Override
    public void presentDeletedAllPuzzles() {
        presenters.forEach(PuzzlePresenter::presentDeletedAllPuzzles);
    }

    @Override
    public void presentDeletedPuzzle(final long id) {
        presenters.forEach(p -> p.presentDeletedPuzzle(id));
    }

    @Override
    public void presentPuzzleDecoders(final List<PuzzleCodecDetails> decoders) {
        presenters.forEach(p -> p.presentPuzzleDecoders(decoders));
    }

    @Override
    public void presentPuzzleImportError(final String error) {
        presenters.forEach(p -> p.presentPuzzleImportError(error));
    }

    @Override
    public void presentPuzzleEncoders(final List<PuzzleCodecDetails> encoders) {
        presenters.forEach(p -> p.presentPuzzleEncoders(encoders));
    }

    @Override
    public void presentPuzzleExportError(final String error) {
        presenters.forEach(p -> p.presentPuzzleExportError(error));
    }

    @Override
    public void presentClueError(final String error) {
        presenters.forEach(p -> p.presentClueError(error));
    }

    @Override
    public void presentClueProviders(final List<ClueProviderDescription> clueProviderDescriptions) {
        presenters.forEach(p -> p.presentClueProviders(clueProviderDescriptions));
    }

    @Override
    public void presentClues(final Map<String, String> clues) {
        presenters.forEach(p -> p.presentClues(clues));
    }
}
