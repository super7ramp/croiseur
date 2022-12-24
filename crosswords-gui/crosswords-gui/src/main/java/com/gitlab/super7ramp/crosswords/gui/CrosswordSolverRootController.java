package com.gitlab.super7ramp.crosswords.gui;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.common.GridPosition;
import com.gitlab.super7ramp.crosswords.gui.controller.dictionary.DictionaryController;
import com.gitlab.super7ramp.crosswords.gui.controller.solver.SolverController;
import com.gitlab.super7ramp.crosswords.gui.view.CrosswordSolverPane;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordSolverViewModel;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionariesViewModel;
import com.gitlab.super7ramp.crosswords.gui.view.model.DictionaryViewModel;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;

import java.util.ResourceBundle;
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
    private CrosswordSolverPane view;

    /** The translations. */
    @FXML
    private ResourceBundle resources;

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
        final MapProperty<GridPosition, CrosswordBoxViewModel> boxes =
                crosswordGridViewModel.boxes();
        view.gridBoxesProperty().setValue(boxes);

        // The dictionary pane shall display the dictionary model and allow to modify the
        // dictionary selection.
        final DictionariesViewModel dictionariesViewModel =
                crosswordSolverViewModel.dictionaryViewModel();
        view.dictionariesProperty().setValue(dictionariesViewModel.dictionariesProperty());
        view.wordsProperty().setValue(dictionariesViewModel.wordsProperty());
        final ListProperty<DictionaryViewModel> selectedDictionary =
                dictionariesViewModel.selectedDictionariesProperty();
        selectedDictionary.addListener(this::onSelectedDictionaryChange);

        // Grid edition buttons and grid pane shall be disabled when solver is running
        final BooleanProperty solverRunning = crosswordSolverViewModel.solverRunning();
        view.gridEditionDisableProperty().bind(solverRunning);

        // Solver button shall be disabled if solver is not running and no dictionary is selected
        // and grid is not empty
        view.solveButtonDisableProperty()
            .bind(solverRunning.not()
                               .and(selectedDictionary.emptyProperty().or(boxes.emptyProperty())));

        // Solver button text shall be consistent with the solver state
        view.solveButtonTextProperty()
            .bind(new When(solverRunning)
                    .then(resources.getString("stop-solving-button"))
                    .otherwise(resources.getString("start-solving-button")));

        // Solver button action shall allow control the start and stop of the solver
        view.onSolveButtonActionProperty().set(event -> onSolveButtonAction());

        // Dictionary pane shall be populated on startup
        dictionaryController.listDictionaries();
    }

    /**
     * Performs the solve button action.
     */
    private void onSolveButtonAction() {
        if (!crosswordSolverViewModel.solverRunning().get()) {
            solverController.start();
        } else {
            solverController.stop();
        }
    }

    /**
     * Performs the dictionary selection change action.
     *
     * @param change the dictionary selection change
     */
    private void onSelectedDictionaryChange(ListChangeListener.Change<?
            extends DictionaryViewModel> change) {
        // TODO Dictionary words retrieval should be performed lazily when dictionary pane is
        //  displayed for the first time
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(dictionaryController::listDictionaryEntries);
            }
        }
    }
}
