package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.GridPosition;
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
    private final Map<GridPosition, Character> boxes;

    /** The kind of result. */
    private final Kind kind;

    /**
     * Constructor.
     *
     * @param someBoxes the solved grid
     */
    private SolverResultImpl(final Map<GridPosition, Character> someBoxes, final Kind aKind) {
        boxes = someBoxes;
        kind = aKind;
    }

    static SolverResultImpl success(final Map<GridPosition, Character> someBoxes) {
        return new SolverResultImpl(someBoxes, Kind.SUCCESS);
    }

    static SolverResultImpl impossible(final Map<GridPosition, Character> someBoxes) {
        return new SolverResultImpl(someBoxes, Kind.IMPOSSIBLE);
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
        SolverResultImpl that = (SolverResultImpl) o;
        return boxes.equals(that.boxes) && kind == that.kind;
    }

    @Override
    public int hashCode() {
        return Objects.hash(boxes, kind);
    }
}
