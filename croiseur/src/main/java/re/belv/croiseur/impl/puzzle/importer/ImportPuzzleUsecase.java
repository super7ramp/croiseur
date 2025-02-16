/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle.importer;

import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecodingException;

/** The 'import puzzle' usecase. */
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
     * @param decodersArg the available puzzle decoders
     * @param repositoryArg the puzzle repository
     * @param presenterArg the puzzle presenter
     */
    ImportPuzzleUsecase(
            final Collection<PuzzleDecoder> decodersArg,
            final SafePuzzleRepository repositoryArg,
            final PuzzlePresenter presenterArg) {
        decoders = decodersArg;
        repository = repositoryArg;
        presenter = presenterArg;
    }

    /**
     * Processes the 'import puzzle' request.
     *
     * @param format the format of the puzzle
     * @param inputStream the input stream where to read the puzzle to import
     */
    void process(final String format, final InputStream inputStream) {

        final Optional<PuzzleDecoder> selectedDecoder = selectDecoder(format);
        if (selectedDecoder.isEmpty()) {
            presenter.presentPuzzleImportError("No suitable decoder found for format '" + format + "'");
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
     * @return the first decoder supporting the given format, if any; otherwise {@link Optional#empty()}
     */
    private Optional<PuzzleDecoder> selectDecoder(final String format) {
        return decoders.stream()
                .filter(decoder -> decoder.details().supportedFormats().contains(format))
                .findFirst();
    }
}
