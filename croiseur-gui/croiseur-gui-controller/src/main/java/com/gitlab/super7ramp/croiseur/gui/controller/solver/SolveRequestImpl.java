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

    /**
     * Constructs an instance.
     *
     * @param crosswordGridViewModel   the crossword model
     * @param dictionariesViewModel    the dictionary model
     * @param solverSelectionViewModel the solver selection model
     */
    SolveRequestImpl(final CrosswordGridViewModel crosswordGridViewModel,
                     final DictionariesViewModel dictionariesViewModel,
                     final SolverSelectionViewModel solverSelectionViewModel) {

        final PuzzleDefinition.PuzzleDefinitionBuilder pdb =
                new PuzzleDefinition.PuzzleDefinitionBuilder();

        final Map<GridPosition, CrosswordBoxViewModel> boxes = crosswordGridViewModel.boxesProperty();
        for (final Map.Entry<GridPosition, CrosswordBoxViewModel> box : boxes.entrySet()) {
            final GridPosition position = box.getKey();
            final CrosswordBoxViewModel content = box.getValue();
            final boolean shaded = content.isShaded();
            if (shaded) {
                pdb.shade(position);
            } else if (!content.getContent().isEmpty()) {
                pdb.fill(position, content.getContent().charAt(0));
            } else {
                // Empty, not needed by solver
            }
        }

        pdb.width(crosswordGridViewModel.columnCount());
        pdb.height(crosswordGridViewModel.rowCount());

        puzzle = pdb.build();
        dictionaries =
                dictionariesViewModel.selectedDictionariesProperty()
                                     .stream()
                                     .map(entry -> new DictionaryIdentifier(entry.provider(),
                                                                            entry.name()))
                                     .toList();

        selectedSolver = solverSelectionViewModel.getSelectedSolver();
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
        return Optional.empty();
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

