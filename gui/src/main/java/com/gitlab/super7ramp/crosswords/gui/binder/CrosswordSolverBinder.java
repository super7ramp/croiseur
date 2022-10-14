package com.gitlab.super7ramp.crosswords.gui.binder;

import com.gitlab.super7ramp.crosswords.gui.controller.dictionary.DictionaryController;
import com.gitlab.super7ramp.crosswords.gui.controller.solver.SolverController;
import com.gitlab.super7ramp.crosswords.gui.fx.model.CrosswordBox;
import com.gitlab.super7ramp.crosswords.gui.fx.model.IntCoordinate2D;
import com.gitlab.super7ramp.crosswords.gui.fx.view.CrosswordGrid;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.DictionaryViewModel;
import javafx.beans.property.SimpleMapProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.Objects;

/**
 * Binds views, view models and controllers.
 */
public final class CrosswordSolverBinder {

    private static SolverController solverController;

    private static DictionaryController dictionaryController;

    private static CrosswordViewModel crosswordViewModel;

    private static DictionaryViewModel dictionaryViewModel;

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
    public CrosswordSolverBinder() {
        // Nothing to do.
    }

    public static void inject(final SolverController solverControllerArg,
                              final DictionaryController dictionaryControllerArg,
                              final CrosswordViewModel crosswordViewModelArg,
                              final DictionaryViewModel dictionaryViewModelArg) {
        solverController = solverControllerArg;
        dictionaryController = dictionaryControllerArg;
        crosswordViewModel = crosswordViewModelArg;
        dictionaryViewModel = dictionaryViewModelArg;
    }


    @FXML
    private void initialize() {
        Objects.requireNonNull(crosswordViewModel, "Crossword view model not injected");
        grid.boxes().bindBidirectional(crosswordViewModel.boxes());
        crosswordViewModel.width().bind(grid.columnCount());
        crosswordViewModel.height().bind(grid.rowCount());

        Objects.requireNonNull(dictionaryViewModel, "Dictionary view model not injected");
        dictionaryComboBox.setItems(dictionaryViewModel.dictionaries());

        Objects.requireNonNull(dictionaryController, "Dictionary controller not injected");
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
