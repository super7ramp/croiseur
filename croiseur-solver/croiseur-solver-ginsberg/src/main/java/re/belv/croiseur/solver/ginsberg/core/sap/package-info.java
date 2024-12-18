/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 *
 *
 * <h2>Satisfaction problem solving</h2>
 *
 * <p>This package implements a satisfaction problem solver. The resolution of the problem is split into three
 * sub-problems:
 *
 * <ul>
 *   <li>Variable choice
 *   <li>Variable instantiation
 *   <li>Backtracking
 * </ul>
 *
 * <h3>Variable choice</h3>
 *
 * <p>This consists in choosing the next variable to instantiate. It is handled by an {@link java.util.Iterator}.
 *
 * <h3>Variable instantiation</h3>
 *
 * <p>When a variable is chosen, it must be instantiated to a valid value. The choice of this value is handled by a
 * {@link re.belv.croiseur.solver.ginsberg.core.sap.CandidateChooser}.
 *
 * <h3>Backtracking</h3>
 *
 * <p>Backtracking is the strategy to apply when a dead-end is reached, i.e. when the chosen variable cannot be
 * instantiated. In order to continue the search of a valid solution, one or several already instantiated variables must
 * be changed. This heuristic is handled by a {@link re.belv.croiseur.solver.ginsberg.core.sap.Backtracker}.
 */
package re.belv.croiseur.solver.ginsberg.core.sap;
