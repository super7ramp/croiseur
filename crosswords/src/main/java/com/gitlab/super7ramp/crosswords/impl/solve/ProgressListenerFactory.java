package com.gitlab.super7ramp.crosswords.impl.solve;

import com.gitlab.super7ramp.crosswords.api.solver.SolveRequest;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverInitialisationState;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverPresenter;
import com.gitlab.super7ramp.crosswords.spi.presenter.solver.SolverProgress;
import com.gitlab.super7ramp.crosswords.spi.solver.ProgressListener;

import java.util.EnumMap;
import java.util.Map;

/**
 * A {@link ProgressListener} factory.
 */
final class ProgressListenerFactory {

    /**
     * Implementation of a periodical progress listener.
     */
    private static class ProgressListenerImpl implements ProgressListener {

        /** The presenter. */
        private final SolverPresenter presenter;

        /**
         * Constructs an instance.
         *
         * @param presenterArg the presenter
         */
        ProgressListenerImpl(final SolverPresenter presenterArg) {
            presenter = presenterArg;
        }

        @Override
        public void onInitialisationStart() {
            presenter.presentSolverInitialisationState(SolverInitialisationState.STARTED);
        }

        @Override
        public void onInitialisationEnd() {
            presenter.presentSolverInitialisationState(SolverInitialisationState.ENDED);
        }

        @Override
        public void onSolverProgressUpdate(final short completionPercentage) {
            presenter.presentProgress(new SolverProgress(completionPercentage));
        }

    }

    /** Listeners. */
    private final Map<SolveRequest.SolverProgressNotificationMethod, ProgressListener> listeners;

    /**
     * Constructs an instance.
     *
     * @param solverPresenterArg the solver presenter instance
     */
    ProgressListenerFactory(final SolverPresenter solverPresenterArg) {
        listeners =
                new EnumMap<>(SolveRequest.SolverProgressNotificationMethod.class);
        listeners.put(SolveRequest.SolverProgressNotificationMethod.NONE,
                ProgressListener.DUMMY_LISTENER);
        listeners.put(SolveRequest.SolverProgressNotificationMethod.PERIODICAL,
                new ProgressListenerImpl(solverPresenterArg));
    }

    /**
     * Creates a {@link ProgressListener} from given notification kind.
     *
     * @param kind the desired notification kind
     * @return a {@link ProgressListener} from given notification kind
     */
    ProgressListener from(final SolveRequest.SolverProgressNotificationMethod kind) {
        return listeners.get(kind);
    }


}
