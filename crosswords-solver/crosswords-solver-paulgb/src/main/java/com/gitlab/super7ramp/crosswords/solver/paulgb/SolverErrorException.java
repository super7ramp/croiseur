package com.gitlab.super7ramp.crosswords.solver.paulgb;

/**
 * Exception denoting the Crossword Composer solver encountered an error.
 */
public final class SolverErrorException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public SolverErrorException(final String message) {
        super(message);
    }

}
