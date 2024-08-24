/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.controller.solver.adapter;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;
import re.belv.croiseur.api.dictionary.DictionaryIdentifier;
import re.belv.croiseur.api.solver.SolveRequest;
import re.belv.croiseur.cli.controller.solver.parser.GridSize;
import re.belv.croiseur.cli.controller.solver.parser.PrefilledBox;
import re.belv.croiseur.cli.controller.solver.parser.PrefilledSlot;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.common.puzzle.PuzzleGrid;

/**
 * Adapts command line arguments into a {@link SolveRequest}.
 */
public final class CliSolveRequest implements SolveRequest {

    /**
     * A builder. Unless specified otherwise, all method arguments are required to be
     * {@code non-null}.
     */
    public static final class Builder {

        private String solver;
        private GridSize size;
        private GridPosition[] shadedBoxes;
        private PrefilledBox[] prefilledBoxes;
        private PrefilledSlot[] prefilledHorizontalSlots;
        private PrefilledSlot[] prefilledVerticalSlots;
        private DictionaryIdentifier[] dictionaryIds;
        private Random random;
        private boolean progress;
        private boolean clues;
        private boolean save;

        /**
         * Constructs an instance.
         */
        public Builder() {
            // Nothing to do.
        }

        /**
         * Specifies the solver name. May be {@code null}.
         *
         * @param solverArg the solver name
         * @return this builder for chaining
         */
        public Builder solver(final String solverArg) {
            solver = solverArg;
            return this;
        }

        /**
         * Specifies the grid size.
         *
         * @param sizeArg the grid size
         * @return this builder for chaining
         */
        public Builder size(final GridSize sizeArg) {
            size = Objects.requireNonNull(sizeArg);
            return this;
        }

        /**
         * Specifies the positions of the shaded boxes, if any.
         *
         * @param shadedBoxesArg the positions of the shaded boxes
         * @return this builder for chaining
         */
        public Builder shadedBoxes(final GridPosition[] shadedBoxesArg) {
            shadedBoxes = Objects.requireNonNull(shadedBoxesArg);
            return this;
        }

        /**
         * Specifies the pre-filled boxes, if any.
         *
         * @param prefilledBoxesArg the pre-filled boxes
         * @return this builder for chaining
         */
        public Builder prefilledBoxes(final PrefilledBox[] prefilledBoxesArg) {
            prefilledBoxes = Objects.requireNonNull(prefilledBoxesArg);
            return this;
        }

        /**
         * Specifies the pre-filled horizontal slots, if any.
         *
         * @param prefilledHorizontalSlotsArg the prefilled horizontal slots
         * @return this builder for chaining
         */
        public Builder prefilledHorizontalSlots(final PrefilledSlot[] prefilledHorizontalSlotsArg) {
            prefilledHorizontalSlots = Objects.requireNonNull(prefilledHorizontalSlotsArg);
            return this;
        }

        /**
         * Specifies the pre-filled vertical slots, if any.
         *
         * @param prefilledVerticalSlotsArg the prefilled vertical slots
         * @return this builder for chaining
         */
        public Builder prefilledVerticalSlots(final PrefilledSlot[] prefilledVerticalSlotsArg) {
            prefilledVerticalSlots = Objects.requireNonNull(prefilledVerticalSlotsArg);
            return this;
        }

        /**
         * Specifies the dictionary identifiers, if any.
         *
         * @param dictionaryIdsArg the dictionary identifiers
         * @return this builder for chaining
         */
        public Builder dictionaryIds(final DictionaryIdentifier[] dictionaryIdsArg) {
            dictionaryIds = Objects.requireNonNull(dictionaryIdsArg);
            return this;
        }

        /**
         * Specifies the randomness source, if any. May be {@code null}.
         *
         * @param randomArg the randomness source
         * @return this builder for chaining
         */
        public Builder random(final Random randomArg) {
            random = randomArg;
            return this;
        }

        /**
         * Specifies whether the progress should be notified.
         *
         * @param progressArg whether progress should be notified
         * @return this builder for chaining
         */
        public Builder progress(final boolean progressArg) {
            progress = progressArg;
            return this;
        }

        /**
         * Specifies whether the clues should be retrieved if a solution is found.
         *
         * @param cluesArg whether the clues should be retrieved if a solution is found
         * @return this builder for chaining
         */
        public Builder clues(final boolean cluesArg) {
            clues = cluesArg;
            return this;
        }

        /**
         * Specifies whether the grid should be saved to puzzle repository.
         *
         * @param saveArg the grid should be saved to puzzle repository
         * @return this builder for chaining
         */
        public Builder save(final boolean saveArg) {
            save = saveArg;
            return this;
        }

        /**
         * Builds the request.
         *
         * @return the built request
         */
        public CliSolveRequest build() {

            final var puzzleGrid = new PuzzleGrid(
                    size.width(), size.height(), Arrays.stream(shadedBoxes).collect(toSet()), mergePrefilledBoxes());

            final Collection<DictionaryIdentifier> dictionaries = Arrays.asList(dictionaryIds);

            final var solverProgress =
                    progress ? SolverProgressNotificationMethod.PERIODICAL : SolverProgressNotificationMethod.NONE;

            return new CliSolveRequest(solver, puzzleGrid, dictionaries, random, solverProgress, clues, save);
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
                    Arrays.stream(prefilledBoxes).collect(toMap(PrefilledBox::gridPosition, PrefilledBox::value));

            final Map<GridPosition, Character> horizontalSlots = Arrays.stream(prefilledHorizontalSlots)
                    .flatMap(slot -> OrientedPrefilledSlot.horizontal(slot).toMap().entrySet().stream())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction));

            final Map<GridPosition, Character> verticalSlots = Arrays.stream(prefilledVerticalSlots)
                    .flatMap(slot -> OrientedPrefilledSlot.vertical(slot).toMap().entrySet().stream())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction));

            return Stream.of(singleBoxes, horizontalSlots, verticalSlots)
                    .flatMap(map -> map.entrySet().stream())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction));
        }
    }

    private final String solver;
    private final PuzzleGrid puzzleGrid;
    private final Collection<DictionaryIdentifier> dictionaryIds;
    private final Random random;
    private final SolverProgressNotificationMethod progress;
    private final boolean clues;
    private final boolean save;

    /**
     * Constructs an instance.
     *
     * @param solverArg       the solver name
     * @param puzzleGridArg   the puzzle grid
     * @param dictionaryIdArg the dictionary identifiers
     * @param randomArg       the randomness source to shuffle dictionaries
     * @param progressArg     whether progress should be notified
     * @param cluesArg        whether to generate clues for result slot words
     * @param saveArg         whether given grid shall be saved
     */
    private CliSolveRequest(
            final String solverArg,
            final PuzzleGrid puzzleGridArg,
            final Collection<DictionaryIdentifier> dictionaryIdArg,
            final Random randomArg,
            final SolverProgressNotificationMethod progressArg,
            final boolean cluesArg,
            final boolean saveArg) {
        solver = solverArg;
        puzzleGrid = puzzleGridArg;
        dictionaryIds = dictionaryIdArg;
        random = randomArg;
        progress = progressArg;
        clues = cluesArg;
        save = saveArg;
    }

    @Override
    public PuzzleGrid grid() {
        return puzzleGrid;
    }

    @Override
    public boolean savePuzzle() {
        return save;
    }

    @Override
    public Collection<DictionaryIdentifier> dictionaries() {
        return dictionaryIds;
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
        return progress;
    }

    @Override
    public boolean withClues() {
        return clues;
    }
}
