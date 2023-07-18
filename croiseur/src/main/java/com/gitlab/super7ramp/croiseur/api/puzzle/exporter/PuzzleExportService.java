/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.puzzle.exporter;

import java.io.OutputStream;
import java.util.List;

/**
 * Services pertaining to export puzzles.
 */
public interface PuzzleExportService {

    /**
     * Lists the available puzzle encoders.
     *
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentPuzzleEncoders(List)
     * presentPuzzleEncoders(List)
     */
    void listEncoders();

    /**
     * Exports a puzzle from repository to given output stream.
     * <p>
     * The puzzle will be encoded using available {@link #listEncoders() encoders}, then saved to
     * the puzzle repository.
     * <p>
     * If no saved puzzle has the given id, an error will be presented.
     *
     * @param id           the id of the saved puzzle to export
     * @param format       the puzzle format - preferably the mimetype, if any, or the file
     *                     extension
     * @param outputStream the output stream where to encode the puzzle
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentPuzzleExportError(String)
     * PuzzlePresenter#presentPuzzleExportError
     */
    void exportPuzzle(final long id, final String format, final OutputStream outputStream);
}
