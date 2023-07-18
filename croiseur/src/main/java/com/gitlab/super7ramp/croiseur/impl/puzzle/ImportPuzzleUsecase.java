/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle;

import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.impl.puzzle.repository.SafePuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecodingException;

import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;

/**
 * The 'import puzzle' usecase.
 */
final class ImportPuzzleUsecase {

    /** The available decoders. */
    private final Collection<PuzzleDecoder> decoders;

    /** The puzzle repository. */
    private final SafePuzzleRepository repository;

    /** The puzzle presenter. */
    private final PuzzlePresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param decodersArg   the available puzzle decoders
     * @param repositoryArg the puzzle repository
     * @param presenterArg  the puzzle presenter
     */
    ImportPuzzleUsecase(final Collection<PuzzleDecoder> decodersArg,
                        final SafePuzzleRepository repositoryArg,
                        final PuzzlePresenter presenterArg) {
        decoders = decodersArg;
        repository = repositoryArg;
        presenter = presenterArg;
    }

    /**
     * Processes the 'import puzzle' request.
     *
     * @param inputStream the input stream where to read the puzzle to import
     * @param format      the format of the puzzle
     */
    void process(final InputStream inputStream, final String format) {

        final Optional<PuzzleDecoder> selectedDecoder = selectDecoder(format);
        if (selectedDecoder.isEmpty()) {
            presenter.presentPuzzleImportError(
                    "No suitable decoder found for format '" + format + "'");
            return;
        }

        try {
            final Puzzle puzzle = selectedDecoder.get().decode(inputStream);
            repository.create(puzzle);
        } catch (final PuzzleDecodingException e) {
            presenter.presentPuzzleImportError("Error while importing puzzle: " + e.getMessage());
        }
    }

    /**
     * Selects the first decoder supporting the given format.
     *
     * @param format the puzzle format
     * @return the first decoder supporting the given format, if any; otherwise
     * {@link Optional#empty()}
     */
    private Optional<PuzzleDecoder> selectDecoder(final String format) {
        return decoders.stream()
                       .filter(decoder -> decoder.details().supportedFormats().contains(format))
                       .findFirst();
    }
}
