/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg;

import java.util.Set;
import java.util.function.Predicate;

/**
 * Dictionary interface (to be provided).
 */
public interface Dictionary {

    /**
     * Search for words matching the given {@link Predicate}.
     *
     * @param predicate the predicate to satisfy
     * @return a set of words matching the given pattern
     */
    Set<String> lookup(final Predicate<String> predicate);
}
