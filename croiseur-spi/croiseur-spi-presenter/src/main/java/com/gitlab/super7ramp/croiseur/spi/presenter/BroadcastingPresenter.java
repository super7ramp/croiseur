/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.presenter;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDetails;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionarySearchResult;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverResult;

import java.util.Collection;
import java.util.List;
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
}
