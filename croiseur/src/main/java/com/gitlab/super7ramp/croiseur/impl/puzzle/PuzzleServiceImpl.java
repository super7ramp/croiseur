/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.impl.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzlePatch;
import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.common.puzzle.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.impl.puzzle.repository.SafePuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleDecoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.codec.PuzzleEncoder;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;

import java.io.InputStream;
import java.util.Collection;

/**
 * Implementation of {@link PuzzleService}.
 * <p>
 * Mostly boilerplate between {@link PuzzleRepository} and {@link PuzzlePresenter}.
 */
public final class PuzzleServiceImpl implements PuzzleService {

    /** The 'list puzzles' usecase. */
    private final ListPuzzlesUsecase listPuzzlesUsecase;

    /** The 'delete puzzle' usecase. */
    private final DeletePuzzleUsecase deletePuzzleUsecase;

    /** The 'delete all puzzles' usecase. */
    private final DeleteAllPuzzlesUsecase deleteAllPuzzlesUsecase;

    /** The 'load puzzle' usecase. */
    private final LoadPuzzleUsecase loadPuzzleUsecase;

    /** The 'save new' puzzle usecase. */
    private final SaveNewPuzzleUsecase saveNewPuzzleUsecase;

    /** The 'save changed' puzzle usecase. */
    private final SaveChangedPuzzleUsecase saveChangedPuzzleUsecase;

    /** The 'patch and save puzzle' usecase. */
    private final PatchAndSavePuzzleUsecase patchAndSavePuzzleUsecase;

    /** The 'list puzzle decoders' usecase. */
    private final ListPuzzleDecodersUsecase listPuzzleDecodersUsecase;

    /** The 'import puzzle' usecase. */
    private final ImportPuzzleUsecase importPuzzleUsecase;

    /** The 'list puzzle encoders' usecase. */
    private final ListPuzzleEncodersUsecase listPuzzleEncodersUsecase;

    /**
     * Constructs an instance.
     *
     * @param repositoryArg the puzzle repository
     * @param decoders      the puzzle decoders
     * @param encoders      the puzzle encoders
     * @param presenterArg  the puzzle presenter
     */
    public PuzzleServiceImpl(final PuzzleRepository repositoryArg,
                             final Collection<PuzzleDecoder> decoders,
                             final Collection<PuzzleEncoder> encoders,
                             final PuzzlePresenter presenterArg) {
        final var repository = new SafePuzzleRepository(repositoryArg, presenterArg);
        listPuzzlesUsecase = new ListPuzzlesUsecase(repository, presenterArg);
        deletePuzzleUsecase = new DeletePuzzleUsecase(repository);
        deleteAllPuzzlesUsecase = new DeleteAllPuzzlesUsecase(repository);
        loadPuzzleUsecase = new LoadPuzzleUsecase(repository, presenterArg);
        saveNewPuzzleUsecase = new SaveNewPuzzleUsecase(repository);
        saveChangedPuzzleUsecase = new SaveChangedPuzzleUsecase(repository);
        patchAndSavePuzzleUsecase = new PatchAndSavePuzzleUsecase(repository, presenterArg);
        listPuzzleDecodersUsecase = new ListPuzzleDecodersUsecase(decoders, presenterArg);
        importPuzzleUsecase = new ImportPuzzleUsecase(decoders, repository, presenterArg);
        listPuzzleEncodersUsecase = new ListPuzzleEncodersUsecase(encoders, presenterArg);
    }

    @Override
    public void list() {
        listPuzzlesUsecase.process();
    }

    @Override
    public void delete(final long puzzleId) {
        deletePuzzleUsecase.process(puzzleId);
    }

    @Override
    public void deleteAll() {
        deleteAllPuzzlesUsecase.process();
    }

    @Override
    public void load(final long puzzleId) {
        loadPuzzleUsecase.process(puzzleId);
    }

    @Override
    public void save(final Puzzle puzzle) {
        saveNewPuzzleUsecase.process(puzzle);
    }

    @Override
    public void save(final ChangedPuzzle puzzle) {
        saveChangedPuzzleUsecase.process(puzzle);
    }

    @Override
    public void save(final long id, final PuzzlePatch patch) {
        patchAndSavePuzzleUsecase.process(id, patch);
    }

    @Override
    public void listDecoders() {
        listPuzzleDecodersUsecase.process();
    }

    @Override
    public void importPuzzle(final InputStream inputStream, final String format) {
        importPuzzleUsecase.process(inputStream, format);
    }

    @Override
    public void listEncoders() {
        listPuzzleEncodersUsecase.process();
    }

}
