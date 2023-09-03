/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.presenter;

import com.gitlab.super7ramp.croiseur.common.dictionary.DictionaryProviderDetails;
import com.gitlab.super7ramp.croiseur.common.dictionary.ProvidedDictionaryDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleCodecDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.clue.ClueProviderDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionaryContent;
import com.gitlab.super7ramp.croiseur.spi.presenter.dictionary.DictionarySearchResult;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverResult;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link Presenter} for Croiseur Web.
 * <p>
 * This class is instantiated by {@link java.util.ServiceLoader ServiceLoader} by core service so
 * dependencies cannot be injected directly: It uses the Spring {@link ApplicationContext}, which is
 * expected to be statically set before instantiation, to retrieve the Spring beans.
 */
public final class WebPresenter implements Presenter {

    /** The spring application context. */
    private static ApplicationContext applicationContext;

    /** The solver presenter. */
    private final SolverPresenter solverPresenter;

    /** The puzzle presenter. */
    private final PuzzlePresenter puzzlePresenter;

    /**
     * Constructs an instance.
     */
    public WebPresenter() {
        Objects.requireNonNull(applicationContext,
                               "Spring application context has not been set. It must be statically set before WebPresenter instantiation.");
        solverPresenter = applicationContext.getBean(SolverPresenter.class);
        puzzlePresenter = applicationContext.getBean(PuzzlePresenter.class);
    }

    /**
     * Injects the Spring application context.
     *
     * @param applicationContextArg the Spring application context
     */
    public static void inject(final ApplicationContext applicationContextArg) {
        applicationContext = Objects.requireNonNull(applicationContextArg);
    }

    @Override
    public void presentClueError(final String error) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentClueProviders(final List<ClueProviderDescription> clueProviderDescriptions) {
        throw new UnsupportedOperationException("Not implemented yet");

    }

    @Override
    public void presentClues(final Map<String, String> clues) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaryProviders(final Collection<DictionaryProviderDetails> providers) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaries(final List<ProvidedDictionaryDetails> dictionaries) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaryEntries(final DictionaryContent content) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionarySearchResult(final DictionarySearchResult searchResult) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDefaultDictionary(final ProvidedDictionaryDetails defaultDictionary) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentDictionaryError(final String error) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void presentAvailablePuzzles(final List<SavedPuzzle> puzzles) {
        puzzlePresenter.presentAvailablePuzzles(puzzles);
    }

    @Override
    public void presentLoadedPuzzle(final SavedPuzzle puzzle) {
        puzzlePresenter.presentLoadedPuzzle(puzzle);
    }

    @Override
    public void presentPuzzleRepositoryError(final String error) {
        puzzlePresenter.presentPuzzleRepositoryError(error);
    }

    @Override
    public void presentSavedPuzzle(final SavedPuzzle puzzle) {
        puzzlePresenter.presentSavedPuzzle(puzzle);
    }

    @Override
    public void presentDeletedAllPuzzles() {
        puzzlePresenter.presentDeletedAllPuzzles();
    }

    @Override
    public void presentDeletedPuzzle(final long id) {
        puzzlePresenter.presentDeletedPuzzle(id);
    }

    @Override
    public void presentPuzzleDecoders(final List<PuzzleCodecDetails> decoders) {
        puzzlePresenter.presentPuzzleDecoders(decoders);
    }

    @Override
    public void presentPuzzleImportError(final String error) {
        puzzlePresenter.presentPuzzleImportError(error);
    }

    @Override
    public void presentPuzzleEncoders(final List<PuzzleCodecDetails> encoders) {
        puzzlePresenter.presentPuzzleEncoders(encoders);
    }

    @Override
    public void presentPuzzleExportError(final String error) {
        puzzlePresenter.presentPuzzleExportError(error);
    }

    @Override
    public void presentAvailableSolvers(final List<SolverDescription> solverDescriptions) {
        solverPresenter.presentAvailableSolvers(solverDescriptions);
    }

    @Override
    public void presentSolverInitialisationState(final String solverRun,
            final SolverInitialisationState solverInitialisationState) {
        solverPresenter.presentSolverInitialisationState(solverRun, solverInitialisationState);
    }

    @Override
    public void presentSolverProgress(final String solverRun, final SolverProgress progress) {
        solverPresenter.presentSolverProgress(solverRun, progress);
    }

    @Override
    public void presentSolverResult(final String solverRun, final SolverResult result) {
        solverPresenter.presentSolverResult(solverRun, result);
    }

    @Override
    public void presentSolverError(final String solverRun, final String error) {
        solverPresenter.presentSolverError(solverRun, error);
    }

    @Override
    public void presentSolverError(final String error) {
        solverPresenter.presentSolverError(error);
    }

}
