/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg;

/** Progress listener. */
public interface ProgressListener {

    /** A no-op listener. */
    ProgressListener DUMMY_LISTENER = new ProgressListener() {
                // No overridden methods
            };

    /** Solver has started its initialization. */
    default void onInitialisationStart() {
        // Do nothing per default
    }

    /** Solver has finished its initialisation. */
    default void onInitialisationEnd() {
        // Do nothing per default
    }

    /**
     * Solver is looking for a solution.
     *
     * <p>This method is called <em>every second</em> with the completion percentage.
     *
     * @param completionPercentage the completion percentage [0..100]; Percentage may decrease as solver is backtracking
     *     after a dead-end is reached
     */
    default void onSolverProgressUpdate(final short completionPercentage) {
        // Do nothing per default
    }
}
