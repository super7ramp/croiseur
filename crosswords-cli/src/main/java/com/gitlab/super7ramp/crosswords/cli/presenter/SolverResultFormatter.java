package com.gitlab.super7ramp.crosswords.cli.presenter;

import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;
import picocli.CommandLine;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Formats the solver result.
 */
final class SolverResultFormatter {

    /** Shaded/empty box. */
    private static final char EMPTY = ' ';

    /** Column separator. */
    private static final char COLUMN_SEPARATOR = '|';

    /** Line separator. */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /** The format used to display unsolvable boxes. */
    public static final String UNSOLVABLE_FORMAT = "bg(red)";

    /** The default format used to display boxes. */
    public static final String DEFAULT_FORMAT = "reset";

    /**
     * Private constructor, static utility methods only.
     */
    private SolverResultFormatter() {
        // Nothing to do.
    }

    /**
     * Formats the solver result.
     *
     * @return the formatted solver result
     */
    static String format(final SolverResult result) {

        final SolverResult.Kind kind = result.kind();
        final Map<GridPosition, Character> filledBoxes = result.filledBoxes();
        final Set<GridPosition> unsolvableBoxes = result.unsolvableBoxes();

        final StringBuilder sb = new StringBuilder();
        sb.append("Result: ").append(kind);
        sb.append(LINE_SEPARATOR).append(LINE_SEPARATOR);

        final Set<GridPosition> positions = new HashSet<>(filledBoxes.keySet());
        positions.addAll(unsolvableBoxes);
        final int width = width(positions);
        final int height = height(positions);
        for (int y = 0; y < height; y++) {
            sb.append('|');
            for (int x = 0; x < width; x++) {
                final GridPosition position = new GridPosition(x, y);
                final Character value = filledBoxes.getOrDefault(position, EMPTY);
                final String format = unsolvableBoxes.contains(position) ? UNSOLVABLE_FORMAT :
                        DEFAULT_FORMAT;
                final String box =
                        CommandLine.Help.Ansi.AUTO.string("@|" + format + " " + value + "|@");
                sb.append(box);
                sb.append(COLUMN_SEPARATOR);
            }
            sb.append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    private static int width(final Set<GridPosition> positions) {
        return positions.stream()
                        .map(GridPosition::x)
                        .max(Comparator.naturalOrder())
                        .orElse(-1) + 1;
    }

    private static int height(final Set<GridPosition> positions) {
        return positions.stream()
                        .map(GridPosition::y)
                        .max(Comparator.naturalOrder())
                        .orElse(-1) + 1;
    }

}
