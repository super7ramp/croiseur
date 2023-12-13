/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.status;

/**
 * The global application status, indicating whether an error has occurred.
 * <p>
 * Status is typically set by presenters and retrieved by controllers to report the exit code to
 * framework. There is no concurrency issue since CLI is mono-threaded.
 */
public final class Status {

    /** The status. */
    private static int status = StatusCodes.OK;

    /** Private constructor to prevent instantiation. */
    private Status() {
        // Nothing to do.
    }

    /**
     * Gets the current status code, then resets it.
     *
     * @return the status code value before reset
     */
    public static int getAndReset() {
        final int resultStatus = status;
        status = StatusCodes.OK;
        return resultStatus;
    }

    /** Sets the current status to "applicative error". */
    public static void setGeneralApplicativeError() {
        status = StatusCodes.APPLICATIVE_ERROR;
    }

    /** Sets the current status to "no solution found". */
    public static void setNoSolutionFound() {
        status = StatusCodes.NO_SOLUTION_FOUND;
    }

}
