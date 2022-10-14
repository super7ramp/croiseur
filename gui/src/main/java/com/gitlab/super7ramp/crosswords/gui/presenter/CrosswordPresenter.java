package com.gitlab.super7ramp.crosswords.gui.presenter;

import com.gitlab.super7ramp.crosswords.gui.fx.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.fx.model.IntCoordinate2D;
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
    private static CrosswordViewModel viewModel;

    /**
     * Constructs an instance.
     */
    CrosswordPresenter() {
        // nothing to do
    }

    public static void inject(final CrosswordViewModel modelArg) {
        viewModel = modelArg;
    }

    private static IntCoordinate2D domainToView(final GridPosition domain) {
        return new IntCoordinate2D(domain.x(), domain.y());
    }

    public void presentSolverResult(final SolverResult result) {
        LOGGER.info(() -> "Received result: " + result);
        final Map<GridPosition, Character> resultBoxes = result.boxes();
        final MapProperty<IntCoordinate2D, CrosswordBox> viewModelBoxes = viewModel.boxes();
        for (final Map.Entry<GridPosition, Character> entry : resultBoxes.entrySet()) {
            final IntCoordinate2D key = domainToView(entry.getKey());
            final CrosswordBox value = viewModelBoxes.computeIfAbsent(key, k -> new CrosswordBox());
            value.contentProperty().set(entry.getValue().toString());
        }
    }


}
