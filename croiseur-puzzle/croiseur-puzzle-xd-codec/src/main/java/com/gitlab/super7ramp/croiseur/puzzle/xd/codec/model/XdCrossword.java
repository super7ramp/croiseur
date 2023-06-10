/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.xd.codec.model;

public final class XdCrossword {

    private final XdMetadata metadata;

    private final XdGrid grid;

    private final XdClues clues;

    XdCrossword(final XdMetadata someMetadata, final XdGrid aGrid, final XdClues someClues) {
        metadata = someMetadata;
        grid = aGrid;
        clues = someClues;
    }

}
