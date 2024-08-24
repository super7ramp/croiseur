/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.puzzle.persistence;

import re.belv.croiseur.api.puzzle.persistence.PuzzlePatch;
import re.belv.croiseur.api.puzzle.persistence.PuzzlePersistenceService;
import re.belv.croiseur.common.puzzle.ChangedPuzzle;
import re.belv.croiseur.common.puzzle.Puzzle;
import re.belv.croiseur.impl.puzzle.persistence.shared.SafePuzzleRepository;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;
import re.belv.croiseur.spi.puzzle.repository.PuzzleRepository;

/**
 * Implementation of {@link PuzzlePersistenceService}.
 *
 * <p>Mostly boilerplate between {@link PuzzleRepository} and {@link PuzzlePresenter}.
 */
public final class PuzzlePersistenceServiceImpl implements PuzzlePersistenceService {

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

    /**
     * Constructs an instance.
     *
     * @param repository the puzzle repository
     * @param presenter the puzzle presenter
     */
    public PuzzlePersistenceServiceImpl(final SafePuzzleRepository repository, final PuzzlePresenter presenter) {
        listPuzzlesUsecase = new ListPuzzlesUsecase(repository, presenter);
        deletePuzzleUsecase = new DeletePuzzleUsecase(repository);
        deleteAllPuzzlesUsecase = new DeleteAllPuzzlesUsecase(repository);
        loadPuzzleUsecase = new LoadPuzzleUsecase(repository, presenter);
        saveNewPuzzleUsecase = new SaveNewPuzzleUsecase(repository);
        saveChangedPuzzleUsecase = new SaveChangedPuzzleUsecase(repository);
        patchAndSavePuzzleUsecase = new PatchAndSavePuzzleUsecase(repository, presenter);
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
}
