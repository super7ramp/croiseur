package com.gitlab.super7ramp.crosswords.gui.controller;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.gui.control.CrosswordGrid;
import com.gitlab.super7ramp.crosswords.gui.control.CrosswordGridEditionToolbar;
import com.gitlab.super7ramp.crosswords.gui.control.DictionaryPane;
import com.gitlab.super7ramp.crosswords.gui.controller.dictionary.DictionaryController;
import com.gitlab.super7ramp.crosswords.gui.controller.solver.SolverController;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordGridViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordSolverViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.DictionaryViewModel;
import javafx.beans.binding.When;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.concurrent.Executor;

/**
 * The main controller.
 */
public final class CrosswordSolverController {

    /** The controller dedicated to the solver use-cases. */
    private final SolverController solverController;

    /** The controller dedicated to the dictionary use-cases. */
    private final DictionaryController dictionaryController;

    /** The view model. */
    private CrosswordSolverViewModel crosswordSolverViewModel;

    @FXML
    private CrosswordGridEditionToolbar gridEditionToolbar;

    @FXML
    private CrosswordGrid grid;

    @FXML
    private Button solveButton;

    @FXML
    private DictionaryPane dictionaryPane;

    /**
     * Constructs an instance.
     *
     * @param crosswordService            the use-cases
     * @param crosswordSolverViewModelArg the view model
     * @param executorArg                 the executor allowing to run background tasks
     */
    public CrosswordSolverController(final CrosswordService crosswordService,
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
        // Bind the crossword view model to the crossword view
        final CrosswordGridViewModel crosswordGridViewModel =
                crosswordSolverViewModel.crosswordGridViewModel();
        grid.boxes().bindBidirectional(crosswordGridViewModel.boxes());

        // Bind the dictionary view model to the dictionary view
        final DictionaryViewModel dictionaryViewModel =
                crosswordSolverViewModel.dictionaryViewModel();
        dictionaryPane.setDictionaries(dictionaryViewModel.dictionariesProperty());
        dictionaryPane.setDictionaryEntries(dictionaryViewModel.dictionaryEntries());

        // Bind the grid editor buttons to the grid
        gridEditionToolbar.onAddColumnActionProperty().set(event -> grid.addColumn());
        gridEditionToolbar.onAddRowActionProperty().set(event -> grid.addRow());
        gridEditionToolbar.onDeleteColumnActionProperty().set(event -> grid.deleteLastColumn());
        gridEditionToolbar.onDeleteRowActionProperty().set(event -> grid.deleteLastRow());

        // Controls should be disabled when solver is running - except the stop button
        grid.disableProperty().bind(crosswordSolverViewModel.solverRunning());
        gridEditionToolbar.disableProperty().bind(crosswordSolverViewModel.solverRunning());
        dictionaryPane.disableProperty().bind(crosswordSolverViewModel.solverRunning());
        solveButton.textProperty()
                   .bind(new When(crosswordSolverViewModel.solverRunning()).then("Stop solving")
                                                                           .otherwise("Solve"));

        // Solve button should be disabled if no dictionary is selected
        solveButton.disableProperty()
                   .bind(dictionaryViewModel.selectedDictionariesProperty().emptyProperty());

        // Populate dictionary pane
        // TODO To be performed lazily when dictionary pane is displayed for the first time
        dictionaryController.listDictionaries();
        dictionaryController.listDictionaryEntries();
    }

    @FXML
    private void onSolveButtonClicked() {
        if (!crosswordSolverViewModel.solverRunning().get()) {
            solverController.start();
        } else {
            solverController.stop();
        }
    }
}
