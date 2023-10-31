/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.model;

/**
 * A crossword.
 *
 * @param metadata the crossword metadata
 * @param grid the crossword grid
 * @param clues the crossword clues
 */
public record XdCrossword(XdMetadata metadata, XdGrid grid, XdClues clues) {
    // Nothing to add.
}
