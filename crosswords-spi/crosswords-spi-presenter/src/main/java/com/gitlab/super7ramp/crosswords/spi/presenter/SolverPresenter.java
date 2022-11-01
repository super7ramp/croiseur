package com.gitlab.super7ramp.crosswords.spi.presenter;

import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;

/**
 * Solver-related presentation services.
 */
public interface SolverPresenter {

    /**
     * Presents the solver initialisation state.
     *
     * @param solverInitialisationState the solver initialisation state
     */
    void presentSolverInitialisationState(final SolverInitialisationState solverInitialisationState);

    /**
     * Presents the solving progress.
     *
     * @param completionPercentage the completion percentage of the solving
     */
    // TODO add intermediate solver result
    void presentProgress(final short completionPercentage);

    /**
     * Presents the result of a crossword solving request.
     *
     * @param result the solver result
     */
    // TODO create specific solver result type so that Presenter SPI does not depend on Solver SPI?
    void presentResult(final SolverResult result);

    /**
     * Presents an error from the crossword services.
     *
     * @param error the error
     */
    void presentError(final String error);

}
