/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.puzzle;

import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.tests.context.PuzzleRepositorySpy;
import com.gitlab.super7ramp.croiseur.tests.context.TestContext;
import io.cucumber.java.Transpose;
import io.cucumber.java.en.Then;

/**
 * Steps pertaining to the puzzle repository.
 */
public final class PuzzleRepositorySteps {

    /** The puzzle repository spy. */
    private final PuzzleRepositorySpy puzzleRepositorySpy;

    /**
     * Constructs an instance.
     *
     * @param testContext the test context
     */
    public PuzzleRepositorySteps(final TestContext testContext) {
        puzzleRepositorySpy = testContext.puzzleRepositorySpy();
    }

    @Then("the application saves the following puzzle:")
    public void thenCreate(@Transpose final SavedPuzzle savedPuzzle) {
        puzzleRepositorySpy.verifyCreation(savedPuzzle);
    }

    @Then("the application updates the saved puzzle:")
    public void thenUpdate(@Transpose final SavedPuzzle savedPuzzle) {
        puzzleRepositorySpy.verifyUpdate(savedPuzzle);
    }
}
