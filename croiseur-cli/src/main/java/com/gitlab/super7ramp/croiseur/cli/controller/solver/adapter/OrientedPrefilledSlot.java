/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.solver.adapter;

import com.gitlab.super7ramp.croiseur.cli.controller.solver.parser.PrefilledSlot;
import com.gitlab.super7ramp.croiseur.common.GridPosition;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * An oriented pre-filled slot.
 */
final class OrientedPrefilledSlot {

    /** The offset function. */
    private final BiFunction<GridPosition, Integer, GridPosition> coordinateOffset;

    /** The non-oriented slot data. */
    private final PrefilledSlot nonOrientedSlot;

    /**
     * Private constructor, use factory methods.
     *
     * @param aCoordinateOffset the offset function
     * @param aNonOrientedSlot  the non-oriented data
     */
    private OrientedPrefilledSlot(final BiFunction<GridPosition, Integer, GridPosition> aCoordinateOffset,
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
        return new OrientedPrefilledSlot(GridPosition::plusVerticalOffset, aNonOrientedSlot);
    }

    /**
     * Build a new horizontal {@link OrientedPrefilledSlot}.
     *
     * @param aNonOrientedSlot the non-oriented data
     * @return a new horizontal {@link OrientedPrefilledSlot}.
     */
    static OrientedPrefilledSlot horizontal(PrefilledSlot aNonOrientedSlot) {
        return new OrientedPrefilledSlot(GridPosition::plusHorizontalOffset, aNonOrientedSlot);
    }

    /**
     * Convert data to a map of Coordinate/Character.
     *
     * @return the corresponding map
     */
    Map<GridPosition, Character> toMap() {
        final Map<GridPosition, Character> map = new HashMap<>();
        for (int i = 0; i < nonOrientedSlot.value().length(); i++) {
            map.put(coordinateOffset.apply(nonOrientedSlot.startGridPosition(), i),
                    nonOrientedSlot.value().charAt(i));
        }
        return map;
    }
}
