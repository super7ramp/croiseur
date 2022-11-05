package com.gitlab.super7ramp.crosswords.solver.ginsberg;

import com.gitlab.super7ramp.crosswords.common.GridPosition;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link SolverResult}.
 */
public final class SolverResultImpl implements SolverResult {

    /**
     * Dummy statistics.
     */
    private static class DummyStatistics implements Statistics {

        /**
         * Constructor.
         */
        DummyStatistics() {
            // Nothing to do.
        }

        @Override
        public long numberOfAssignments() {
            return 0;
        }

        @Override
        public long numberOfUnassignments() {
            return 0;
        }

        @Override
        public long eliminationSetSize() {
            return 0;
        }

        @Override
        public String toString() {
            return "(none recorded)";
        }
    }

    /** Not filled/shaded character. */
    private static final char NOT_FILLED = '#';

    /** Column separator. */
    private static final char COLUMN_SEPARATOR = '|';

    /** Line separator. */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /** The solved grid as boxes. */
    private final Map<GridPosition, Character> boxes;

    /** The kind of result. */
    private final Kind kind;

    /** Solver statistics. */
    private final Statistics statistics;

    /**
     * Constructor.
     *
     * @param someBoxes the solved grid
     */
    private SolverResultImpl(final Map<GridPosition, Character> someBoxes, final Kind aKind,
                             final Statistics someStatistics) {
        boxes = someBoxes;
        kind = aKind;
        statistics = someStatistics;
    }

    static SolverResultImpl success(final Map<GridPosition, Character> someBoxes,
                                    final Statistics someStatistics) {
        return new SolverResultImpl(someBoxes, Kind.SUCCESS, someStatistics);
    }

    static SolverResultImpl impossible(final Map<GridPosition, Character> someBoxes,
                                       final Statistics someStatistics) {
        return new SolverResultImpl(someBoxes, Kind.IMPOSSIBLE, someStatistics);
    }

    static SolverResultImpl success(final Map<GridPosition, Character> someBoxes) {
        return success(someBoxes, new DummyStatistics());
    }

    static SolverResultImpl impossible(final Map<GridPosition, Character> someBoxes) {
        return impossible(someBoxes, new DummyStatistics());
    }

    @Override
    public Kind kind() {
        return kind;
    }

    @Override
    public Map<GridPosition, Character> boxes() {
        return Collections.unmodifiableMap(boxes);
    }

    @Override
    public Statistics statistics() {
        return statistics;
    }

    @Override
    public String toString() {
        final int width = boxes.keySet()
                               .stream()
                               .map(coordinate -> coordinate.x() + 1)
                               .max(Comparator.naturalOrder())
                               .orElse(0);

        final int height = boxes.keySet()
                                .stream()
                                .map(coordinate -> coordinate.y() + 1)
                                .max(Comparator.naturalOrder())
                                .orElse(0);

        final StringBuilder sb = new StringBuilder();
        sb.append("Result: ").append(kind.toString()).append(LINE_SEPARATOR);
        sb.append("Grid:").append(LINE_SEPARATOR);
        for (int y = 0; y < height; y++) {
            sb.append('|');
            for (int x = 0; x < width; x++) {
                final Character value = boxes.get(new GridPosition(x, y));
                if (value != null) {
                    sb.append(value);
                } else {
                    sb.append(NOT_FILLED);
                }
                sb.append(COLUMN_SEPARATOR);
            }
            sb.append(LINE_SEPARATOR);
        }
        sb.append("Statistics: ").append(statistics.toString()).append(LINE_SEPARATOR);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SolverResultImpl that = (SolverResultImpl) o;
        // TODO assess whether statistics should be included in equals()+hashCoded()
        return boxes.equals(that.boxes) && kind == that.kind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxes, kind);
    }
}
