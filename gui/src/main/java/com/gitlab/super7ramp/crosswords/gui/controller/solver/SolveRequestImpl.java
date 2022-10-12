package com.gitlab.super7ramp.crosswords.gui.controller.solver;

import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.gui.fx.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.fx.model.IntCoordinate2D;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.DictionaryViewModel;
import com.gitlab.super7ramp.crosswords.spi.solver.GridPosition;
import com.gitlab.super7ramp.crosswords.spi.solver.ProgressListener;
import com.gitlab.super7ramp.crosswords.spi.solver.PuzzleDefinition;

import java.util.Map;
import java.util.Optional;

/**
 * Implementation of {@link SolveRequest}.
 */
final class SolveRequestImpl implements SolveRequest {

    /** The puzzle definition. */
    private final PuzzleDefinition puzzle;

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
        for (final Map.Entry<IntCoordinate2D, CrosswordBox> box : crosswordViewModel.boxes()
                                                                                    .entrySet()) {
            final IntCoordinate2D position = box.getKey();
            final CrosswordBox content = box.getValue();
            if (content.shadedProperty().get()) {
                pdb.shade(viewToDomain(position));
            } else if (!content.contentProperty().get().isEmpty()) {
                pdb.fill(viewToDomain(position), content.contentProperty().get().charAt(0));
            } else {
                // Empty, not needed by solver
            }
        }
        pdb.width(crosswordViewModel.width().get());
        pdb.height(crosswordViewModel.height().get());
        puzzle = pdb.build();
    }

    private static GridPosition viewToDomain(IntCoordinate2D viewCoordinate) {
        return new GridPosition(viewCoordinate.x(), viewCoordinate.y());
    }

    @Override
    public PuzzleDefinition puzzle() {
        return puzzle;
    }

    @Override
    public Optional<String> dictionaryId() {
        // TODO retrieve from dictionary model
        return Optional.empty();
    }

    @Override
    public ProgressListener progressListener() {
        return ProgressListener.DUMMY_LISTENER;
    }
}

