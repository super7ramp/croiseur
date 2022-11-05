package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.gui.control.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.control.model.IntCoordinate2D;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordGridViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.DictionaryViewModel;

import java.util.Collection;
import java.util.Collections;
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

        final Map<IntCoordinate2D, CrosswordBox> boxes = crosswordGridViewModel.boxes();
        for (final Map.Entry<IntCoordinate2D, CrosswordBox> box : boxes.entrySet()) {
            final IntCoordinate2D position = box.getKey();
            final CrosswordBox content = box.getValue();
            final boolean shaded = content.shadedProperty().get();
            if (shaded) {
                pdb.shade(viewToDomain(position));
            } else if (!content.contentProperty().get().isEmpty()) {
                pdb.fill(viewToDomain(position), content.contentProperty().get().charAt(0));
            } else {
                // Empty, not needed by solver
            }
        }

        boxes.keySet()
             .stream()
             .max(comparingInt(IntCoordinate2D::x).thenComparing(IntCoordinate2D::y))
             .ifPresent(maxCoordinate -> {
                 pdb.width(maxCoordinate.x() + 1);
                 pdb.height(maxCoordinate.y() + 1);
             });

        puzzle = pdb.build();

        // TODO update when view model supports several selected dictionaries
        dictionaries = Collections.emptyList();
    }

    /**
     * Converts a {@link IntCoordinate2D} to {@link GridPosition}.
     *
     * @param viewCoordinate the view type
     * @return the domain type
     */
    private static GridPosition viewToDomain(final IntCoordinate2D viewCoordinate) {
        return new GridPosition(viewCoordinate.x(), viewCoordinate.y());
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

