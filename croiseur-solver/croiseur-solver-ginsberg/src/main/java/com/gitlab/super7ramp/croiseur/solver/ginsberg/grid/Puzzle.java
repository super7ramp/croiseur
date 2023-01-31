/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.grid;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;
import com.gitlab.super7ramp.croiseur.solver.ginsberg.lookahead.Probable;

import java.util.Collection;

/**
 * Read/write access to crossword puzzle model.
 */
public interface Puzzle extends Probable {

    /**
     * The slots.
     *
     * @return the slots
     */
    Collection<Slot> slots();
}
