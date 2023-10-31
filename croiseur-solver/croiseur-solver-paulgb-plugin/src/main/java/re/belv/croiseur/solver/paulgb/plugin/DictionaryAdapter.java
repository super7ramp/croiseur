/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.paulgb.plugin;

import re.belv.croiseur.spi.solver.Dictionary;

/**
 * Adapts {@link Dictionary} into Crossword Composer's
 * {@link re.belv.croiseur.solver.paulgb.Dictionary}.
 */
final class DictionaryAdapter {

    /** Private constructor - static utilities only. */
    private DictionaryAdapter() {
        // Nothing to do.
    }

    /**
     * Adapts {@link Dictionary} into Crossword Composer's
     * {@link re.belv.croiseur.solver.paulgb.Dictionary}.
     *
     * @param dictionary a {@link Dictionary}
     * @return Crossword Composer's
     * {@link re.belv.croiseur.solver.paulgb.Dictionary}
     */
    static re.belv.croiseur.solver.paulgb.Dictionary adapt(final Dictionary dictionary) {
        return new re.belv.croiseur.solver.paulgb.Dictionary(dictionary.words());
    }
}
