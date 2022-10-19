package com.gitlab.super7ramp.crosswords.gui.controller;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.gui.controller.dictionary.DictionaryController;
import com.gitlab.super7ramp.crosswords.gui.controller.solver.SolverController;
import com.gitlab.super7ramp.crosswords.gui.view.CrosswordGrid;
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
    private final CrosswordSolverViewModel crosswordSolverViewModel;

    @FXML
    private CrosswordGrid grid;

    @FXML
    private Button addColumnButton;

    @FXML
    private Button addRowButton;

    @FXML
    private Button deleteColumnButton;

    @FXML
    private Button deleteRowButton;

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
        final CrosswordViewModel crosswordViewModel = crosswordSolverViewModel.crosswordViewModel();
        grid.boxes().bindBidirectional(crosswordViewModel.boxes());

        grid.disableProperty().bind(crosswordSolverViewModel.solverRunning());
        addColumnButton.disableProperty().bind(crosswordSolverViewModel.solverRunning());
        addRowButton.disableProperty().bind(crosswordSolverViewModel.solverRunning());
        deleteColumnButton.disableProperty().bind(crosswordSolverViewModel.solverRunning());
        deleteRowButton.disableProperty().bind(crosswordSolverViewModel.solverRunning());
        dictionaryComboBox.disableProperty().bind(crosswordSolverViewModel.solverRunning());

        final DictionaryViewModel dictionaryViewModel =
                crosswordSolverViewModel.dictionaryViewModel();
        dictionaryComboBox.setItems(dictionaryViewModel.dictionaries());
        dictionaryViewModel.selectedDictionary()
                           .bind(dictionaryComboBox.getSelectionModel().selectedItemProperty());

        solveButton.textProperty()
                   .bind(new When(crosswordSolverViewModel.solverRunning()).then("Stop solving")
                                                                           .otherwise("Solve"));

        // Populate dictionaries
        dictionaryController.start();
    }

    @FXML
    private void onAddColumnButtonClicked() {
        grid.addColumn();
    }

    @FXML
    private void onAddRowButtonClicked() {
        grid.addRow();
    }

    @FXML
    private void onDeleteColumnButtonClicked() {
        grid.deleteLastColumn();
    }

    @FXML
    private void onDeleteRowButtonClicked() {
        grid.deleteLastRow();
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
