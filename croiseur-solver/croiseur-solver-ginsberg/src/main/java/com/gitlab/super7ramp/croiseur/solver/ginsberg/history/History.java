/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.history;

import com.gitlab.super7ramp.croiseur.solver.ginsberg.core.Slot;

import java.util.Iterator;
import java.util.Optional;

/**
 * Assignment history. Useful for backtracking.
 */
public interface History {

    /**
     * Gets the last assigned slot from history.
     *
     * @return the last assigned slot in history if any; otherwise, returns {@link Optional#empty()}
     */
    Optional<Slot> lastAssignedSlot();

    /**
     * Gets the last assigned slot from history, connected to given variable.
     *
     * @param toBeConnectedWith the slot that the returned last assigned slot must be connected to
     * @return the last assigned slot connected to given slot, if any; otherwise, returns
     * {@link Optional#empty()}
     */
    Optional<Slot> lastAssignedConnectedSlot(final Slot toBeConnectedWith);

    /**
     * Gets an iterator on the history.
     * <p>
     * Element removal is not supported - this class is for read-only access. Behaviour on element
     * removal is not fully determined yet, at best it will be no effect, otherwise it will throw a
     * runtime exception.
     *
     * @return the iterator on history
     */
    Iterator<Slot> explorer();
}
