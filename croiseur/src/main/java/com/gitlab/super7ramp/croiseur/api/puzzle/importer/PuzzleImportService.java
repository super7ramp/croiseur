/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.puzzle.importer;

import java.io.InputStream;
import java.util.List;

/**
 * Services pertaining to import puzzles.
 */
public interface PuzzleImportService {

    /**
     * Lists the available puzzle decoders.
     *
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentPuzzleDecoders(List)
     * PuzzlePresenter#presentPuzzleDecoders(List)
     */
    void listDecoders();

    /**
     * Imports a puzzle from an input stream.
     * <p>
     * The given input stream will be read and decoded using available
     * {@link #listDecoders() decoders}, then saved to the puzzle repository.
     *
     * @param inputStream the input stream from which to decode the puzzle to import
     * @param format      the puzzle format - preferably the mimetype, if any, or the file
     *                    extension
     * @see com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter#presentSavedPuzzle(com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle)
     * PuzzlePresenter#presentSavedPuzzle(SavedPuzzle)
     */
    void importPuzzle(final String format, final InputStream inputStream);
}
