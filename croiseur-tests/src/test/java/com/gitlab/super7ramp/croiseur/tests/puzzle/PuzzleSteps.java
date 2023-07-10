/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzlePatch;
import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.common.puzzle.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import com.gitlab.super7ramp.croiseur.tests.context.TestContext;
import io.cucumber.java.Transpose;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Collections;
import java.util.List;

import static com.gitlab.super7ramp.croiseur.tests.puzzle.PuzzleMatchers.withId;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
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

    @When("user requests to delete the puzzle with id {puzzleId}")
    public void whenDelete(final long puzzleId) {
        puzzleService.delete(puzzleId);
    }

    @When("user requests to delete all puzzles")
    public void whenDeleteAll() {
        puzzleService.deleteAll();
    }

    @When("user requests to list the available puzzles")
    public void whenList() {
        puzzleService.list();
    }

    @When("user requests to load puzzle with id {long}")
    public void whenLoad(final long puzzleId) {
        puzzleService.load(puzzleId);
    }

    @When("user requests to patch and save puzzle with id {long} with:")
    public void whenPatchAndSave(final long id, @Transpose final PuzzlePatch patch) {
        puzzleService.save(id, patch);
    }

    @When("user requests to save the following puzzle:")
    public void whenSave(@Transpose final Puzzle puzzle) {
        puzzleService.save(puzzle);
    }

    @When("user requests to save the following revised puzzle:")
    public void whenSaveRevision(@Transpose final ChangedPuzzle puzzle) {
        puzzleService.save(puzzle);
    }

    @Then("the application presents the following loaded puzzle:")
    public void thenPresentLoadedPuzzle(@Transpose final SavedPuzzle puzzle) {
        verify(presenterMock).presentLoadedPuzzle(eq(puzzle));
    }

    @Then("the application presents an empty list of puzzles")
    public void thenPresentNoPuzzle() {
        verify(presenterMock).presentAvailablePuzzles(eq(Collections.emptyList()));
    }

    @Then("the application presents the following list of puzzles:")
    public void thenPresentPuzzles(final List<SavedPuzzle> puzzles) {
        verify(presenterMock).presentAvailablePuzzles(eq(puzzles));
    }

    @Then("the application presents the puzzle repository error {string}")
    public void thenPresentPuzzleRepositoryError(final String error) {
        verify(presenterMock).presentPuzzleRepositoryError(eq(error));
    }

    @Then("the application presents the confirmation the puzzle has been saved using identifier {puzzleId}")
    public void thenPresentSavedPuzzle(final long puzzleId) {
        verify(presenterMock).presentSavedPuzzle(withId(puzzleId));
    }

    @Then("the application presents the confirmation the puzzle has been saved twice using identifier {puzzleId}")
    public void thenPresentSavedPuzzleTwice(final long puzzleId) {
        verify(presenterMock, times(2)).presentSavedPuzzle(withId(puzzleId));
    }

    @Then("the application presents the confirmation the puzzle with identifier {puzzleId} has been deleted")
    public void thenPresentDeletedPuzzle(final long puzzleId) {
        verify(presenterMock).presentDeletedPuzzle(eq(puzzleId));
    }

    @Then("the application presents the confirmation that all puzzles have been deleted")
    public void thenPresentDeletedAllPuzzles() {
        verify(presenterMock).presentDeletedAllPuzzles();
    }
}
