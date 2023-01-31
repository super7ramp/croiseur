/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.core.sap;

import java.util.Iterator;

/**
 * Encapsulates the heuristics for choosing the next variable to instantiate.
 *
 * @param <VariableT> the variable type
 */
public interface VariableIterator<VariableT> extends Iterator<VariableT> {
    // Marker interface.
}
