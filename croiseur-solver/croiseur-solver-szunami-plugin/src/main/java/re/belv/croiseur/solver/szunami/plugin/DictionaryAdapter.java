/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.szunami.plugin;

import re.belv.croiseur.spi.solver.Dictionary;

/** Adapts {@link Dictionary} into xwords-rs' {@link re.belv.croiseur.solver.szunami.Dictionary}. */
final class DictionaryAdapter {

    /** Private constructor - static utilities only. */
    private DictionaryAdapter() {
        // Nothing to do.
    }

    /**
     * Adapts {@link Dictionary} into xwords-rs' {@link re.belv.croiseur.solver.szunami.Dictionary}.
     *
     * @param dictionary a {@link Dictionary}
     * @return xwords-rs' {@link re.belv.croiseur.solver.szunami.Dictionary}
     */
    static re.belv.croiseur.solver.szunami.Dictionary adapt(final Dictionary dictionary) {
        return new re.belv.croiseur.solver.szunami.Dictionary(dictionary.words());
    }
}
