/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.szunami.plugin;

import com.gitlab.super7ramp.croiseur.spi.solver.Dictionary;

/**
 * Adapts {@link Dictionary} into xwords-rs'
 * {@link com.gitlab.super7ramp.croiseur.solver.szunami.Dictionary}.
 */
final class DictionaryAdapter {

    /** Private constructor - static utilities only. */
    private DictionaryAdapter() {
        // Nothing to do.
    }

    /**
     * Adapts {@link Dictionary} into xwords-rs'
     * {@link com.gitlab.super7ramp.croiseur.solver.szunami.Dictionary}.
     *
     * @param dictionary a {@link Dictionary}
     * @return xwords-rs' {@link com.gitlab.super7ramp.croiseur.solver.szunami.Dictionary}
     */
    static com.gitlab.super7ramp.croiseur.solver.szunami.Dictionary adapt(final Dictionary dictionary) {
        return new com.gitlab.super7ramp.croiseur.solver.szunami.Dictionary(dictionary.lookup(word -> true)
                                                                                      .stream()
                                                                                      .toList());
    }
}
