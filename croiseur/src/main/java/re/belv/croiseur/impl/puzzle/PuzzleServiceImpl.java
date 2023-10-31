/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle;

import re.belv.croiseur.api.puzzle.PuzzleService;
import re.belv.croiseur.api.puzzle.exporter.PuzzleExportService;
import re.belv.croiseur.api.puzzle.importer.PuzzleImportService;
import re.belv.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import re.belv.croiseur.impl.puzzle.exporter.PuzzleExportServiceImpl;
import re.belv.croiseur.impl.puzzle.importer.PuzzleImportServiceImpl;
import re.belv.croiseur.impl.puzzle.persistence.PuzzlePersistenceServiceImpl;
import re.belv.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import re.belv.croiseur.spi.puzzle.codec.PuzzleDecoder;
import re.belv.croiseur.spi.puzzle.codec.PuzzleEncoder;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;

import java.util.Collection;

/**
 * Implementation of {@link PuzzleService}.
 */
public final class PuzzleServiceImpl implements PuzzleService {

    /** The persistence service. */
    private final PuzzlePersistenceService persistence;

    /** The import service. */
    private final PuzzleImportService importer;

    /** The export service. */
    private final PuzzleExportService exporter;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the puzzle repository
     * @param decoders      the puzzle decoders
     * @param encoders      the puzzle encoders
     * @param presenter     the puzzle presenter
     */
    public PuzzleServiceImpl(final PuzzleRepository repositoryArg,
                             final Collection<PuzzleDecoder> decoders,
                             final Collection<PuzzleEncoder> encoders,
                             final PuzzlePresenter presenter) {
        final var repository = new SafePuzzleRepository(repositoryArg, presenter);
        persistence = new PuzzlePersistenceServiceImpl(repository, presenter);
        importer = new PuzzleImportServiceImpl(repository, decoders, presenter);
        exporter = new PuzzleExportServiceImpl(repository, encoders, presenter);
    }

    @Override
    public PuzzlePersistenceService persistence() {
        return persistence;
    }

    @Override
    public PuzzleImportService importer() {
        return importer;
    }

    @Override
    public PuzzleExportService exporter() {
        return exporter;
    }
}
