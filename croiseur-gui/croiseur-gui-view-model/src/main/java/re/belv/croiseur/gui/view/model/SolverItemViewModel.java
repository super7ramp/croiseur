/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

/**
 * View model for a solver item.
 *
 * @param name        the name of the solver
 * @param description a short description
 */
public record SolverItemViewModel(String name, String description) {
    // Nothing to add
}
