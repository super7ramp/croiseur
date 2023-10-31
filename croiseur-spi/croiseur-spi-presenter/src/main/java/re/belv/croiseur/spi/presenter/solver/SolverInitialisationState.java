/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.spi.presenter.solver;

/** The solver initialisation state. */
public enum SolverInitialisationState {
    /** Solver has started its initialisation. */
    STARTED,
    /** Solver has ended its initialisation. */
    ENDED
}
