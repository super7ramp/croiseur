/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui;

import com.gitlab.super7ramp.croiseur.api.puzzle.PuzzleService;
import com.gitlab.super7ramp.croiseur.gui.controller.puzzle.PuzzleController;
import com.gitlab.super7ramp.croiseur.gui.view.SavedPuzzleSelector;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleCodecsViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleEditionViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.PuzzleSelectionViewModel;
import javafx.beans.InvalidationListener;
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

    /** The file chooser (for the import function). */
    private final FileChooser fileChooser;

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
     * @param puzzleCodecsViewModel       the puzzle codecs view model
     * @param puzzleService               the puzzle service
     * @param executor                    the background task executor
     * @param editorViewArg               the editor view to switch to when a puzzle is selected
     */
    public WelcomeScreenController(final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                                   final PuzzleEditionViewModel puzzleEditionViewModel,
                                   final PuzzleCodecsViewModel puzzleCodecsViewModel,
                                   final PuzzleService puzzleService, final Executor executor,
                                   final Parent editorViewArg) {
        puzzleSelectionViewModel = puzzleSelectionViewModelArg;
        puzzleController = new PuzzleController(puzzleSelectionViewModelArg, puzzleEditionViewModel,
                                                puzzleService, executor);
        fileChooser = initializeFileChooser(puzzleCodecsViewModel);
        editorView = editorViewArg;
    }

    /**
     * Initializes the {@link #fileChooser}.
     * <p>
     * Note: The file chooser is <em>not</em> a JavaFx control and is not injected from FXML. It can
     * thus be initialized from constructor.
     *
     * @param puzzleCodecsViewModel the puzzle codecs view model
     */
    private static FileChooser initializeFileChooser(
            final PuzzleCodecsViewModel puzzleCodecsViewModel) {
        final FileChooser fileChooser;
        fileChooser = new FileChooser();
        // TODO l10n
        fileChooser.setTitle("Import puzzle");
        puzzleCodecsViewModel.decodersProperty().addListener((InvalidationListener) observable -> {
            final List<FileChooser.ExtensionFilter> extensionFilters =
                    puzzleCodecsViewModel.decodersProperty().stream()
                                         .map(codec -> new FileChooser.ExtensionFilter(
                                                 codec.name(), codec.extensions())).toList();
            fileChooser.getExtensionFilters().setAll(extensionFilters);
        });
        return fileChooser;
    }

    /**
     * Initializes the control after object hierarchy has been loaded from FXML.
     */
    @FXML
    private void initialize() {
        initializeListViewBindings();
        initializeButtonsBindings();
        populateModels();
    }

    /**
     * Initializes bindings between the puzzle selection view model and the list view.
     */
    private void initializeListViewBindings() {
        view.recentPuzzles().set(puzzleSelectionViewModel.availablePuzzlesProperty());
        puzzleSelectionViewModel.selectedPuzzleProperty().bind(view.selectedPuzzleProperty());
    }

    /**
     * Initializes bindings between the controller and the view buttons.
     */
    private void initializeButtonsBindings() {
        view.onNewPuzzleButtonActionProperty().set(e -> onNewPuzzleButtonAction());
        view.onOpenSelectedPuzzleButtonActionProperty().set(e -> onOpenPuzzleButtonAction());
        view.onImportPuzzleButtonActionProperty().set(e -> onImportPuzzleButtonAction());
        view.onDeleteSelectedPuzzleButtonActionProperty().set(e -> onDeletePuzzleButtonAction());
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
        final File selectedFile = fileChooser.showOpenDialog(view.getScene().getWindow());
        if (selectedFile != null) {
            final List<String> selectedExtensions =
                    fileChooser.getSelectedExtensionFilter().getExtensions();
            final String selectedFormat =
                    selectedExtensions.isEmpty() ? "unknown" : selectedExtensions.get(0);
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

    /**
     * Populate view models by calling the {@link #puzzleController}.
     */
    private void populateModels() {
        puzzleController.listAvailablePuzzles();
        puzzleController.listAvailablePuzzleDecoders();
    }

}
