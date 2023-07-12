/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.gui.controller.puzzle.PuzzleController;
import com.gitlab.super7ramp.croiseur.gui.view.SavedPuzzleSelector;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleEditionViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleSelectionViewModel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * The welcome screen controller.
 */
public final class WelcomeScreenController {

    /** The puzzle selection view model. */
    private final PuzzleSelectionViewModel puzzleSelectionViewModel;

    /** The puzzle service controller. */
    private final PuzzleController puzzleController;

    /** The editor view to switch to when a puzzle is selected. */
    private final Parent editorView;

    /** The welcome screen view. */
    @FXML
    private SavedPuzzleSelector view;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param puzzleEditionViewModel      the puzzle edition view model
     * @param puzzleService               the puzzle service
     * @param executor                    the background task executor
     * @param editorViewArg               the editor view to switch to when a puzzle is selected
     */
    public WelcomeScreenController(final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                                   final PuzzleEditionViewModel puzzleEditionViewModel,
                                   final PuzzleService puzzleService, final Executor executor,
                                   final Parent editorViewArg) {
        puzzleSelectionViewModel = puzzleSelectionViewModelArg;
        puzzleController =
                new PuzzleController(puzzleSelectionViewModelArg, puzzleEditionViewModel,
                                     puzzleService, executor);
        editorView = editorViewArg;
    }

    @FXML
    private void initialize() {
        view.recentPuzzles().set(puzzleSelectionViewModel.availablePuzzlesProperty());
        puzzleSelectionViewModel.selectedPuzzleProperty().bind(view.selectedPuzzleProperty());
        view.onNewPuzzleButtonActionProperty().set(e -> onNewPuzzleButtonAction());
        view.onOpenSelectedPuzzleButtonActionProperty().set(e -> onOpenPuzzleButtonAction());
        view.onImportPuzzleButtonActionProperty().set(e -> onImportPuzzleButtonAction());
        view.onDeleteSelectedPuzzleButtonActionProperty().set(e -> onDeletePuzzleButtonAction());
        puzzleController.listAvailablePuzzles();
    }

    /**
     * Action when 'new puzzle' button is pressed: Just switches to editor view (it is already
     * loaded with a default crossword).
     */
    private void onNewPuzzleButtonAction() {
        switchToEditorView();
    }

    /**
     * Action when 'import puzzle' button is pressed: Selects a file and import it.
     */
    private void onImportPuzzleButtonAction() {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import puzzle");
        // TODO read support formats from view model
        fileChooser.getExtensionFilters()
                   .add(new FileChooser.ExtensionFilter("xd files", List.of("*.xd")));
        final File selectedFile = fileChooser.showOpenDialog(view.getScene().getWindow());
        if (selectedFile != null) {
            final String selectedFormat =
                    fileChooser.getSelectedExtensionFilter().getExtensions().get(0);
            puzzleController.importPuzzle(selectedFile, selectedFormat);
        } // else do nothing since no file has been chosen
    }

    /**
     * Action when the 'edit selected puzzle' button is pressed: Loads the selected puzzle in editor
     * view then switches to editor view.
     */
    private void onOpenPuzzleButtonAction() {
        puzzleController.loadSelectedPuzzle();
        switchToEditorView();
    }

    /**
     * Switches to editor view.
     */
    private void switchToEditorView() {
        final Stage stage = (Stage) view.getScene().getWindow();
        final Scene editorScene = new Scene(editorView);
        stage.setScene(editorScene);
    }

    /**
     * Action when delete button is pressed: Deletes the selected puzzle from puzzle repository.
     */
    private void onDeletePuzzleButtonAction() {
        puzzleController.deleteSelectedPuzzle();
    }
}
