package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;
import com.gitlab.super7ramp.crosswords.solver.api.SolverResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of {@link SolverResult}.
 */
public final class SolverResultImpl implements SolverResult {

    /** Not filled/shaded character. */
    private static final char NOT_FILLED = '#';

    /** Column separator. */
    private static final char COLUMN_SEPARATOR = '|';

    /** Line separator. */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /** The solved grid as boxes. */
    private final Map<Coordinate, Character> boxes;

    /**
     * Constructor.
     *
     * @param someBoxes the solved grid
     */
    SolverResultImpl(final Map<Coordinate, Character> someBoxes) {
        boxes = someBoxes;
    }

    @Override
    public Map<Coordinate, Character> boxes() {
        return Collections.unmodifiableMap(boxes);
    }

    @Override
    public String toString() {
        final int width = boxes.keySet().stream()
                .map(coordinate -> coordinate.x() + 1)
                .max(Comparator.naturalOrder())
                .orElse(0);

        final int height = boxes.keySet().stream()
                .map(coordinate -> coordinate.y() + 1)
                .max(Comparator.naturalOrder())
                .orElse(0);

        final StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; y++) {
            sb.append('|');
            for (int x = 0; x < width; x++) {
                Character value = boxes.get(new Coordinate(x, y));
                if (value != null) {
                    sb.append(value);
                } else {
                    sb.append(NOT_FILLED);
                }
                sb.append(COLUMN_SEPARATOR);
            }
            sb.append(LINE_SEPARATOR);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SolverResultImpl that = (SolverResultImpl) o;
        return Objects.equals(boxes, that.boxes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxes);
    }
}
