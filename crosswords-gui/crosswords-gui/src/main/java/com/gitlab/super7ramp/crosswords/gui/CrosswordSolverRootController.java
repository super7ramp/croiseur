package com.gitlab.super7ramp.crosswords.gui;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.gui.controller.dictionary.DictionaryController;
import com.gitlab.super7ramp.crosswords.gui.controller.solver.SolverController;
import com.gitlab.super7ramp.crosswords.gui.view.CrosswordSolver;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordSolverViewModel;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;
import javafx.beans.binding.When;
import javafx.fxml.FXML;

import java.util.concurrent.Executor;

/**
 * The main controller.
 */
public final class CrosswordSolverRootController {

    /** The controller dedicated to the solver use-cases. */
    private final SolverController solverController;

    /** The controller dedicated to the dictionary use-cases. */
    private final DictionaryController dictionaryController;

    /** The view model. */
    private final CrosswordSolverViewModel crosswordSolverViewModel;

    /** The view. */
    @FXML
    private CrosswordSolver view;

    /**
     * Constructs an instance.
     *
     * @param crosswordService            the use-cases
     * @param crosswordSolverViewModelArg the view model
     * @param executorArg                 the executor allowing to run background tasks
     */
    public CrosswordSolverRootController(final CrosswordService crosswordService,
                                         final CrosswordSolverViewModel crosswordSolverViewModelArg,
                                         final Executor executorArg) {
        solverController = new SolverController(crosswordSolverViewModelArg,
                crosswordService.solverService());
        dictionaryController = new DictionaryController(crosswordService.dictionaryService(),
                executorArg);
        crosswordSolverViewModel = crosswordSolverViewModelArg;
    }

    @FXML
    private void initialize() {
        // The grid view shall display and allow edition of the crossword grid model
        final CrosswordGridViewModel crosswordGridViewModel =
                crosswordSolverViewModel.crosswordGridViewModel();
        view.gridBoxesProperty().bindBidirectional(crosswordGridViewModel.boxes());

        // The dictionary pane shall display the dictionary model
        final DictionaryViewModel dictionaryViewModel =
                crosswordSolverViewModel.dictionaryViewModel();
        view.setDictionaries(dictionaryViewModel.dictionariesProperty());
        view.setDictionaryEntries(dictionaryViewModel.dictionaryEntries());

        // Grid edition buttons and grid pane shall be disabled when solver is running
        view.gridEditionDisableProperty().bind(crosswordSolverViewModel.solverRunning());

        // Solver button shall be disabled if no solver not running and no dictionary selected
        view.solveButtonDisableProperty()
            .bind(dictionaryViewModel.selectedDictionariesProperty().emptyProperty());

        // Solver button text shall be consistent with the solver state
        view.solveButtonTextProperty()
            .bind(new When(crosswordSolverViewModel.solverRunning()).then("Stop solving")
                                                                    .otherwise("Solve"));

        // Solver button action shall be consistent with the solver state
        view.onSolveButtonActionProperty().set(event -> {
            if (!crosswordSolverViewModel.solverRunning().get()) {
                solverController.start();
            } else {
                solverController.stop();
            }
        });

        // Dictionary pane shall be populated on startup
        // TODO To be performed lazily when dictionary pane is displayed for the first time
        dictionaryController.listDictionaries();
        dictionaryController.listDictionaryEntries();
    }

}
