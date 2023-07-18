/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.puzzle.persistence;

import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePatch;
import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import com.gitlab.super7ramp.croiseur.common.puzzle.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleCodecDetails;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.WriteException;
import com.gitlab.super7ramp.croiseur.tests.context.PuzzleRepositorySpy;
import com.gitlab.super7ramp.croiseur.tests.context.TestContext;
import io.cucumber.java.Transpose;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Collections;
import java.util.List;

import static com.gitlab.super7ramp.croiseur.tests.puzzle.persistence.PuzzleMatchers.withId;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Steps pertaining to the {@link PuzzlePersistenceService}.
 */
public final class PuzzlePersistenceSteps {

    /** The puzzle service. */
    private final PuzzlePersistenceService puzzlePersistenceService;

    /** The presenter mock. */
    private final PuzzlePresenter presenterMock;

    /** The puzzle repository spy. */
    private final PuzzleRepositorySpy puzzleRepositorySpy;

    /**
     * Constructs an instance.
     *
     * @param testContext the test context
     */
    public PuzzlePersistenceSteps(final TestContext testContext) {
        puzzlePersistenceService = testContext.puzzleService().persistence();
        puzzleRepositorySpy = testContext.puzzleRepositorySpy();
        presenterMock = testContext.presenterMock();
    }

    @Given("the puzzle repository contains:")
    public void givenContains(final List<SavedPuzzle> expectedSavedPuzzles) throws WriteException {
        for (final SavedPuzzle expectedSavedPuzzle : expectedSavedPuzzles) {
            puzzleRepositorySpy.create(expectedSavedPuzzle.data());
            puzzleRepositorySpy.verifyCreation(expectedSavedPuzzle);
        }
    }

    @When("user requests to delete the puzzle with id {puzzleId}")
    public void whenDelete(final long puzzleId) {
        puzzlePersistenceService.delete(puzzleId);
    }

    @When("user requests to delete all puzzles")
    public void whenDeleteAll() {
        puzzlePersistenceService.deleteAll();
    }

    @When("user requests to list the available puzzles")
    public void whenList() {
        puzzlePersistenceService.list();
    }

    @When("user requests to load puzzle with id {long}")
    public void whenLoad(final long puzzleId) {
        puzzlePersistenceService.load(puzzleId);
    }

    @When("user requests to patch and save puzzle with id {long} with:")
    public void whenPatchAndSave(final long id, @Transpose final PuzzlePatch patch) {
        puzzlePersistenceService.save(id, patch);
    }

    @When("user requests to save the following puzzle:")
    public void whenSave(@Transpose final Puzzle puzzle) {
        puzzlePersistenceService.save(puzzle);
    }

    @When("user requests to save the following revised puzzle:")
    public void whenSaveRevision(@Transpose final ChangedPuzzle puzzle) {
        puzzlePersistenceService.save(puzzle);
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

    @Then("the application presents the following puzzle decoders:")
    public void thenPresentAvailablePuzzleDecoders(final List<PuzzleCodecDetails> codecs) {
        verify(presenterMock).presentPuzzleDecoders(eq(codecs));
    }

    @Then("the application saves the following puzzle:")
    public void thenCreate(@Transpose final SavedPuzzle savedPuzzle) {
        puzzleRepositorySpy.verifyCreation(savedPuzzle);
    }

    @Then("the application updates the saved puzzle:")
    public void thenUpdate(@Transpose final SavedPuzzle savedPuzzle) {
        puzzleRepositorySpy.verifyUpdate(savedPuzzle);
    }

    @Then("the application deletes the puzzle with id {puzzleId} from repository")
    public void thenDelete(final long puzzleId) {
        puzzleRepositorySpy.verifyDeletion(puzzleId);
    }

}
