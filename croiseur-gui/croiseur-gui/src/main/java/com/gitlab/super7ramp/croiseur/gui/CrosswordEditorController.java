/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.gui.controller.dictionary.DictionaryController;
import com.gitlab.super7ramp.croiseur.gui.controller.puzzle.PuzzleController;
import com.gitlab.super7ramp.croiseur.gui.controller.solver.SolverController;
import com.gitlab.super7ramp.croiseur.gui.view.CrosswordEditorPane;
import com.gitlab.super7ramp.croiseur.gui.view.model.ApplicationViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordGridViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionariesViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionaryViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleDetailsViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverProgressViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverSelectionViewModel;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;

import java.util.ResourceBundle;
import java.util.concurrent.Executor;

/**
 * The crossword editor controller.
 */
public final class CrosswordEditorController {

    /** The translations. */
    private final ResourceBundle resources;

    /** The controller dedicated to the solver use-cases. */
    private final SolverController solverController;

    /** The controller dedicated to the dictionary use-cases. */
    private final DictionaryController dictionaryController;

    /** The controller dedicated to puzzle use-cases. */
    private final PuzzleController puzzleController;

    /** The view model. */
    private final ApplicationViewModel applicationViewModel;

    /** The view. */
    @FXML
    private CrosswordEditorPane view;

    /**
     * Constructs an instance.
     *
     * @param crosswordService        the use-cases
     * @param applicationViewModelArg the view model
     * @param executor                the executor allowing to run background tasks
     */
    public CrosswordEditorController(final CrosswordService crosswordService,
                                     final ApplicationViewModel applicationViewModelArg,
                                     final Executor executor) {
        solverController =
                new SolverController(applicationViewModelArg, crosswordService.solverService(),
                                     executor);
        dictionaryController =
                new DictionaryController(crosswordService.dictionaryService(), executor);
        puzzleController = new PuzzleController(applicationViewModelArg.puzzleSelectionViewModel(),
                                                applicationViewModelArg.puzzleDetailsViewModel(),
                                                applicationViewModelArg.crosswordGridViewModel(),
                                                crosswordService.puzzleService(), executor);
        applicationViewModel = applicationViewModelArg;
        resources = ResourceBundle.getBundle(CrosswordEditorController.class.getName());
    }

    @FXML
    private void initialize() {
        initializeCrosswordGridBindings();
        initializeDictionaryBindings();
        initializeSolverSelectionBindings();
        initializeSolverProgressBindings();
        initializeOtherSolverBindings();
        initializePuzzleBindings();
        populateServiceLists();
    }

    /**
     * Initializes bindings between the puzzle details view and the puzzle details view model.
     */
    private void initializePuzzleBindings() {
        final PuzzleDetailsViewModel viewModel = applicationViewModel.puzzleDetailsViewModel();
        view.puzzleTitleProperty().bindBidirectional(viewModel.titleProperty());
        view.puzzleAuthorProperty().bindBidirectional(viewModel.authorProperty());
        view.puzzleEditorProperty().bindBidirectional(viewModel.editorProperty());
        view.puzzleCopyrightProperty().bindBidirectional(viewModel.copyrightProperty());
        view.puzzleDateProperty().bindBidirectional(viewModel.dateProperty());
        view.onSaveButtonActionProperty().set(e -> puzzleController.savePuzzle());
    }

    /**
     * Initializes bindings between the grid view and the crossword grid view model.
     */
    private void initializeCrosswordGridBindings() {
        final CrosswordGridViewModel viewModel = applicationViewModel.crosswordGridViewModel();
        view.gridBoxesProperty().set(viewModel.boxesProperty());
        view.gridCurrentBoxProperty().bindBidirectional(viewModel.currentBoxPositionProperty());
        view.gridCurrentSlotOrientationVerticalProperty()
            .bindBidirectional(viewModel.currentSlotVerticalProperty());
        view.onAddRowActionButtonProperty().set(event -> viewModel.addRow());
        view.onAddColumnActionButtonProperty().set(event -> viewModel.addColumn());
        view.onDeleteColumnActionButtonProperty().set(event -> viewModel.deleteLastColumn());
        view.onDeleteRowActionButtonProperty().set(event -> viewModel.deleteLastRow());
        view.onClearGridAllLettersMenuItemActionProperty()
            .set(event -> viewModel.resetContentLettersOnly());
        view.onClearGridLettersFilledBySolverMenuItemActionProperty()
            .set(event -> viewModel.resetContentLettersFilledBySolverOnly());
        view.onClearGridContentMenuItemActionProperty().set(event -> viewModel.resetContentAll());
        view.onDeleteGridActionProperty().set(event -> viewModel.clear());
        view.onSuggestionSelected().set(viewModel::currentSlotContent);
    }

    /**
     * Initializes bindings between dictionary view and dictionary view model.
     */
    private void initializeDictionaryBindings() {
        final DictionariesViewModel viewModel = applicationViewModel.dictionaryViewModel();
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
                applicationViewModel.solverSelectionViewModel();
        view.solversProperty().set(viewModel.availableSolversProperty());
        viewModel.selectedSolverProperty().bind(view.selectedSolverProperty());
    }

    /**
     * Initializes bindings between the solver progress view and the solver progress view models.
     */
    private void initializeSolverProgressBindings() {
        final SolverProgressViewModel solverProgressViewModel =
                applicationViewModel.solverProgressViewModel();
        view.solverProgressIndicatorVisibleProperty()
            .bind(solverProgressViewModel.solverRunningProperty());
        view.solverProgressIndicatorValueProperty()
            .bind(solverProgressViewModel.solverProgressProperty());
    }

    /**
     * Initializes transverse bindings between views and view models.
     */
    private void initializeOtherSolverBindings() {
        // Grid edition buttons and grid pane shall be disabled when solver is running
        final BooleanProperty solverRunning = applicationViewModel.solverRunning();
        view.gridEditionDisableProperty().bind(solverRunning);

        // Solver button text shall be consistent with the solver state
        view.solveButtonTextProperty()
            .bind(new When(solverRunning).then(resources.getString("stop-solving-button"))
                                         .otherwise(resources.getString("start-solving-button")));

        // Solver button action shall allow control the start and stop of the solver
        view.onSolveButtonActionProperty().set(event -> onSolveButtonAction());

        // Solver button shall be disabled if solver is not running and no dictionary is selected
        // and grid is not empty
        final ReadOnlyListProperty<DictionaryViewModel> selectedDictionaries =
                applicationViewModel.dictionaryViewModel().selectedDictionariesProperty();
        final ReadOnlyMapProperty<GridPosition, CrosswordBoxViewModel> grid =
                applicationViewModel.crosswordGridViewModel().boxesProperty();
        view.solveButtonDisableProperty()
            .bind(solverRunning.not()
                               .and(selectedDictionaries.emptyProperty().or(grid.emptyProperty())));
    }

    /**
     * Performs the solve button action.
     */
    private void onSolveButtonAction() {
        if (!applicationViewModel.solverRunning().get()) {
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
