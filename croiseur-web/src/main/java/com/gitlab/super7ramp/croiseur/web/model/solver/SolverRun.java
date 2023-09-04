/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.model.solver;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Represent a crossword solver run.
 */
// TODO define property types: CreatedSolverRun, RunningSolverRun, InterruptedSolverRun, TerminatedSolverRun
public final class SolverRun {

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

    /** The run id. */
    private final String name;

    /** The run creation time. */
    private final LocalDateTime creationTime;

    /** The run end time. May be {@code null}. */
    private final LocalDateTime endTime;

    /** The run status. */
    private final Status status;

    /** The run progress (percentage). */
    private final short progress;

    /**
     * Constructs an instance.
     *
     * @param nameArg the run id
     */
    public SolverRun(final String nameArg) {
        this(nameArg, LocalDateTime.now(), null, Status.CREATED, (short) 0);
    }

    private SolverRun(final String nameArg, final LocalDateTime creationTimeArg,
                      final LocalDateTime endTimeArg, final Status statusArg,
                      final short progressArg) {
        name = nameArg;
        creationTime = creationTimeArg;
        endTime = endTimeArg;
        status = statusArg;
        progress = progressArg;
    }

    /**
     * The run id.
     *
     * @return the run id
     */
    public String name() {
        return name;
    }

    /**
     * The run creation time.
     *
     * @return the run creation time
     */
    public LocalDateTime creationTime() {
        return creationTime;
    }

    /**
     * The run end time, if run status is terminated or interrupted.
     *
     * @return the run end time, if any
     */
    public Optional<LocalDateTime> endTime() {
        return Optional.ofNullable(endTime);
    }

    /**
     * The run status.
     *
     * @return the job status
     */
    public Status status() {
        return status;
    }

    public short progress() {
        return progress;
    }

    public SolverRun progress(final short newProgressValue) {
        return new SolverRun(name, creationTime, endTime, status, newProgressValue);
    }

    public SolverRun started() {
        return new SolverRun(name, creationTime, endTime, Status.RUNNING, progress);
    }

    public SolverRun terminated() {
        return new SolverRun(name, creationTime, LocalDateTime.now(), Status.TERMINATED,
                             (short) 100);
    }
}
