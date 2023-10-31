/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui;

import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import re.belv.croiseur.api.puzzle.PuzzleService;
import re.belv.croiseur.gui.controller.puzzle.PuzzleController;
import re.belv.croiseur.gui.view.SavedPuzzleSelector;
import re.belv.croiseur.gui.view.model.PuzzleCodecsViewModel;
import re.belv.croiseur.gui.view.model.PuzzleEditionViewModel;
import re.belv.croiseur.gui.view.model.PuzzleSelectionViewModel;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

/**
 * The welcome screen controller.
 */
final class WelcomeScreenController {

    /** The puzzle selection view model. */
    private final PuzzleSelectionViewModel puzzleSelectionViewModel;

    /** The puzzle codecs view model. */
    private final PuzzleCodecsViewModel puzzleCodecsViewModel;

    /** The puzzle service controller. */
    private final PuzzleController puzzleController;

    /** The file chooser (for the import function). */
    private final FileChooser fileChooser;

    /** The scene switcher. */
    private final SceneSwitcher sceneSwitcher;

    /** The puzzle selector of the welcome screen. */
    @FXML
    private SavedPuzzleSelector selectorView;

    /** The translations. */
    @FXML
    private ResourceBundle resources;

    /**
     * Constructs an instance.
     *
     * @param puzzleSelectionViewModelArg the puzzle selection view model
     * @param puzzleEditionViewModel      the puzzle edition view model
     * @param puzzleCodecsViewModelArg    the puzzle codecs view model
     * @param puzzleService               the puzzle service
     * @param sceneSwitcherArg            the scene switcher
     * @param executor                    the background task executor
     */
    WelcomeScreenController(final PuzzleSelectionViewModel puzzleSelectionViewModelArg,
                            final PuzzleEditionViewModel puzzleEditionViewModel,
                            final PuzzleCodecsViewModel puzzleCodecsViewModelArg,
                            final PuzzleService puzzleService, final SceneSwitcher sceneSwitcherArg,
                            final Executor executor) {
        puzzleSelectionViewModel = puzzleSelectionViewModelArg;
        puzzleCodecsViewModel = puzzleCodecsViewModelArg;
        puzzleController = new PuzzleController(puzzleSelectionViewModelArg, puzzleEditionViewModel,
                                                puzzleService, executor);
        fileChooser = new FileChooser();
        sceneSwitcher = sceneSwitcherArg;
    }

    /**
     * Initializes the control after object hierarchy has been loaded from FXML.
     */
    @FXML
    private void initialize() {
        initializeListViewBindings();
        initializeFileChooserBindings();
        initializeButtonsBindings();
        populateModels();
    }

    /**
     * Initializes bindings between the puzzle selection view model and the list view.
     */
    private void initializeListViewBindings() {
        selectorView.recentPuzzles().set(puzzleSelectionViewModel.availablePuzzlesProperty());
        puzzleSelectionViewModel.selectedPuzzleProperty()
                                .bind(selectorView.selectedPuzzleProperty());
    }

    /**
     * Initializes the bindings related to file chooser.
     */
    private void initializeFileChooserBindings() {
        fileChooser.setTitle(resources.getString("import-filechooser-title"));
        puzzleCodecsViewModel.decodersProperty().addListener((InvalidationListener) observable -> {
            final List<FileChooser.ExtensionFilter> extensionFilters =
                    puzzleCodecsViewModel.decodersProperty().stream()
                                         .map(codec -> new FileChooser.ExtensionFilter(
                                                 codec.name(), codec.extensions())).toList();
            fileChooser.getExtensionFilters().setAll(extensionFilters);
        });
    }

    /**
     * Initializes bindings between the controller and the view buttons.
     */
    private void initializeButtonsBindings() {
        selectorView.onNewPuzzleButtonActionProperty().set(e -> onNewPuzzleButtonAction());
        selectorView.onOpenSelectedPuzzleButtonActionProperty()
                    .set(e -> onOpenPuzzleButtonAction());
        selectorView.onImportPuzzleButtonActionProperty().set(e -> onImportPuzzleButtonAction());
        selectorView.onDeleteSelectedPuzzleButtonActionProperty()
                    .set(e -> onDeletePuzzleButtonAction());
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
        final File selectedFile = fileChooser.showOpenDialog(selectorView.getScene().getWindow());
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
        sceneSwitcher.switchToEditorView();
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
        puzzleController.listPuzzles();
        puzzleController.listPuzzleDecoders();
    }

}
