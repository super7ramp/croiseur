/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.solver.adapter;

import com.gitlab.super7ramp.croiseur.api.dictionary.DictionaryIdentifier;
import com.gitlab.super7ramp.croiseur.api.solver.SolveRequest;
import com.gitlab.super7ramp.croiseur.cli.controller.solver.parser.GridSize;
import com.gitlab.super7ramp.croiseur.cli.controller.solver.parser.PrefilledBox;
import com.gitlab.super7ramp.croiseur.cli.controller.solver.parser.PrefilledSlot;
import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * Adapts command line arguments into a {@link SolveRequest}.
 */
public final class SolveRequestImpl implements SolveRequest {

    private final String solver;

    private final GridSize size;

    private final GridPosition[] shadedBoxes;

    private final PrefilledBox[] prefilledBoxes;

    private final PrefilledSlot[] prefilledHorizontalSlots;

    private final PrefilledSlot[] prefilledVerticalSlots;

    private final DictionaryIdentifier[] dictionaryIds;

    private final Random random;

    private final boolean progress;

    /**
     * Constructs an instance.
     *
     * @param solverArg                   the solver name
     * @param sizeArg                     grid size
     * @param someShadedBoxesArg          the shaded boxes
     * @param prefilledBoxesArg           the prefilled boxes
     * @param prefilledHorizontalSlotsArg the prefilled horizontal boxes
     * @param prefilledVerticalSlotsArg   the prefilled vertical boxes
     * @param dictionaryIdArg             the dictionary identifiers
     * @param randomArg                   the randomness source to shuffle dictionaries
     * @param progressArg                 whether progress should be notified
     */
    public SolveRequestImpl(final String solverArg,
                            final GridSize sizeArg,
                            final GridPosition[] someShadedBoxesArg,
                            final PrefilledBox[] prefilledBoxesArg,
                            final PrefilledSlot[] prefilledHorizontalSlotsArg,
                            final PrefilledSlot[] prefilledVerticalSlotsArg,
                            final DictionaryIdentifier[] dictionaryIdArg,
                            final Random randomArg,
                            final boolean progressArg) {
        solver = solverArg;
        size = sizeArg;
        shadedBoxes = someShadedBoxesArg;
        prefilledBoxes = prefilledBoxesArg;
        prefilledHorizontalSlots = prefilledHorizontalSlotsArg;
        prefilledVerticalSlots = prefilledVerticalSlotsArg;
        dictionaryIds = dictionaryIdArg;
        random = randomArg;
        progress = progressArg;
    }

    @Override
    public PuzzleDefinition puzzle() {
        return new PuzzleDefinition(size.width(), size.height(),
                                    Arrays.stream(shadedBoxes).collect(toSet()),
                                    mergePrefilledBoxes());
    }

    @Override
    public Collection<DictionaryIdentifier> dictionaries() {
        return dictionaryIds != null ? Arrays.asList(dictionaryIds) : Collections.emptyList();
    }

    @Override
    public Optional<Random> dictionariesShuffle() {
        return Optional.ofNullable(random);
    }

    @Override
    public Optional<String> solver() {
        return Optional.ofNullable(solver);
    }

    @Override
    public SolverProgressNotificationMethod progress() {
        return progress ? SolverProgressNotificationMethod.PERIODICAL :
                SolverProgressNotificationMethod.NONE;
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
