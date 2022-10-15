package com.gitlab.super7ramp.crosswords.gui.controller;

import com.gitlab.super7ramp.crosswords.gui.controller.dictionary.DictionaryController;
import com.gitlab.super7ramp.crosswords.gui.controller.solver.SolverController;
import com.gitlab.super7ramp.crosswords.gui.fx.view.CrosswordGrid;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.SolverViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.Objects;

/**
 * Binds views, view models and controllers.
 */
public final class CrosswordSolverController {

    private static SolverController solverController;

    private static DictionaryController dictionaryController;

    private static SolverViewModel solverViewModel;

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
     */
    public CrosswordSolverController() {
        // Nothing to do.
    }

    public static void inject(final SolverController solverControllerArg,
                              final DictionaryController dictionaryControllerArg,
                              final SolverViewModel solverViewModelArg) {
        solverController = solverControllerArg;
        dictionaryController = dictionaryControllerArg;
        solverViewModel = solverViewModelArg;
    }


    @FXML
    private void initialize() {
        Objects.requireNonNull(solverViewModel, "Solver view model not injected");
        Objects.requireNonNull(dictionaryController, "Dictionary controller not injected");

        final CrosswordViewModel crosswordViewModel = solverViewModel.crosswordViewModel();
        grid.boxes().bindBidirectional(crosswordViewModel.boxes());
        // TODO not great, view model could derive these information but that's not practical
        crosswordViewModel.width().bind(grid.columnCount());
        crosswordViewModel.height().bind(grid.rowCount());

        grid.disableProperty().bind(solverViewModel.solverRunning());
        solveButton.disableProperty().bind(solverViewModel.solverRunning());
        addColumnButton.disableProperty().bind(solverViewModel.solverRunning());
        addRowButton.disableProperty().bind(solverViewModel.solverRunning());
        deleteColumnButton.disableProperty().bind(solverViewModel.solverRunning());
        deleteRowButton.disableProperty().bind(solverViewModel.solverRunning());

        dictionaryComboBox.setItems(solverViewModel.dictionaryViewModel().dictionaries());
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
        solverController.start();
    }
}
