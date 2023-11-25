/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.web.model.solver;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * The solver session model: All data relative to the solver service and relevant for the lifetime
 * of a session.
 * <p>
 * Note that solver runs memory is a bit special as it is <em>not </em>managed by the core/service
 * layer (yet): There is no repository of them. For now, it's purely managed here, in memory, per
 * session.
 */
@Component
@SessionScope
public class SolverSessionModel {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(SolverSessionModel.class.getName());

    /** The solver runs. */
    private final Map<String, SolverRun> solverRuns;

    /**
     * Constructs an instance.
     */
    public SolverSessionModel() {
        LOGGER.info("Creating new solver session model");
        solverRuns = new HashMap<>();
    }

    /**
     * The current solver runs.
     *
     * @return the solver runs
     */
    public Iterable<SolverRun> solverRuns() {
        return Collections.unmodifiableCollection(solverRuns.values());
    }

    /**
     * The solver run with given name, if any.
     *
     * @return the solver run with given name, if any; otherwise {@link Optional#empty()}
     */
    public Optional<SolverRun> solverRun(final String name) {
        return Optional.ofNullable(solverRuns.get(name));
    }

    /**
     * Creates and adds a new {@link SolverRun} to this model.
     *
     * @param solverRun the name of the solver run to create
     * @return the created solver run
     */
    public SolverRun newSolverRun(final String solverRun) {
        final var run = SolverRun.created(solverRun);
        solverRuns.put(run.name(), run);
        return run;
    }

    /**
     * Notifies that the solver run with given name has been started.
     *
     * @param solverRun the solver run name
     */
    public void solverRunStarted(final String solverRun) {
        solverRuns.computeIfPresent(solverRun, (name, run) -> run.started());
    }

    /**
     * Notifies that the solver run with given name has progressed.
     *
     * @param solverRun  the solver run name
     * @param percentage the progress percentage
     */
    public SolverRun solverRunProgressed(final String solverRun, final short percentage) {
        return solverRuns.computeIfPresent(solverRun, (name, run) -> run.progressed(percentage));
    }

    /**
     * Notifies that the solver run with given name has terminated.
     *
     * @param solverRun  the solver run name
     */
    public SolverRun solverRunTerminated(final String solverRun) {
        return solverRuns.computeIfPresent(solverRun, (name, run) -> run.terminated());
    }

    /**
     * Clears the solver runs.
     */
    public void clear() {
        solverRuns.clear();
    }
}
