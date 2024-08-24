/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.tests.puzzle.importer;

import static org.mockito.Mockito.verify;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import re.belv.croiseur.api.puzzle.importer.PuzzleImportService;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import re.belv.croiseur.tests.context.TestContext;

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
        verify(presenterMock).presentPuzzleImportError(error);
    }
}
