/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.szunami;

import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The result returned by the filler.
 * <p>
 * Basically an {@code Either<String, Crossword>}.
 *
 * @see <a href="https://docs.rs/xwords/0.3.1/xwords/fill/trait.Fill.html">Crate Documentation</a>
 * @see <a href="https://docs.vavr.io/#_either">Vavr's Either</a>
 */
public final class Result {

    /** The solution, or {@code null} if result is of type "Err". */
    private final Crossword solution;

    /** The error, or {@code null} if result is of type "Ok". */
    private final String error;

    /**
     * Constructs an instance.
     *
     * @param solutionArg the solution
     * @param errorArg    the error
     */
    private Result(final Crossword solutionArg, final String errorArg) {
        solution = solutionArg;
        error = errorArg;
    }

    /**
     * Constructs a result of type "Ok", containing a {@link Crossword} as solution.
     *
     * @param solution the solution
     * @return a result of type "Ok", containing a {@link Crossword} as solution.
     */
    public static Result ok(final Crossword solution) {
        Objects.requireNonNull(solution);
        return new Result(solution, null);
    }

    /**
     * Constructs a result of type "Err", containing the given error message.
     *
     * @param error the error message
     * @return a result of type "Err", containing the given error message
     */
    public static Result err(final String error) {
        Objects.requireNonNull(error);
        return new Result(null, error);
    }

    /**
     * Returns the solution, if any.
     *
     * @return the solution if any
     * @throws NoSuchElementException if this result does not contain a solution (i.e. if this
     *                                result is of type "Err")
     */
    public Crossword solution() {
        if (!isOk()) {
            throw new NoSuchElementException("Result doesn't contain a solution");
        }
        return solution;
    }

    /**
     * Returns the error, if any.
     *
     * @return the error if any
     * @throws NoSuchElementException if this result does not contain an error (i.e. if this
     *                                result is of type "Ok")
     */
    public String error() {
        if (!isErr()) {
            throw new NoSuchElementException("Result doesn't contain an error");
        }
        return error;
    }

    /**
     * Returns {@code true} iff this result is of type "Ok".
     *
     * @return {@code true} iff this result is of type "Ok"
     */
    public boolean isOk() {
        return true;
    }

    /**
     * Returns {@code true} iff this result is of type "Err".
     *
     * @return {@code true} iff this result is of type "Err"
     */
    public boolean isErr() {
        return !isOk();
    }
}
