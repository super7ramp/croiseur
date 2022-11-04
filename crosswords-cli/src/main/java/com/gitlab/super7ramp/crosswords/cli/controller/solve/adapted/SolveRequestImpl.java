package com.gitlab.super7ramp.crosswords.cli.controller.solve.adapted;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.cli.controller.solve.parsed.GridSize;
import com.gitlab.super7ramp.crosswords.cli.controller.solve.parsed.PrefilledBox;
import com.gitlab.super7ramp.crosswords.cli.controller.solve.parsed.PrefilledSlot;
import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.common.PuzzleDefinition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * Adapts command line arguments into a {@link SolveRequest}.
 */
public final class SolveRequestImpl implements SolveRequest {

    private final GridSize size;

    private final GridPosition[] shadedBoxes;

    private final PrefilledBox[] prefilledBoxes;

    private final PrefilledSlot[] prefilledHorizontalSlots;

    private final PrefilledSlot[] prefilledVerticalSlots;

    private final boolean progress;

    private final DictionaryIdentifier dictionaryId;

    /**
     * Constructor.
     *
     * @param aSize                        grid size
     * @param someShadedBoxes              the shaded boxes
     * @param somePrefilledBoxes           the prefilled boxes
     * @param somePrefilledHorizontalSlots the prefilled horizontal boxes
     * @param somePrefilledVerticalSlots   the prefilled vertical boxes
     * @param aDictionaryId                the dictionary identifier
     * @param aProgress                    whether progress should be notified
     */
    public SolveRequestImpl(final GridSize aSize,
                            final GridPosition[] someShadedBoxes,
                            final PrefilledBox[] somePrefilledBoxes,
                            final PrefilledSlot[] somePrefilledHorizontalSlots,
                            final PrefilledSlot[] somePrefilledVerticalSlots,
                            final DictionaryIdentifier aDictionaryId,
                            final boolean aProgress) {
        size = aSize;
        shadedBoxes = someShadedBoxes;
        prefilledBoxes = somePrefilledBoxes;
        prefilledHorizontalSlots = somePrefilledHorizontalSlots;
        prefilledVerticalSlots = somePrefilledVerticalSlots;
        dictionaryId = aDictionaryId;
        progress = aProgress;
    }

    @Override
    public PuzzleDefinition puzzle() {
        return new PuzzleDefinition(size.width(), size.height(),
                Arrays.stream(shadedBoxes).collect(toSet()), mergePrefilledBoxes());
    }

    @Override
    public Collection<DictionaryIdentifier> dictionaries() {
        if (dictionaryId != null) {
            return Collections.singletonList(dictionaryId);
        }
        return Collections.emptyList();
    }

    @Override
    public SolverProgressNotificationMethod progress() {
        if (progress) {
            return SolverProgressNotificationMethod.PERIODICAL;
        }
        return SolverProgressNotificationMethod.NONE;
    }

    /**
     * Merge {@link #prefilledBoxes}, {@link #prefilledHorizontalSlots} and
     * {@link #prefilledVerticalSlots} into a single map.
     *
     * @return the merged map
     * @throws IllegalArgumentException if boxes overlap
     */
    private Map<GridPosition, Character> mergePrefilledBoxes() {

        final BinaryOperator<Character> mergeFunction = (a, b) -> {
            if (a.equals(b)) {
                return a;
            }
            throw new IllegalArgumentException("Conflict in prefilled boxes");
        };

        final Map<GridPosition, Character> singleBoxes =
                Arrays.stream(prefilledBoxes)
                      .collect(toMap(PrefilledBox::gridPosition, PrefilledBox::value));

        final Map<GridPosition, Character> horizontalSlots =
                Arrays.stream(prefilledHorizontalSlots)
                      .flatMap(slot ->
                              OrientedPrefilledSlot.horizontal(slot)
                                                   .toMap()
                                                   .entrySet()
                                                   .stream())
                      .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction));

        final Map<GridPosition, Character> verticalSlots =
                Arrays.stream(prefilledVerticalSlots)
                      .flatMap(slot ->
                              OrientedPrefilledSlot.vertical(slot)
                                                   .toMap()
                                                   .entrySet()
                                                   .stream())
                      .collect(toMap(Map.Entry::getKey,
                              Map.Entry::getValue,
                              mergeFunction));

        return Stream.of(singleBoxes, horizontalSlots, verticalSlots)
                     .flatMap(map -> map.entrySet().stream())
                     .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction));
    }
}
