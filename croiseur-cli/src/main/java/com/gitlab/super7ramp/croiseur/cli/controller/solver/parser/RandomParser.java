/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.cli.controller.solver.parser;

import com.gitlab.super7ramp.croiseur.common.GridPosition;

import java.util.Random;

/**
 * Parser for {@link Random}. Expects the value of the seed.
 */
public final class RandomParser {

    /** Private constructor to prevent instantiation. */
    private RandomParser() {
        // Nothing to do.
    }

    /**
     * Parses the textual representation of a {@link Random}.
     *
     * @param seedValue the value to parse
     * @return the parsed {@link GridPosition}
     * @throws IllegalArgumentException if given string cannot be parsed
     */
    public static Random parse(final String seedValue) {
        final Random random;
        if (seedValue == null || seedValue.isEmpty()) {
            random = new Random();
        } else {
            final long seed = Long.parseLong(seedValue);
            random = new Random(seed);
        }
        return random;
    }
}
