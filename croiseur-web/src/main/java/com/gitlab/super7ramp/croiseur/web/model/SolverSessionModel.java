/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * The solver session model: All data relative to the solver service and relevant for the lifetime
 * of a session.
 */
@Component
@SessionScope
public class SolverSessionModel {

    /** The solver runs. */
    private final List<SolverRun> solverRuns;

    /**
     * Constructs an instance.
     */
    public SolverSessionModel() {
        solverRuns = new ArrayList<>();
    }

    /**
     * The current solver runs.
     *
     * @return the solver runs
     */
    public List<SolverRun> solverRuns() {
        return Collections.unmodifiableList(solverRuns);
    }

    /**
     * Updates the list of solver runs with the given run.
     * <p>
     * If a run with same id already exists, it will be replaced by given run; Otherwise, given run
     * will be added at the end of the list.
     *
     * @param run the run to add
     */
    public void addOrUpdate(final SolverRun run) {
        IntStream.range(0, solverRuns.size())
                 .filter(i -> solverRuns.get(i).id() == run.id())
                 .findFirst()
                 .ifPresentOrElse(i -> solverRuns.set(i, run),
                                  () -> solverRuns.add(run));
    }
}
