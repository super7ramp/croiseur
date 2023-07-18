/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle.importer;

import com.gitlab.super7ramp.croiseur.api.puzzle.importer.PuzzleImportService;
import com.gitlab.super7ramp.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecoder;

import java.io.InputStream;
import java.util.Collection;

/**
 * Implementation of {@link PuzzleImportService}.
 */
public final class PuzzleImportServiceImpl implements PuzzleImportService {

    /** The 'list puzzle decoders' usecase. */
    private final ListPuzzleDecodersUsecase listPuzzleDecodersUsecase;

    /** The 'import puzzle' usecase. */
    private final ImportPuzzleUsecase importPuzzleUsecase;

    /**
     * Constructs an instance.
     *
     * @param repository the puzzle repository
     * @param decoders   the puzzle decoders
     * @param presenter  the puzzle presenter
     */
    public PuzzleImportServiceImpl(final SafePuzzleRepository repository,
                                   final Collection<PuzzleDecoder> decoders,
                                   final PuzzlePresenter presenter) {
        listPuzzleDecodersUsecase = new ListPuzzleDecodersUsecase(decoders, presenter);
        importPuzzleUsecase = new ImportPuzzleUsecase(decoders, repository, presenter);
    }

    @Override
    public void listDecoders() {
        listPuzzleDecodersUsecase.process();
    }

    @Override
    public void importPuzzle(final String format, final InputStream inputStream) {
        importPuzzleUsecase.process(format, inputStream);
    }

}
