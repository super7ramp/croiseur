package com.gitlab.super7ramp.crosswords.cli;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * An oriented pre-filled slot.
 */
final class OrientedPrefilledSlot {

    /** The offset function. */
    private final BiFunction<Coordinate, Integer, Coordinate> coordinateOffset;

    /** The non-oriented slot data. */
    private final PrefilledSlot nonOrientedSlot;

    /**
     * Private constructor, use factory methods.
     *
     * @param aCoordinateOffset the offset function
     * @param aNonOrientedSlot  the non-oriented data
     */
    private OrientedPrefilledSlot(final BiFunction<Coordinate, Integer, Coordinate> aCoordinateOffset,
                                  final PrefilledSlot aNonOrientedSlot) {
        coordinateOffset = aCoordinateOffset;
        nonOrientedSlot = aNonOrientedSlot;
    }

    /**
     * Build a new vertical {@link OrientedPrefilledSlot}.
     *
     * @param aNonOrientedSlot the non-oriented data
     * @return a new vertical {@link OrientedPrefilledSlot}.
     */
    static OrientedPrefilledSlot vertical(PrefilledSlot aNonOrientedSlot) {
        return new OrientedPrefilledSlot((c, i) -> c.atVerticalOffset(i), aNonOrientedSlot);
    }

    /**
     * Build a new horizontal {@link OrientedPrefilledSlot}.
     *
     * @param aNonOrientedSlot the non-oriented data
     * @return a new horizontal {@link OrientedPrefilledSlot}.
     */
    static OrientedPrefilledSlot horizontal(PrefilledSlot aNonOrientedSlot) {
        return new OrientedPrefilledSlot((c, i) -> c.atHorizontalOffset(i), aNonOrientedSlot);
    }

    /**
     * Convert data to a map of Coordinate/Character.
     *
     * @return the corresponding map
     */
    Map<Coordinate, Character> toMap() {
        final Map<Coordinate, Character> map = new HashMap<>();
        for (int i = 0; i < nonOrientedSlot.value().length(); i++) {
            map.put(coordinateOffset.apply(nonOrientedSlot.startCoordinate(), i),
                    nonOrientedSlot.value().charAt(i));
        }
        return map;
    }
}
