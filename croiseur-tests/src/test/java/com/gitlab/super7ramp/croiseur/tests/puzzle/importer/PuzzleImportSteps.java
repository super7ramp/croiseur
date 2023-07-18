/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.puzzle.importer;

import com.gitlab.super7ramp.croiseur.api.puzzle.importer.PuzzleImportService;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.tests.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Steps pertaining to {@link PuzzleImportService}.
 */
public final class PuzzleImportSteps {

    /** The puzzle import service. */
    private final PuzzleImportService puzzleImportService;

    /** The presenter mock. */
    private final PuzzlePresenter presenterMock;

    /**
     * Constructs an instance.
     *
     * @param testContext the test context
     */
    public PuzzleImportSteps(final TestContext testContext) {
        puzzleImportService = testContext.puzzleService().importer();
        presenterMock = testContext.presenterMock();
    }

    @When("user requests to import the following puzzle in the {string} format:")
    public void whenImport(final String formatName, final String input) {
        final InputStream is = new ByteArrayInputStream(input.getBytes());
        puzzleImportService.importPuzzle(formatName, is);
    }

    @When("user requests to list the available puzzle decoders")
    public void whenListDecoders() {
        puzzleImportService.listDecoders();
    }

    @Then("the application presents the puzzle import error {string}")
    public void thenPresentImportError(final String error) {
        verify(presenterMock).presentPuzzleImportError(eq(error));
    }
}
