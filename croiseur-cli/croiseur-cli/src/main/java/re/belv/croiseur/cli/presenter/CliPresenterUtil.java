/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.presenter;

/**
 * Some presentation utility methods.
 */
final class CliPresenterUtil {

    /** Private constructor, static utilities only. */
    private CliPresenterUtil() {
        // Nothing to do.
    }

    /**
     * Creates a line of "-" of given length.
     *
     * @param length the length of the line (i.e. the number of "-")
     * @return a line of "-" of given length
     */
    static String lineOf(final int length) {
        return "-".repeat(length);
    }
}
