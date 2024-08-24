/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.controller.solver;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.api.solver.SolveRequest;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;
import re.belv.croiseur.gui.view.model.CrosswordGridViewModel;
import re.belv.croiseur.gui.view.model.DictionariesViewModel;
import re.belv.croiseur.gui.view.model.GridCoord;
import re.belv.croiseur.gui.view.model.SolverConfigurationViewModel;

/**
 * Implementation of {@link SolveRequest}, adapting {@link CrosswordGridViewModel}.
 */
final class SolveRequestImpl implements SolveRequest {

    /** The puzzle definition. */
    private final PuzzleGrid puzzle;

    /** Selected dictionaries. */
    private final Collection<DictionaryIdentifier> dictionaries;

    /** The selected solver. */
    private final String selectedSolver;

    /** The source of randomness. */
    private final Random random;

    /** Whether to fill clues for the solution, if any. */
    private final boolean fillCluesOnSuccess;

    /**
     * Constructs an instance.
     *
     * @param crosswordGridViewModel       the crossword model
     * @param dictionariesViewModel        the dictionary model
     * @param solverConfigurationViewModel the solver configuration model
     * @param randomArg                    the source of randomness
     */
    SolveRequestImpl(
            final CrosswordGridViewModel crosswordGridViewModel,
            final DictionariesViewModel dictionariesViewModel,
            final SolverConfigurationViewModel solverConfigurationViewModel,
            final Random randomArg) {

        final var pdb = new PuzzleGrid.Builder();
        final var boxes = crosswordGridViewModel.boxesProperty();
        boxes.forEach((coord, box) -> {
            if (box.isShaded()) {
                pdb.shade(gridPositionFrom(coord));
            } else if (!box.userContent().isEmpty()) {
                pdb.fill(gridPositionFrom(coord), box.userContent().charAt(0));
            } // Else box empty, not needed by solver
        });
        pdb.width(crosswordGridViewModel.columnCount());
        pdb.height(crosswordGridViewModel.rowCount());

        puzzle = pdb.build();
        dictionaries = dictionariesViewModel.selectedDictionariesProperty().stream()
                .map(entry -> new DictionaryIdentifier(entry.provider(), entry.name()))
                .toList();

        selectedSolver = solverConfigurationViewModel.selectedSolver();
        random = randomArg;
        fillCluesOnSuccess = solverConfigurationViewModel.fillCluesOnSuccess();
    }

    @Override
    public PuzzleGrid grid() {
        return puzzle;
    }

    @Override
    public boolean savePuzzle() {
        return false;
    }

    @Override
    public Collection<DictionaryIdentifier> dictionaries() {
        return dictionaries;
    }

    @Override
    public Optional<Random> dictionariesShuffle() {
        return Optional.of(random);
    }

    @Override
    public Optional<String> solver() {
        return Optional.ofNullable(selectedSolver);
    }

    @Override
    public SolverProgressNotificationMethod progress() {
        return SolverProgressNotificationMethod.PERIODICAL;
    }

    @Override
    public boolean withClues() {
        return fillCluesOnSuccess;
    }

    private static GridPosition gridPositionFrom(final GridCoord coord) {
        return new GridPosition(coord.column(), coord.row());
    }
}
