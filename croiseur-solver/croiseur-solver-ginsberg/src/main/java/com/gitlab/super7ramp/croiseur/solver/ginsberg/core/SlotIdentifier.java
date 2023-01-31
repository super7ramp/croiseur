/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.core;

import java.util.Objects;

/**
 * Unique identifier for a variable inside a given problem.
 */
public final class SlotIdentifier {

    /** Just an integer. */
    private final int id;

    /**
     * Constructor.
     *
     * @param anId the identifier
     */
    public SlotIdentifier(final int anId) {
        id = anId;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SlotIdentifier otherSlotIdentifier)) {
            return false;
        }
        return id == otherSlotIdentifier.id;
    }

    @Override
    public String toString() {
        return "SlotIdentifier{" + "id=" + id + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    /**
     * The identifier as an integer.
     *
     * @return the identifier as an integer
     */
    public int id() {
        return id;
    }
}
