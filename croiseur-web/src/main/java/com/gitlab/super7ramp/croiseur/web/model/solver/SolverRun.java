/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.model.solver;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represent a crossword solver run.
 *
 * @param name         the name
 * @param status       the status
 * @param progress     the progress (a percentage)
 * @param creationTime the creation time
 * @param endTime      the end time (may be {@code null})
 */
public record SolverRun(String name, Status status, short progress, LocalDateTime creationTime,
                        LocalDateTime endTime) {

    /** The run status. */
    public enum Status {
        /** The job has been created but has not actually started yet. */
        CREATED,
        /** The job is running. */
        RUNNING,
        /** The job has been interrupted. */
        INTERRUPTED,
        /** The job has terminated without interruption. */
        TERMINATED
    }

    /**
     * Validates fields.
     *
     * @param name         the name
     * @param status       the status
     * @param progress     the progress (a percentage)
     * @param creationTime the creation time
     * @param endTime      the end time (may be {@code null})
     */
    public SolverRun {
        Objects.requireNonNull(name);
        Objects.requireNonNull(status);
        Objects.requireNonNull(creationTime);
        if (status == Status.INTERRUPTED || status == Status.TERMINATED) {
            Objects.requireNonNull(endTime);
        }
    }

    /**
     * Creates a new {@link SolverRun} in {@link Status#CREATED} state.
     *
     * @param nameArg the run name
     * @return a new {@link SolverRun} in {@link Status#CREATED} state
     */
    static SolverRun created(final String nameArg) {
        return new SolverRun(nameArg, Status.CREATED, (short) 0, LocalDateTime.now(), null);
    }

    /**
     * Updates state to {@link Status#RUNNING} state.
     *
     * @return a new {@link SolverRun} in {@link Status#RUNNING} state
     * @throws IllegalStateException if this object is not in {@link Status#CREATED} state
     */
    public SolverRun started() {
        if (status != Status.CREATED) {
            throw new IllegalStateException(
                    "Illegal attempt to start an already running or stopped solver run");
        }
        return new SolverRun(name, Status.RUNNING, progress, creationTime, endTime);
    }

    /**
     * Updates progress.
     *
     * @param newProgressValue the new progress value
     * @return a new {@link SolverRun} in {@link Status#RUNNING} state, with an updated
     * {@link #progress}
     * @throws IllegalStateException if this object is not in {@link Status#RUNNING} state
     */
    public SolverRun progressed(final short newProgressValue) {
        if (status != Status.RUNNING) {
            throw new IllegalStateException(
                    "Illegal attempt to set progress on a non-running solver run");
        }
        return new SolverRun(name, status, newProgressValue, creationTime, endTime);
    }

    /**
     * Updates state to {@link Status#TERMINATED}.
     *
     * @return a new {@link SolverRun} in {@link Status#TERMINATED} state
     */
    public SolverRun terminated() {
        return new SolverRun(name, Status.TERMINATED, (short) 100, creationTime,
                             LocalDateTime.now());
    }
}
