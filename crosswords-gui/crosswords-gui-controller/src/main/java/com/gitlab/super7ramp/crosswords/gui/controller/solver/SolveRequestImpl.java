package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;

import java.util.Collection;
import java.util.Map;

import static java.util.Comparator.comparingInt;

/**
 * Implementation of {@link SolveRequest}, adapting {@link CrosswordGridViewModel}.
 */
final class SolveRequestImpl implements SolveRequest {

    /** The puzzle definition. */
    private final PuzzleDefinition puzzle;

    /** Selected dictionaries. */
    private final Collection<DictionaryIdentifier> dictionaries;

    /**
     * Constructs an instance.
     *
     * @param crosswordGridViewModel the crossword model
     * @param dictionaryViewModel    the dictionary model
     */
    SolveRequestImpl(final CrosswordGridViewModel crosswordGridViewModel,
                     final DictionaryViewModel dictionaryViewModel) {

        final PuzzleDefinition.PuzzleDefinitionBuilder pdb =
                new PuzzleDefinition.PuzzleDefinitionBuilder();

        final Map<GridPosition, CrosswordBox> boxes = crosswordGridViewModel.boxes();
        for (final Map.Entry<GridPosition, CrosswordBox> box : boxes.entrySet()) {
            final GridPosition position = box.getKey();
            final CrosswordBox content = box.getValue();
            final boolean shaded = content.shadedProperty().get();
            if (shaded) {
                pdb.shade(position);
            } else if (!content.contentProperty().get().isEmpty()) {
                pdb.fill(position, content.contentProperty().get().charAt(0));
            } else {
                // Empty, not needed by solver
            }
        }

        boxes.keySet()
             .stream()
             .max(comparingInt(GridPosition::x).thenComparing(GridPosition::y))
             .ifPresent(maxCoordinate -> {
                 pdb.width(maxCoordinate.x() + 1);
                 pdb.height(maxCoordinate.y() + 1);
             });

        puzzle = pdb.build();
        dictionaries =
                dictionaryViewModel.selectedDictionariesProperty()
                                   .stream()
                                   .map(entry -> new DictionaryIdentifier(entry.provider(),
                                           entry.name()))
                                   .toList();
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
    public SolverProgressNotificationMethod progress() {
        return SolverProgressNotificationMethod.PERIODICAL;
    }
}

