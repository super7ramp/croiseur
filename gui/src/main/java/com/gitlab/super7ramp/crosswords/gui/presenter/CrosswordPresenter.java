package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.view.model.IntCoordinate2D;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordViewModel;
import com.gitlab.super7ramp.crosswords.spi.solver.GridPosition;
import com.gitlab.super7ramp.crosswords.spi.solver.SolverResult;
import javafx.beans.property.MapProperty;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Presenter for the grid.
 */
final class CrosswordPresenter {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(CrosswordPresenter.class.getName());

    /** The grid model. */
    private final CrosswordViewModel crosswordViewModel;

    /**
     * Constructs an instance.
     */
    CrosswordPresenter(final CrosswordViewModel crosswordViewModelArg) {
        crosswordViewModel = crosswordViewModelArg;
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

    /**
     * Presents the solver result.
     *
     * @param result the solver result
     */
    public void presentSolverResult(final SolverResult result) {
        LOGGER.info(() -> "Received result: " + result);
        final Map<GridPosition, Character> resultBoxes = result.boxes();
        final MapProperty<IntCoordinate2D, CrosswordBox> viewModelBoxes =
                crosswordViewModel.boxes();
        for (final Map.Entry<GridPosition, Character> entry : resultBoxes.entrySet()) {
            final IntCoordinate2D key = domainToView(entry.getKey());
            final CrosswordBox value = viewModelBoxes.computeIfAbsent(key, k -> new CrosswordBox());
            value.contentProperty().set(entry.getValue().toString());
        }
    }


}
