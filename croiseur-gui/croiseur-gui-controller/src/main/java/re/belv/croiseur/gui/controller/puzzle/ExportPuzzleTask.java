/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.puzzle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.concurrent.Task;
import re.belv.croiseur.api.puzzle.exporter.PuzzleExportService;
import re.belv.croiseur.gui.view.model.PuzzleEditionViewModel;

/** The 'export puzzle' task. */
final class ExportPuzzleTask extends Task<Void> {

    /** The puzzle id. */
    private final long puzzleId;

    /** The selected file (where will be written the export). */
    private final File selectedFile;

    /** The export format. */
    private final String selectedFormat;

    /** The export service. */
    private final PuzzleExportService puzzleExportService;

    /**
     * Constructs an instance.
     *
     * @param puzzleEditionViewModel the puzzle edition view model
     * @param selectedFileArg the selected filed (where will be written the export)
     * @param selectedFormatArg the export format
     * @param puzzleExportServiceArg the export service
     */
    ExportPuzzleTask(
            final PuzzleEditionViewModel puzzleEditionViewModel,
            final File selectedFileArg,
            final String selectedFormatArg,
            final PuzzleExportService puzzleExportServiceArg) {
        puzzleId = puzzleEditionViewModel.puzzleDetailsViewModel().id();
        selectedFile = selectedFileArg;
        selectedFormat = selectedFormatArg;
        puzzleExportService = puzzleExportServiceArg;
    }

    @Override
    protected Void call() throws IOException {
        try (final var selectedFileOutputStream = new FileOutputStream(selectedFile)) {
            puzzleExportService.exportPuzzle(puzzleId, selectedFormat, selectedFileOutputStream);
        }
        return null;
    }
}
