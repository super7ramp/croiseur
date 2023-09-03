/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.impl.solver.prerun;

import re.belv.croiseur.api.solver.SolveRequest;
import re.belv.croiseur.spi.presenter.solver.SolverInitialisationState;
import re.belv.croiseur.spi.presenter.solver.SolverPresenter;
import re.belv.croiseur.spi.presenter.solver.SolverProgress;
import re.belv.croiseur.spi.solver.ProgressListener;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A {@link ProgressListener} factory.
 */
public final class ProgressListenerFactory {

    /**
     * Implementation of a periodical progress listener.
     */
    private static final class ProgressListenerImpl implements ProgressListener {

        /** The name of the solver run. */
        private final String solverRun;

        /** The presenter. */
        private final SolverPresenter presenter;

        /**
         * Constructs an instance.
         *
         * @param solverRunArg the name of the solver run
         * @param presenterArg the presenter
         */
        ProgressListenerImpl(final String solverRunArg, final SolverPresenter presenterArg) {
            solverRun = solverRunArg;
            presenter = presenterArg;
        }

        @Override
        public void onInitialisationStart() {
            presenter.presentSolverInitialisationState(solverRun,
                                                       SolverInitialisationState.STARTED);
        }

        @Override
        public void onInitialisationEnd() {
            presenter.presentSolverInitialisationState(solverRun, SolverInitialisationState.ENDED);
        }

        @Override
        public void onSolverProgressUpdate(final short completionPercentage) {
            presenter.presentSolverProgress(solverRun, new SolverProgress(completionPercentage));
        }

    }

    /** Listeners. */
    private final Map<SolveRequest.SolverProgressNotificationMethod, Function<String, ProgressListener>>
            listeners;

    /**
     * Constructs an instance.
     *
     * @param solverPresenter the solver presenter instance
     */
    public ProgressListenerFactory(final SolverPresenter solverPresenter) {
        listeners =
                new EnumMap<>(SolveRequest.SolverProgressNotificationMethod.class);
        listeners.put(SolveRequest.SolverProgressNotificationMethod.NONE,
                      solverRun -> ProgressListener.DUMMY_LISTENER);
        listeners.put(SolveRequest.SolverProgressNotificationMethod.PERIODICAL,
                      solverRun -> new ProgressListenerImpl(solverRun, solverPresenter));
    }

    /**
     * Creates a {@link ProgressListener} from given notification kind.
     *
     * @param solverRun the solver run name
     * @param kind      the desired notification kind
     * @return a {@link ProgressListener} from given notification kind
     */
    public ProgressListener from(final String solverRun,
                                 final SolveRequest.SolverProgressNotificationMethod kind) {
        return listeners.get(kind).apply(solverRun);
    }

}
