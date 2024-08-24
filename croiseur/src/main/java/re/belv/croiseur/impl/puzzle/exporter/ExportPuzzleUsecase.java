/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle.exporter;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Optional;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncodingException;

/**
 * The 'export puzzle' usecase.
 */
final class ExportPuzzleUsecase {

    /** The puzzle repository. */
    private final SafePuzzleRepository repository;

    /** The puzzle encoders. */
    private final Collection<PuzzleEncoder> encoders;

    /** The puzzle presenter. */
    private final PuzzlePresenter presenter;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the puzzle repository
     * @param encodersArg   the puzzle encoders
     * @param presenterArg  the puzzle presenter
     */
    ExportPuzzleUsecase(
            final SafePuzzleRepository repositoryArg,
            final Collection<PuzzleEncoder> encodersArg,
            final PuzzlePresenter presenterArg) {
        repository = repositoryArg;
        encoders = encodersArg;
        presenter = presenterArg;
    }

    /**
     * Processes the 'export puzzle' request.
     *
     * @param id           the id of the saved puzzle to export
     * @param format       the export format
     * @param outputStream where to encode the puzzle
     */
    public void process(final long id, final String format, final OutputStream outputStream) {

        final Optional<PuzzleEncoder> encoder = selectEncoder(format);
        if (encoder.isEmpty()) {
            presenter.presentPuzzleExportError("No suitable encoder found for format '" + format + "'");
            return;
        }

        final Optional<Puzzle> puzzle = repository.query(id).map(SavedPuzzle::data);
        if (puzzle.isEmpty()) {
            presenter.presentPuzzleRepositoryError("Puzzle with id " + id + " not found.");
            return;
        }

        try {
            encoder.get().encode(puzzle.get(), outputStream);
        } catch (final PuzzleEncodingException e) {
            presenter.presentPuzzleExportError("Failed to export puzzle: " + e.getMessage());
        }
    }

    /**
     * Selects the first encoder supporting the given format.
     *
     * @param format the puzzle format
     * @return the first encoder supporting the given format, if any; otherwise
     * {@link Optional#empty()}
     */
    private Optional<PuzzleEncoder> selectEncoder(final String format) {
        return encoders.stream()
                .filter(encoder -> encoder.details().supportedFormats().contains(format))
                .findFirst();
    }
}
