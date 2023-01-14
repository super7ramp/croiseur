/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.solver.ginsberg.grid;

interface BoxData {

    /** Empty character value. */
    char EMPTY_VALUE = ' ';

    /** Shaded value. */
    char SHADED_VALUE = '#';

    char value();

    boolean isShaded();

    void set(final char aCharacter);

    void reset();

    BoxData copy();
}
