package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.gui.controls.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.controls.model.IntCoordinate2D;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordGridViewModel;
import com.gitlab.super7ramp.crosswords.spi.presenter.SolverInitialisationState;
import com.gitlab.super7ramp.crosswords.spi.presenter.SolverPresenter;
import com.gitlab.super7ramp.crosswords.spi.solver.GridPosition;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;
import javafx.beans.property.MapProperty;

import java.util.Map;
import java.util.logging.Logger;

/**
 * GUI implementation of {@link SolverPresenter}.
 */
final class GuiSolverPresenter implements SolverPresenter {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(GuiSolverPresenter.class.getName());

    /** The grid model. */
    private final CrosswordGridViewModel crosswordGridViewModel;

    /**
     * Constructs an instance.
     */
    GuiSolverPresenter(final CrosswordGridViewModel crosswordGridViewModelArg) {
        crosswordGridViewModel = crosswordGridViewModelArg;
    }

    /**
     * Transforms a {@link GridPosition} to a {@link IntCoordinate2D}.
     *
     * @param domain the {@link GridPosition}
     * @return the {@link IntCoordinate2D}
     */
    private static IntCoordinate2D domainToView(final GridPosition domain) {
        return new IntCoordinate2D(domain.x(), domain.y());
    }

    @Override
    public void presentSolverInitialisationState(SolverInitialisationState solverInitialisationState) {
        // TODO really implement
        LOGGER.info(() -> "Solver initialisation: " + solverInitialisationState);
    }

    @Override
    public void presentProgress(short completionPercentage) {
        // TODO really implement
        LOGGER.info(() -> "Completion: " + completionPercentage + " %");
    }

    @Override
    public void presentResult(final SolverResult result) {
        LOGGER.info(() -> "Received result: " + result);
        final Map<GridPosition, Character> resultBoxes = result.boxes();
        final MapProperty<IntCoordinate2D, CrosswordBox> viewModelBoxes =
                crosswordGridViewModel.boxes();
        for (final Map.Entry<GridPosition, Character> entry : resultBoxes.entrySet()) {
            final IntCoordinate2D key = domainToView(entry.getKey());
            final CrosswordBox value = viewModelBoxes.computeIfAbsent(key, k -> new CrosswordBox());
            value.contentProperty().set(entry.getValue().toString());
        }
    }

    @Override
    public void presentError(final String error) {
        // TODO really implement
        LOGGER.warning(error);
    }
}
