package com.gitlab.super7ramp.crosswords.spi.presenter;

import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

/**
 * Solver-related presentation services.
 */
public interface SolverPresenter {

    /** The solver initialisation state. */
    enum SolverInitialisationState {
        /** Solver has started its initialisation. */
        STARTED,
        /** Solver has ended its initialisation. */
        ENDED
    }

    /**
     * Presents the solver initialisation state.
     *
     * @param solverInitialisationState the solver initialisation state
     */
    void publishSolverInitialisationState(final SolverInitialisationState solverInitialisationState);

    /**
     * Presents the solving progress.
     *
     * @param completionPercentage the completion percentage of the solving
     */
    // TODO add intermediate solver result
    void publishProgress(final short completionPercentage);

    /**
     * Presents the result of a crossword solving request.
     *
     * @param result the solver result
     */
    // TODO create specific solver result type so that Presenter SPI does not depend on Solver SPI?
    void publishResult(final SolverResult result);

    /**
     * Presents an error from the crossword services.
     *
     * @param error the error
     */
    void publishError(final String error);


}
