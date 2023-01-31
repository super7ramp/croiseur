/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;

import java.util.Collection;

/**
 * Provides lookahead capabilities.
 */
public interface Probable {

    /**
     * Returns a {@link Collection} of {@link Slot}s as if given {@link Assignment} were applied.
     *
     * @param assignment the assignment to probe
     * @return Returns a {@link Collection} of {@link Slot} as if given {@link Assignment} were
     * applied.
     */
    Collection<Slot> probe(final Assignment assignment);

    /**
     * Returns a {@link Collection} of {@link Slot}s as if given {@link Unassignment} were applied.
     *
     * @param unassignment the unassignment to probe
     * @return Returns a {@link Collection} of {@link Slot} as if given {@link Unassignment} were
     * applied.
     */
    Collection<Slot> probe(final Unassignment unassignment);
}
