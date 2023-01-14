/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.solver.ginsberg.elimination;

import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.SlotIdentifier;

import java.util.Set;

/**
 * The eliminated candidates.
 */
public interface EliminationSpace {

    /**
     * Return the eliminated values for given slot.
     *
     * @param slot the slot for which return the eliminated values
     * @return the eliminated values for given slot
     */
    Set<String> eliminations(final SlotIdentifier slot);
}
