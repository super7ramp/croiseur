/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.gui.controller.puzzle.PuzzleController;
import com.gitlab.super7ramp.croiseur.gui.view.WelcomeScreenPane;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleSelectionViewModel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
    private WelcomeScreenPane view;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param puzzleService               the puzzle service
     * @param executor                    the background task executor
     * @param editorViewArg               the editor view to switch to when a puzzle is selected
     */
    public WelcomeScreenController(final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                                   final PuzzleService puzzleService,
                                   final Executor executor,
                                   final Parent editorViewArg) {
        puzzleSelectionViewModel = puzzleSelectionViewModelArg;
        puzzleController =
                new PuzzleController(puzzleSelectionViewModelArg, puzzleService, executor);
        editorView = editorViewArg;
    }

    @FXML
    private void initialize() {
        view.recentPuzzles().set(puzzleSelectionViewModel.availablePuzzlesProperty());
        view.onNewPuzzleButtonActionProperty().set(e -> switchToEditorView());
        view.onOpenPuzzleButtonActionProperty().set(e -> onOpenPuzzleButtonAction());
        puzzleSelectionViewModel.selectedPuzzleProperty().bind(view.selectedPuzzleProperty());
        puzzleController.listAvailablePuzzles();
    }

    /**
     * Switches to editor view scene.
     */
    private void switchToEditorView() {
        final Stage stage = (Stage) view.getScene().getWindow();
        final Scene editorScene = new Scene(editorView);
        stage.setScene(editorScene);
    }

    /**
     * Loads selected puzzle in editor view and switches to editor view.
     */
    private void onOpenPuzzleButtonAction() {
        puzzleController.loadSelectedPuzzle();
        switchToEditorView();
    }
}
