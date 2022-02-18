/**
 * <h2>Satisfaction problem solving</h2>
 * <p>
 * This package implements a satisfaction problem solver. The resolution of the problem is split
 * into three sub-problems:
 * </p>
 * <ul>
 *     <li>Variable choice</li>
 *     <li>Variable instantiation</li>
 *     <li>Backtracking</li>
 * </ul>
 * <h3>Variable choice</h3>
 * <p>
 * This consists in choosing the next variable to instantiate. It is handled by an
 * {@link java.util.Iterator}.
 * </p>
 * <h3>Variable instantiation</h3>
 * <p>
 * When a variable is chosen, it must be instantiated to a valid value. The choice of this value
 * is handled by a
 * {@link com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap.CandidateChooser}.
 * </p>
 * <h3>Backtracking</h3>
 * <p>
 * Backtracking is the strategy to apply when a dead-end is reached, i.e. when the chosen
 * variable cannot be instantiated. In order to continue the search of a valid solution, one or
 * several already instantiated variables must be changed. This heuristic is handled by a
 * {@link com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap.Backtracker}.
 * </p>
 */
package com.gitlab.super7ramp.crosswords.solver.ginsberg.core.sap;