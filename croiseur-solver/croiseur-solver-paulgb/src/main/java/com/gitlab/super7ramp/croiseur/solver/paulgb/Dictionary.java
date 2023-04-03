/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.paulgb;

/**
 * A dictionary.
 *
 * @param words the words
 */
// TODO change String[] to Set<String> to avoid useless copy on Java side
public record Dictionary(String[] words) {
    // Nothing to add.
}
