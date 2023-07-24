/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.api.puzzle.exporter.PuzzleExportService;
import com.gitlab.super7ramp.croiseur.api.puzzle.importer.PuzzleImportService;
import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import com.gitlab.super7ramp.croiseur.impl.puzzle.exporter.PuzzleExportServiceImpl;
import com.gitlab.super7ramp.croiseur.impl.puzzle.importer.PuzzleImportServiceImpl;
import com.gitlab.super7ramp.croiseur.impl.puzzle.persistence.PuzzlePersistenceServiceImpl;
import com.gitlab.super7ramp.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleEncoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;

import java.util.Collection;

/**
 * Implementation of {@link com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService}.
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
