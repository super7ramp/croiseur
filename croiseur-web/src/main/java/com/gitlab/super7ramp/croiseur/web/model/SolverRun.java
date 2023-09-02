/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.model;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Represent a crossword solver run.
 */
// TODO impact service layer
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
    private final int id;

    /** The run creation time. */
    private final LocalDateTime creationTime;

    /** The run end time. May be {@code null}. */
    private LocalDateTime endTime;

    /** The run status. */
    private Status status;

    /**
     * Constructs an instance.
     *
     * @param idArg the run id
     */
    public SolverRun(final int idArg) {
        id = idArg;
        creationTime = LocalDateTime.now();
        status = Status.CREATED;
        // endTime intentionally kept null
    }

    /**
     * The run id.
     *
     * @return the run id
     */
    public int id() {
        return id;
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

}
