package com.gitlab.super7ramp.crosswords.gui.controller;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.gui.controller.dictionary.DictionaryController;
import com.gitlab.super7ramp.crosswords.gui.controller.solver.SolverController;
import com.gitlab.super7ramp.crosswords.gui.controls.CrosswordGrid;
import com.gitlab.super7ramp.crosswords.gui.controls.CrosswordGridEditionToolbar;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordSolverViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.DictionaryViewModel;
import javafx.beans.binding.When;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

/**
 * The main controller.
 */
public final class CrosswordSolverController {

    /** The controller dedicated for the solver use-cases. */
    private final SolverController solverController;

    /** The controller dedicated for the dictionary use-cases. */
    private final DictionaryController dictionaryController;

    /** The view model. */
    private CrosswordSolverViewModel crosswordSolverViewModel;

    @FXML
    private CrosswordGrid grid;

    @FXML
    private CrosswordGridEditionToolbar gridEditionToolbar;

    @FXML
    private ComboBox<String> dictionaryComboBox;

    @FXML
    private Button solveButton;

    /**
     * Constructs an instance.
     *
     * @param crosswordService            the use-cases
     * @param crosswordSolverViewModelArg the view model
     */
    public CrosswordSolverController(final CrosswordService crosswordService,
                                     final CrosswordSolverViewModel crosswordSolverViewModelArg) {
        solverController = new SolverController(crosswordSolverViewModelArg,
                crosswordService.solverService());
        dictionaryController = new DictionaryController(crosswordService.dictionaryService());
        crosswordSolverViewModel = crosswordSolverViewModelArg;
    }

    @FXML
    private void initialize() {
        // Bind the crossword view model to the crossword view
        final CrosswordViewModel crosswordViewModel = crosswordSolverViewModel.crosswordViewModel();
        grid.boxes().bindBidirectional(crosswordViewModel.boxes());

        // Bind the dictionary view model to the dictionary view
        final DictionaryViewModel dictionaryViewModel =
                crosswordSolverViewModel.dictionaryViewModel();
        dictionaryComboBox.setItems(dictionaryViewModel.dictionaries());
        dictionaryViewModel.selectedDictionary()
                           .bind(dictionaryComboBox.getSelectionModel().selectedItemProperty());

        // Bind the grid editor buttons to the grid
        gridEditionToolbar.onAddColumnActionProperty().set(event -> grid.addColumn());
        gridEditionToolbar.onAddRowActionProperty().set(event -> grid.addRow());
        gridEditionToolbar.onDeleteColumnActionProperty().set(event -> grid.deleteLastColumn());
        gridEditionToolbar.onDeleteRowActionProperty().set(event -> grid.deleteLastRow());

        // Controls should be disabled when solver is running - except the stop button
        grid.disableProperty().bind(crosswordSolverViewModel.solverRunning());
        gridEditionToolbar.disableProperty().bind(crosswordSolverViewModel.solverRunning());
        dictionaryComboBox.disableProperty().bind(crosswordSolverViewModel.solverRunning());
        solveButton.textProperty()
                   .bind(new When(crosswordSolverViewModel.solverRunning()).then("Stop solving")
                                                                           .otherwise("Solve"));

        // Populate dictionaries
        dictionaryController.start();
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
