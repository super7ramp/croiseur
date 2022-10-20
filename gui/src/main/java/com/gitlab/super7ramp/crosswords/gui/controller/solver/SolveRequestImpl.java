package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.gui.controls.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.controls.model.IntCoordinate2D;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.DictionaryViewModel;
import com.gitlab.super7ramp.crosswords.spi.solver.GridPosition;
import com.gitlab.super7ramp.crosswords.spi.solver.ProgressListener;
import com.gitlab.super7ramp.crosswords.spi.solver.PuzzleDefinition;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static java.util.Comparator.comparingInt;

/**
 * Implementation of {@link SolveRequest}, adapting {@link CrosswordViewModel}.
 */
final class SolveRequestImpl implements SolveRequest {

    /** The puzzle definition. */
    private final PuzzleDefinition puzzle;

    /** Selected dictionaries. */
    private final Collection<DictionaryIdentifier> dictionaries;

    /**
     * Constructs an instance.
     *
     * @param crosswordViewModel  the crossword model
     * @param dictionaryViewModel the dictionary model
     */
    SolveRequestImpl(final CrosswordViewModel crosswordViewModel,
                     final DictionaryViewModel dictionaryViewModel) {

        final PuzzleDefinition.PuzzleDefinitionBuilder pdb =
                new PuzzleDefinition.PuzzleDefinitionBuilder();

        final Map<IntCoordinate2D, CrosswordBox> boxes = crosswordViewModel.boxes();
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
        final String selectedDictionary = dictionaryViewModel.selectedDictionary().get();
        if (selectedDictionary != null && !selectedDictionary.isEmpty()) {
            dictionaries =
                    Collections.singletonList(DictionaryIdentifier.valueOf(selectedDictionary));
        } else {
            dictionaries = Collections.emptyList();
        }
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
    public ProgressListener progressListener() {
        return ProgressListener.DUMMY_LISTENER;
    }
}

