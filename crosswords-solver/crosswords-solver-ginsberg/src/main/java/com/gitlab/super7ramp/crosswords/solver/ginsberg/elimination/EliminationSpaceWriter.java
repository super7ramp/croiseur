/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.solver.ginsberg.elimination;

import com.gitlab.super7ramp.crosswords.solver.ginsberg.core.SlotIdentifier;

import java.util.Collection;

/**
 * Write access to the {@link EliminationSpace}.
 */
public interface EliminationSpaceWriter extends EliminationSpace {

    /**
     * Create a new instance.
     *
     * @return a new instance of {@link EliminationSpaceWriter}
     */
    static EliminationSpaceWriter create() {
        return new EliminationSpaceImpl();
    }

    /**
     * Eliminate the given no-good.
     *
     * @param unassigned the unassigned slot
     * @param reasons    the reasons of the elimination (other relevant slots)
     * @param eliminated the eliminated value
     */
    void eliminate(final SlotIdentifier unassigned, final Collection<SlotIdentifier> reasons,
                   final String eliminated);
}
