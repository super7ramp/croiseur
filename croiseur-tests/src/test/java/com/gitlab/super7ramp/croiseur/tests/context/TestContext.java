/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.context;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.api.clue.ClueService;
import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryService;
import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.api.solver.SolverService;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Test context.
 */
public final class TestContext {

    /** The crossword service. */
    private CrosswordService crosswordService;

    /** The mocked presenter. */
    private Presenter presenterMock;

    /** The spied puzzle repository. */
    private PuzzleRepositorySpy puzzleRepositorySpy;

    /** The export stream. */
    private OutputStream exportStream;

    /**
     * Returns the dictionary service.
     *
     * @return the dictionary service
     * @throws NullPointerException if test context is not initialised
     */
    public DictionaryService dictionaryService() {
        Objects.requireNonNull(crosswordService, "Crossword service not initialized, have you " +
                                                 "called a deployment step?");
        return crosswordService.dictionaryService();
    }

    /**
     * Returns the solver service.
     *
     * @return the solver service
     * @throws NullPointerException if test context is not initialised
     */
    public SolverService solverService() {
        Objects.requireNonNull(crosswordService, "Crossword service not initialized, have you " +
                                                 "called a deployment step?");
        return crosswordService.solverService();
    }

    /**
     * Returns the puzzle service.
     *
     * @return the puzzle service
     * @throws NullPointerException if test context is not initialised
     */
    public PuzzleService puzzleService() {
        Objects.requireNonNull(crosswordService, "Crossword service not initialized, have you " +
                                                 "called a deployment step?");
        return crosswordService.puzzleService();
    }

    /**
     * Returns the clue service.
     *
     * @return the clue service
     * @throws NullPointerException if test context is not initialised
     */
    public ClueService clueService() {
        Objects.requireNonNull(crosswordService, "Crossword service not initialized, have you " +
                "called a deployment step?");
        return crosswordService.clueService();
    }

    /**
     * Returns the presenter (a mock)
     *
     * @return the presenter
     * @throws NullPointerException if test context is not initialised
     */
    public Presenter presenterMock() {
        Objects.requireNonNull(presenterMock, "Presenter mock not initialized, have you called a " +
                                              "deployment step?");
        return presenterMock;
    }

    /**
     * Returns the puzzle repository.
     *
     * @return the puzzle repository
     * @throws NullPointerException if test context is not initialised
     */
    public PuzzleRepositorySpy puzzleRepositorySpy() {
        Objects.requireNonNull(puzzleRepositorySpy,
                               "Puzzle repository not initialized, have you called a " +
                               "deployment step?");
        return puzzleRepositorySpy;
    }


    /**
     * Returns the puzzle export stream.
     *
     * @return the puzzle export stream
     */
    public OutputStream exportStream() {
        Objects.requireNonNull(exportStream,
                               "Puzzle export stream not initialized, have you called a deployment step?");
        return exportStream;
    }

    /**
     * Initialises the test context with the given information.
     *
     * @param crosswordServiceArg    the crossword service
     * @param puzzleRepositorySpyArg the spied puzzle repository
     * @param presenterMockArg       the mocked presenter
     * @throws IllegalStateException if test context is already initialised
     */
    void deploy(final CrosswordService crosswordServiceArg,
                final PuzzleRepositorySpy puzzleRepositorySpyArg,
                final Presenter presenterMockArg) {
        if (crosswordService != null || puzzleRepositorySpy != null || presenterMock != null) {
            throw new IllegalStateException("Already deployed, did you try to instantiate " +
                                            "application twice in the same test?");
        }
        crosswordService = crosswordServiceArg;
        puzzleRepositorySpy = puzzleRepositorySpyArg;
        presenterMock = presenterMockArg;
        exportStream = new ByteArrayOutputStream();
    }

    /**
     * Checks that all interactions have been verified.
     *
     * @throws NullPointerException if context not deployed
     */
    void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(presenterMock);
        puzzleRepositorySpy.verifyNoMoreInteractions();
    }

    /**
     * Resets the test context.
     */
    void undeploy() {
        crosswordService = null;
        puzzleRepositorySpy = null;
        presenterMock = null;
        exportStream = null;
    }
}
