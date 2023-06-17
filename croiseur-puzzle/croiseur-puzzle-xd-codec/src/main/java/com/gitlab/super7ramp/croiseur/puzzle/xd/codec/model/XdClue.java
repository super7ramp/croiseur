/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.xd.codec.model;

/**
 * A single clue, with its accompanying answer.
 *
 * @param number the clue number
 * @param clue   the clue
 * @param answer the answer
 */
public record XdClue(int number, String clue, String answer) {
    // Nothing to add.
}
