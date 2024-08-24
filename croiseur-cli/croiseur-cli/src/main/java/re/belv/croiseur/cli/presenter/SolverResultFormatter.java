/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.presenter;

import re.belv.croiseur.cli.l10n.ResourceBundles;
import re.belv.croiseur.spi.presenter.solver.SolverResult;

/**
 * Formatter for the solver result.
 */
final class SolverResultFormatter {

    /** Private constructor, static utility methods only. */
    private SolverResultFormatter() {
        // Nothing to do.
    }

    /**
     * Formats the solver result.
     *
     * @return the formatted solver result
     */
    static String formatSolverResult(final SolverResult result) {
        final boolean success = result.isSuccess();
        final String resultHeaderKey = $("result.header");
        final String resultHeaderValue = $(success ? "result.success" : "result.impossible");
        final String formattedPuzzle = PuzzleFormatter.formatPuzzleGrid(result.grid(), result.unsolvableBoxes());
        final String lineSeparator = System.lineSeparator();
        return resultHeaderKey + resultHeaderValue + lineSeparator + lineSeparator + formattedPuzzle + lineSeparator;
    }

    /**
     * Returns the localised message with given key.
     *
     * @param key the message key
     * @return the localised message
     */
    private static String $(final String key) {
        return ResourceBundles.$("presenter.solver." + key);
    }
}
