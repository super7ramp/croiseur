package com.gitlab.super7ramp.crosswords.cli;

import com.gitlab.super7ramp.crosswords.dictionary.api.Dictionary;
import com.gitlab.super7ramp.crosswords.dictionary.api.DictionaryLoader;
import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;
import com.gitlab.super7ramp.crosswords.solver.api.CrosswordSolverLoader;
import com.gitlab.super7ramp.crosswords.solver.api.ProgressListener;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * "solve" subcommand.
 */
@Command(name = "solve", description = "Solve a crossword puzzle")
final class SolveCommand implements Runnable {

    /**
     * Implementation of {@link ProgressListener}.
     */
    private static final class ProgressListenerImpl implements ProgressListener {

        /** The message format. */
        private static final String PROGRESS_FORMAT = "Completion: %3d %% [best: %3d %%]\r";

        /** The best completion percentage reached. */
        private short bestCompletionPercentage;

        /**
         * Constructor.
         */
        ProgressListenerImpl() {
            // Nothing to do.
        }

        @Override
        public void onSolverProgressUpdate(final short completionPercentage) {
            if (completionPercentage > bestCompletionPercentage) {
                bestCompletionPercentage = completionPercentage;
            }
            System.err.printf(PROGRESS_FORMAT, completionPercentage, bestCompletionPercentage);
        }
    }

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(SolveCommand.class.getName());

    /** Dictionary service loader. */
    private final DictionaryLoader dictionaryLoader;

    /** Solver service loader. */
    private final CrosswordSolverLoader solverLoader;

    @Option(names = {"-s", "--size"}, paramLabel = "<INTEGERxINTEGER>", arity = "1", required =
            true, description = "Grid dimensions, e.g. '--size 7x15' for a grid of width 7 and" +
            "height 15")
    private GridSize size;

    // TODO allow optional => use default dictionary => sort dictionaries
    //  (prefer system's locale, implement criteria on provider)
    @Option(names = {"-d", "--dictionary"}, paramLabel = "<[PROVIDER:]DICTIONARY>", arity = "1",
            required = true, description = "Dictionary identifier")
    private String dictionary;

    @Option(names = {"-B", "--shaded-box", "--shaded-boxes"}, arity = "1..*", description =
            "Shaded boxes, e.g. '--shaded-boxes (1,2) (3,4)...'", paramLabel = "<COORDINATE> ")
    private Coordinate[] shadedBoxes = {};

    @Option(names = {"-b", "--box", "--boxes"}, arity = "1..*", description = "Pre-filled boxes, " +
            "e.g. '--boxes ((1,2),A) ((3,4),B)...'", paramLabel = "<(COORDINATE,LETTER)> ")
    private PrefilledBox[] prefilledBoxes = {};

    @Option(names = {"-H", "--horizontal"}, arity = "1..*", description = "Pre-filled horizontal " +
            "slot(s), e.g. '--horizontal ((0,0),hello) ((5,0),world)...",
            paramLabel = "<(COORDINATE,WORD)> ")
    private PrefilledSlot[] prefilledHorizontalSlots = {};

    @Option(names = {"-V", "--vertical"}, arity = "1..*", description = "Pre-filled vertical " +
            "slot(s), e.g. '--vertical ((0,0),hello) ((5,0),world)...",
            paramLabel = "<(COORDINATE,WORD)> ")
    private PrefilledSlot[] prefilledVerticalSlots = {};

    @Option(names = {"-p", "--progress"}, description = "Show solver progress")
    private boolean progress;

    /**
     * Constructor.
     *
     * @param aDictionaryLoader dictionary service loader
     * @param aSolverLoader     solver service loader
     */
    SolveCommand(final CrosswordSolverLoader aSolverLoader,
                 final DictionaryLoader aDictionaryLoader) {
        solverLoader = aSolverLoader;
        dictionaryLoader = aDictionaryLoader;
    }

    @Override
    public void run() {
        final Dictionary dictionary =
                dictionaryLoader.get(DictionaryLoader.Search.includeAll(),
                                        DictionaryLoader.Search.byName("fr" + ".obj"))
                                .values()
                                .iterator()
                                .next()
                                .iterator()
                                .next();

        final PuzzleDefinition puzzle = new PuzzleDefinition(size.width(), size.height(),
                Arrays.stream(shadedBoxes).collect(toSet()), mergePrefilledBoxes());

        final ProgressListener progressListener = createProgressListener();
        try {
            final SolverResult result = solverLoader.get()
                                                    .solve(puzzle, dictionary::lookup,
                                                            progressListener);
            System.out.print("\n" + result);
        } catch (final InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Solver interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Create the {@link ProgressListener}.
     *
     * @return the progress listener
     */
    private ProgressListener createProgressListener() {
        final ProgressListener progressListener;
        if (progress) {
            progressListener = new ProgressListenerImpl();
        } else {
            progressListener = ProgressListener.DUMMY_LISTENER;
        }
        return progressListener;
    }

    /**
     * Merge {@link #prefilledBoxes}, {@link #prefilledHorizontalSlots} and
     * {@link #prefilledVerticalSlots} into a single map.
     *
     * @return the merged map
     * @throws IllegalArgumentException if boxes overlap
     */
    private Map<Coordinate, Character> mergePrefilledBoxes() {

        final BinaryOperator<Character> mergeFunction = (a, b) -> {
            if (a.equals(b)) {
                return a;
            }
            throw new IllegalArgumentException("Conflict in prefilled boxes");
        };

        final Map<Coordinate, Character> singleBoxes =
                Arrays.stream(prefilledBoxes)
                      .collect(toMap(PrefilledBox::coordinate, PrefilledBox::value));

        final Map<Coordinate, Character> horizontalSlots =
                Arrays.stream(prefilledHorizontalSlots)
                      .flatMap(slot ->
                              OrientedPrefilledSlot.horizontal(slot)
                                                   .toMap()
                                                   .entrySet()
                                                   .stream())
                      .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, mergeFunction));

        final Map<Coordinate, Character> verticalSlots =
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
