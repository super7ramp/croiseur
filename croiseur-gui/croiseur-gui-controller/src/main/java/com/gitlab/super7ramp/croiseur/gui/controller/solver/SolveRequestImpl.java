/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.controller.solver;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.croiseur.api.solver.SolveRequest;
import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionariesViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverSelectionViewModel;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Implementation of {@link SolveRequest}, adapting {@link CrosswordGridViewModel}.
 */
final class SolveRequestImpl implements SolveRequest {

    /** The puzzle definition. */
    private final PuzzleDefinition puzzle;

    /** Selected dictionaries. */
    private final Collection<DictionaryIdentifier> dictionaries;

    /** The selected solver. */
    private final String selectedSolver;

    /** The source of randomness. */
    private final Random random;

    /**
     * Constructs an instance.
     *
     * @param crosswordGridViewModel   the crossword model
     * @param dictionariesViewModel    the dictionary model
     * @param solverSelectionViewModel the solver selection model
     * @param randomArg                the randomness source
     */
    SolveRequestImpl(final CrosswordGridViewModel crosswordGridViewModel,
                     final DictionariesViewModel dictionariesViewModel,
                     final SolverSelectionViewModel solverSelectionViewModel,
                     final Random randomArg) {

        final PuzzleDefinition.PuzzleDefinitionBuilder pdb =
                new PuzzleDefinition.PuzzleDefinitionBuilder();

        final Map<GridPosition, CrosswordBoxViewModel> boxes =
                crosswordGridViewModel.boxesProperty();
        for (final Map.Entry<GridPosition, CrosswordBoxViewModel> box : boxes.entrySet()) {
            final GridPosition position = box.getKey();
            final CrosswordBoxViewModel boxModel = box.getValue();
            if (boxModel.isShaded()) {
                pdb.shade(position);
            } else if (!boxModel.userContent().isEmpty()) {
                pdb.fill(position, boxModel.userContent().charAt(0));
            } else {
                // Empty, not needed by solver
            }
        }

        pdb.width(crosswordGridViewModel.columnCount());
        pdb.height(crosswordGridViewModel.rowCount());

        puzzle = pdb.build();
        dictionaries = dictionariesViewModel.selectedDictionariesProperty().stream()
                                            .map(entry -> new DictionaryIdentifier(entry.provider(),
                                                                                   entry.name()))
                                            .toList();

        selectedSolver = solverSelectionViewModel.selectedSolver();
        random = randomArg;
    }

    @Override
    public PuzzleDefinition puzzle() {
        return puzzle;
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
}

