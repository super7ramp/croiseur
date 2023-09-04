/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.model.puzzle;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Special deserializer for {@link GridPosition}.
 */
public final class GridPositionDeserializer extends KeyDeserializer {

    /** The textual representation. */
    // TODO it would be better to have just (\\d,\\d). Either:
    //  - Create a custom serializer
    //  - Create an entire custom data model with json serialization in mind (i.e. don't reuse
    //  common types but define a specific data model, like it's done in gui-view-model and
    //  perform conversions in controller/presenter)
    private static final Pattern PATTERN =
            Pattern.compile("GridPosition\\{x=(?<x>\\d+), y=(?<y>\\d+)}");

    /**
     * Constructs an instance.
     */
    public GridPositionDeserializer() {
        // Nothing to do.
    }

    @Override
    public GridPosition deserializeKey(final String key, final DeserializationContext context) {
        final Matcher matcher = PATTERN.matcher(key);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid format: Expected " + PATTERN.pattern() +
                                               ", was " + key);
        }
        final int x = Integer.parseInt(matcher.group("x"));
        final int y = Integer.parseInt(matcher.group("y"));
        return new GridPosition(x, y);
    }
}
