/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.status;

/** The status codes. */
public final class StatusCodes {

    /** Default status, no error. */
    public static final int OK = 0;

    /** General applicative error. */
    public static final int APPLICATIVE_ERROR = 1;

    /** Usage error (e.g. mistyped command). */
    public static final int USAGE_ERROR = 2;

    /** An I/O error occurred. */
    public static final int IO_ERROR = 3;

    /** The solver did not find a solution to a grid. */
    public static final int NO_SOLUTION_FOUND = 4;

    /** Private constructor to prevent instantiation. */
    private StatusCodes() {
        // Nothing to do.
    }
}
