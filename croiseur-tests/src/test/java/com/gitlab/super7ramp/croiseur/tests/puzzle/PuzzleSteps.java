/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.tests.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Steps pertaining to the PuzzleService.
 */
public class PuzzleSteps {

    /** The puzzle service. */
    private final PuzzleService puzzleService;

    /** The presenter mock. */
    private final Presenter presenterMock;

    /**
     * Constructs an instance.
     *
     * @param testContext the test context
     */
    public PuzzleSteps(final TestContext testContext) {
        puzzleService = testContext.puzzleService();
        presenterMock = testContext.presenterMock();
    }

    @When("user requests to list the available puzzles")
    public void whenList() {
        puzzleService.list();
    }

    @Then("the application presents an empty list of puzzles")
    public void thenPresentNoPuzzle() {
        verify(presenterMock).presentAvailablePuzzles(eq(Collections.emptyList()));
    }

    @Then("the application presents the following list of puzzles:")
    public void thenPresentPuzzles(final List<Puzzle> puzzles) {
        verify(presenterMock).presentAvailablePuzzles(eq(puzzles));
    }
}
