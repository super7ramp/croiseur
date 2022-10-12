package com.gitlab.super7ramp.crosswords.gui.binder;

import com.gitlab.super7ramp.crosswords.gui.controller.dictionary.DictionaryController;
import com.gitlab.super7ramp.crosswords.gui.controller.solver.SolverController;
import com.gitlab.super7ramp.crosswords.gui.fx.view.CrosswordGrid;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordViewModel;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.DictionaryViewModel;
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
        // Populate the empty grid with cells
        // TODO populate from initial model
        /*
        for (int i = 1; i <= 5; i++) {
            grid.addRow();
            grid.addColumn();
        }*/

        Objects.requireNonNull(crosswordViewModel, "Crossword view model not injected");
        grid.boxes().putAll(crosswordViewModel.boxes());

        Objects.requireNonNull(dictionaryViewModel, "Dictionary view model not injected");
        dictionaryComboBox.setItems(dictionaryViewModel.dictionaries());

        Objects.requireNonNull(dictionaryController, "Dictionary controller not injected");
        dictionaryController.start();
    }

    @FXML
    private void onSolveButtonClicked() {
        solverController.start();
    }
}
