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
import java.util.Collections;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toSet;

/**
 * "solve-grid" subcommand.
 */
@Command(
        name = "solve",
        description = "Solve a crossword puzzle"
)
final class SolveCommand implements Runnable {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(SolveCommand.class.getName());
    @Option(names = {"-s", "--size"}, arity = "1", required = true, description = "Grid dimension")
    private GridSize size;
    // TODO allow optional => use default dictionary => sort dictionaries
    //  (prefer system's locale, implement criteria on provider)
    @Option(names = {"-d", "--dictionary"}, arity = "1", required = true,
            paramLabel = "<[provider:]dictionary>", description = "Dictionary identifier")
    private String dictionary;
    @Option(names = {"-S", "--shaded"}, arity = "1..*", description = "Shaded box coordinates")
    private Coordinate[] shaded;

    /**
     * Constructor.
     */
    SolveCommand() {
        // Nothing to do.
    }

    private static com.gitlab.super7ramp.crosswords.solver.api.Dictionary wrap(Dictionary dictionary) {
        return new com.gitlab.super7ramp.crosswords.solver.api.Dictionary() {
            @Override
            public Set<String> lookup(Predicate<String> predicate) {
                return dictionary.lookup(predicate);
            }

            @Override
            public boolean contains(String value) {
                // FIXME should not be solver dictionary API
                return !dictionary.lookup(value::equals).isEmpty();
            }
        };
    }

    @Override
    public void run() {
        LOGGER.info("solve-grid command launched");
        LOGGER.info("Size is " + size);
        LOGGER.info("Dictionary is " + dictionary);
        if (shaded.length > 0) {
            LOGGER.info("Shaded boxes are " + Arrays.toString(shaded));
        }

        final Dictionary dictionary =
                DictionaryLoader.get(DictionaryLoader.Search.includeAll(), DictionaryLoader.Search.byName("fr" +
                        ".obj")).values().iterator().next().iterator().next();

        final PuzzleDefinition puzzle = new PuzzleDefinition(size.width(), size.height(),
                Arrays.stream(shaded).collect(toSet()), Collections.emptyMap());

        final com.gitlab.super7ramp.crosswords.solver.api.Dictionary adaptedDictionary = wrap(dictionary);
        try {
            final SolverResult result = CrosswordSolverLoader.get().solve(puzzle, adaptedDictionary,
                    new ProgressListenerImpl());
            System.out.println(result);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implementation of {@link ProgressListener}.
     */
    static final class ProgressListenerImpl implements ProgressListener {

        /**
         * Constructor.
         */
        ProgressListenerImpl() {
            // Nothing to do.
        }

        @Override
        public void onSolverProgressUpdate(final short completionPercentage) {
            System.out.println("Completion: " + completionPercentage + " %");
        }
    }
}
