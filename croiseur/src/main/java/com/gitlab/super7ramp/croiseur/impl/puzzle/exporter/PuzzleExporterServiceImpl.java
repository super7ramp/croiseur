/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle.exporter;

import com.gitlab.super7ramp.croiseur.api.puzzle.exporter.PuzzleExportService;
import com.gitlab.super7ramp.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleEncoder;

import java.io.OutputStream;
import java.util.Collection;

public final class PuzzleExporterServiceImpl implements PuzzleExportService {

    /** The 'list puzzle encoders' usecase. */
    private final ListPuzzleEncodersUsecase listPuzzleEncodersUsecase;

    private final ExportPuzzleUsecase exportPuzzleUsecase;

    public PuzzleExporterServiceImpl(final SafePuzzleRepository repository,
                                     final Collection<PuzzleEncoder> encoders,
                                     final PuzzlePresenter presenter) {
        listPuzzleEncodersUsecase = new ListPuzzleEncodersUsecase(encoders, presenter);
        exportPuzzleUsecase = new ExportPuzzleUsecase(repository, encoders, presenter);
    }

    @Override
    public void listEncoders() {
        listPuzzleEncodersUsecase.process();
    }

    @Override
    public void exportPuzzle(final long id, final String format, final OutputStream outputStream) {
        exportPuzzleUsecase.process(id, format, outputStream);
    }
}
