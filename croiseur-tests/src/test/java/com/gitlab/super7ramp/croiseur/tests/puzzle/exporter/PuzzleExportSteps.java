/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.puzzle.exporter;

import com.gitlab.super7ramp.croiseur.api.puzzle.exporter.PuzzleExportService;
import com.gitlab.super7ramp.croiseur.common.puzzle.PuzzleCodecDetails;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.tests.context.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.OutputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Steps pertaining to the {@link PuzzleExportService}.
 */
public final class PuzzleExportSteps {

    /** The puzzle export service. */
    private final PuzzleExportService puzzleExportService;

    /** The presenter mock. */
    private final PuzzlePresenter presenterMock;

    /** The puzzle export output stream. */
    private final OutputStream exportStream;

    /**
     * Constructs an instance.
     *
     * @param testContext the test context
     */
    public PuzzleExportSteps(final TestContext testContext) {
        puzzleExportService = testContext.puzzleService().exporter();
        exportStream = testContext.exportStream();
        presenterMock = testContext.presenterMock();
    }

    @When("user requests to list the available puzzle encoders")
    public void whenListEncoders() {
        puzzleExportService.listEncoders();
    }

    @When("user requests to export the puzzle with id {puzzleId} to format {string}")
    public void whenExport(final long puzzleId, final String format) {
        puzzleExportService.exportPuzzle(puzzleId, format, exportStream);
    }

    @Then("the application writes the following export data:")
    public void thenExport(final String exportedData) {
        assertEquals(exportedData, exportStream.toString());
    }

    @Then("the application presents the following puzzle encoders:")
    public void thenPresentAvailablePuzzleEncoders(final List<PuzzleCodecDetails> codecs) {
        verify(presenterMock).presentPuzzleEncoders(codecs);
    }

    @Then("the application presents the puzzle export error {string}")
    public void thenPresentExportError(final String error) {
        verify(presenterMock).presentPuzzleExportError(error);
    }

}
