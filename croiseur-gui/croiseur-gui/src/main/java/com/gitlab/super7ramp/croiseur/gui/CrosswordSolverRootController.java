/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.gui.controller.dictionary.DictionaryController;
import com.gitlab.super7ramp.croiseur.gui.controller.solver.SolverController;
import com.gitlab.super7ramp.croiseur.gui.view.CrosswordSolverPane;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordSolverViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionariesViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionaryViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.ErrorsViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverProgressViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverSelectionViewModel;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

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

    /** The special error alert view. */
    private final Alert errorAlert;

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
     * @param executor                    the executor allowing to run background tasks
     */
    public CrosswordSolverRootController(final CrosswordService crosswordService,
                                         final CrosswordSolverViewModel crosswordSolverViewModelArg,
                                         final Executor executor) {
        solverController =
                new SolverController(crosswordSolverViewModelArg, crosswordService.solverService(),
                                     executor);
        dictionaryController =
                new DictionaryController(crosswordService.dictionaryService(), executor);
        crosswordSolverViewModel = crosswordSolverViewModelArg;
        errorAlert = new Alert(Alert.AlertType.ERROR);
    }

    @FXML
    private void initialize() {
        initializeCrosswordGridBindings();
        initializeDictionaryBindings();
        initializeSolverSelectionBindings();
        initializeSolverProgressBindings();
        initializeOtherSolverBindings();
        initializeErrorsBindings();
        populateServiceLists();
    }

    /**
     * Initializes bindings between the grid view and the crossword grid view model.
     */
    private void initializeCrosswordGridBindings() {
        final CrosswordGridViewModel viewModel = crosswordSolverViewModel.crosswordGridViewModel();
        view.gridBoxesProperty().set(viewModel.boxesProperty());
        view.gridCurrentBoxProperty().bindBidirectional(viewModel.currentBoxPositionProperty());
        view.gridIsCurrentSlotOrientationVerticalProperty()
            .bindBidirectional(viewModel.isCurrentSlotVerticalProperty());
        view.onAddRowActionButtonProperty().set(event -> viewModel.addRow());
        view.onAddColumnActionButtonProperty().set(event -> viewModel.addColumn());
        view.onDeleteColumnActionButtonProperty().set(event -> viewModel.deleteLastColumn());
        view.onDeleteRowActionButtonProperty().set(event -> viewModel.deleteLastRow());
        view.onClearGridLettersMenuItemActionProperty()
            .set(event -> viewModel.resetContentLettersOnly());
        view.onClearGridContentMenuItemActionProperty().set(event -> viewModel.resetContentAll());
        view.onDeleteGridActionProperty().set(event -> viewModel.clear());
    }

    /**
     * Initializes bindings between dictionary view and dictionary view model.
     */
    private void initializeDictionaryBindings() {
        final DictionariesViewModel viewModel = crosswordSolverViewModel.dictionaryViewModel();
        view.dictionariesProperty().set(viewModel.dictionariesProperty());
        view.wordsProperty().set(viewModel.wordsProperty());
        view.suggestionsProperty().set(viewModel.suggestionsProperty());
        viewModel.selectedDictionariesProperty().addListener(this::onSelectedDictionaryChange);
    }

    /**
     * Performs the dictionary selection change action.
     *
     * @param change the dictionary selection change
     */
    private void onSelectedDictionaryChange(
            final ListChangeListener.Change<? extends DictionaryViewModel> change) {
        // TODO Dictionary words retrieval should be performed lazily when dictionary pane is
        //  displayed for the first time
        while (change.next()) {
            if (change.wasAdded()) {
                change.getAddedSubList().forEach(dictionaryController::listDictionaryEntries);
            }
        }
    }

    /**
     * Initializes bindings between the solver selection view and the solver selection view model.
     */
    private void initializeSolverSelectionBindings() {
        final SolverSelectionViewModel viewModel =
                crosswordSolverViewModel.solverSelectionViewModel();
        view.solversProperty().set(viewModel.availableSolversProperty());
        viewModel.selectedSolverProperty().bind(view.selectedSolverProperty());
    }

    /**
     * Initializes bindings between the solver progress view and the solver progress view models.
     */
    private void initializeSolverProgressBindings() {
        final SolverProgressViewModel solverProgressViewModel =
                crosswordSolverViewModel.solverProgressViewModel();
        view.solverProgressIndicatorVisibleProperty()
            .bind(solverProgressViewModel.solverRunningProperty());
        view.solverProgressIndicatorValueProperty()
            .bind(solverProgressViewModel.solverProgressProperty());
    }

    /**
     * Initializes binding between error view model and error view.
     */
    private void initializeErrorsBindings() {
        final ErrorsViewModel errorsViewModel = crosswordSolverViewModel.errorsViewModel();
        errorsViewModel.currentErrorProperty().addListener((observable, oldError, newError) -> {
            if (newError != null) {
                errorAlert.setContentText(newError);
                errorAlert.showAndWait();
                errorsViewModel.acknowledgeError();
            }
        });
    }

    /**
     * Initializes transverse bindings between views and view models.
     */
    private void initializeOtherSolverBindings() {
        // Grid edition buttons and grid pane shall be disabled when solver is running
        final BooleanProperty solverRunning = crosswordSolverViewModel.solverRunning();
        view.gridEditionDisableProperty().bind(solverRunning);

        // Solver button text shall be consistent with the solver state
        view.solveButtonTextProperty()
            .bind(new When(solverRunning).then(resources.getString("stop-solving-button"))
                                         .otherwise(resources.getString("start-solving-button")));

        // Solver button action shall allow control the start and stop of the solver
        view.onSolveButtonActionProperty().set(event -> onSolveButtonAction());

        // Solver button shall be disabled if solver is not running and no dictionary is selected
        // and grid is not empty
        final ListProperty<DictionaryViewModel> dictionaries =
                crosswordSolverViewModel.dictionaryViewModel().dictionariesProperty();
        final ReadOnlyMapProperty<GridPosition, CrosswordBoxViewModel> grid =
                crosswordSolverViewModel.crosswordGridViewModel().boxesProperty();
        view.solveButtonDisableProperty()
            .bind(solverRunning.not().and(dictionaries.emptyProperty().or(grid.emptyProperty())));
    }

    /**
     * Performs the solve button action.
     */
    private void onSolveButtonAction() {
        if (!crosswordSolverViewModel.solverRunning().get()) {
            solverController.startSolver();
        } else {
            solverController.stopSolver();
        }
    }

    /**
     * Populates solver and dictionary lists.
     */
    private void populateServiceLists() {
        solverController.listSolvers();
        dictionaryController.listDictionaries();
    }
}
