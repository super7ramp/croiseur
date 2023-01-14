/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.solver.paulgb.plugin;

import com.gitlab.super7ramp.crosswords.spi.solver.Dictionary;

/**
 * Adapts {@link Dictionary} into Crossword Composer's
 * {@link com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary}.
 */
final class DictionaryAdapter {

    /** Private constructor - static utilities only. */
    private DictionaryAdapter() {
        // Nothing to do.
    }

    /**
     * Adapts {@link Dictionary} into Crossword Composer's
     * {@link com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary}.
     *
     * @param dictionary a {@link Dictionary}
     * @return Crossword Composer's
     * {@link com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary}
     */
    static com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary adapt(final Dictionary dictionary) {
        return new com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary(dictionary.lookup(word -> true)
                                                                                       .toArray(String[]::new));
    }
}
