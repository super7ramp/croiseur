/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import re.belv.croiseur.api.CrosswordService;
import re.belv.croiseur.gui.controller.clue.ClueController;
import re.belv.croiseur.gui.controller.dictionary.DictionaryController;
import re.belv.croiseur.gui.controller.puzzle.PuzzleController;
import re.belv.croiseur.gui.controller.solver.SolverController;
import re.belv.croiseur.gui.view.CrosswordEditorPane;
import re.belv.croiseur.gui.view.model.ApplicationViewModel;
import re.belv.croiseur.gui.view.model.CluesViewModel;
import re.belv.croiseur.gui.view.model.CrosswordBoxViewModel;
import re.belv.croiseur.gui.view.model.CrosswordGridViewModel;
import re.belv.croiseur.gui.view.model.DictionariesViewModel;
import re.belv.croiseur.gui.view.model.DictionaryViewModel;
import re.belv.croiseur.gui.view.model.GridCoord;
import re.belv.croiseur.gui.view.model.PuzzleCodecsViewModel;
import re.belv.croiseur.gui.view.model.PuzzleDetailsViewModel;
import re.belv.croiseur.gui.view.model.SolverConfigurationViewModel;
import re.belv.croiseur.gui.view.model.SolverProgressViewModel;

import java.io.File;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

/**
 * The crossword editor controller.
 */
final class CrosswordEditorController {

    /** The controller dedicated to the dictionary use-cases. */
    private final DictionaryController dictionaryController;

    /** The controller dedicated to the solver use-cases. */
    private final SolverController solverController;

    /** The controller dedicated to puzzle use-cases. */
    private final PuzzleController puzzleController;

    /** The controller dedicated to clue use-cases. */
    private final ClueController clueController;

    /** The view model. */
    private final ApplicationViewModel applicationViewModel;

    /** The file chooser (for the export function). */
    private final FileChooser fileChooser;

    /** The utility to switch between scenes. */
    private final SceneSwitcher sceneSwitcher;

    /** The view. */
    @FXML
    private CrosswordEditorPane view;

    /** The translations. */
    @FXML
    private ResourceBundle resources;

    /**
     * Constructs an instance.
     *
     * @param crosswordService        the use-cases
     * @param applicationViewModelArg the view model
     * @param sceneSwitcherArg        the scene switcher
     * @param executor                the executor allowing to run background tasks
     */
    CrosswordEditorController(final CrosswordService crosswordService,
                              final ApplicationViewModel applicationViewModelArg,
                              final SceneSwitcher sceneSwitcherArg, final Executor executor) {
        dictionaryController =
                new DictionaryController(crosswordService.dictionaryService(), executor);
        solverController =
                new SolverController(applicationViewModelArg, crosswordService.solverService(),
                                     executor);
        clueController = new ClueController(applicationViewModelArg.cluesViewModel(),
                                            applicationViewModelArg.crosswordGridViewModel(),
                                            crosswordService.clueService(), executor);
        puzzleController = new PuzzleController(applicationViewModelArg.puzzleSelectionViewModel(),
                                                applicationViewModelArg.puzzleEditionViewModel(),
                                                crosswordService.puzzleService(), executor);
        applicationViewModel = applicationViewModelArg;
        fileChooser = new FileChooser();
        sceneSwitcher = sceneSwitcherArg;
    }

    @FXML
    private void initialize() {
        initializeCrosswordGridBindings();
        initializeDictionaryBindings();
        initializeSolverConfigurationBindings();
        initializeSolverProgressBindings();
        initializeOtherSolverBindings();
        initializePuzzleBindings();
        initializePuzzleExportBindings();
        initializeNavigationBindings();
        initializeClueBindings();
        populateModels();
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
        final BooleanProperty puzzleIsBeingSaved = applicationViewModel.puzzleIsBeingSaved();
        view.puzzleEditionDisableProperty().bind(puzzleIsBeingSaved);
    }

    /**
     * Initializes the bindings related to puzzle export function.
     */
    private void initializePuzzleExportBindings() {
        fileChooser.setTitle(resources.getString("export-filechooser-title"));

        final PuzzleCodecsViewModel puzzleCodecsViewModel =
                applicationViewModel.puzzleCodecsViewModel();
        puzzleCodecsViewModel.encodersProperty().addListener((InvalidationListener) observable -> {
            final List<FileChooser.ExtensionFilter> extensionFilters =
                    puzzleCodecsViewModel.encodersProperty().stream()
                                         .map(codec -> new FileChooser.ExtensionFilter(
                                                 codec.name(), codec.extensions())).toList();
            fileChooser.getExtensionFilters().setAll(extensionFilters);
        });
        view.onExportPuzzleButtonActionProperty().set(e -> onExportButtonAction());

        // Export button exports last saved puzzle; Disable it if puzzle hasn't been saved yet
        final PuzzleDetailsViewModel puzzleDetailsViewModel =
                applicationViewModel.puzzleDetailsViewModel();
        final BooleanExpression puzzleNotSavedYet =
                Bindings.createBooleanBinding(() -> puzzleDetailsViewModel.id() == null,
                                              puzzleDetailsViewModel.idProperty());
        view.puzzleExportButtonDisableProperty().bind(puzzleNotSavedYet);
    }

    /**
     * Performs the export button action.
     */
    private void onExportButtonAction() {
        final File selectedFile = fileChooser.showSaveDialog(view.getScene().getWindow());
        if (selectedFile != null) {
            final List<String> selectedExtensions =
                    fileChooser.getSelectedExtensionFilter().getExtensions();
            final String selectedFormat =
                    selectedExtensions.isEmpty() ? "unknown" : selectedExtensions.get(0);
            puzzleController.exportPuzzle(selectedFile, selectedFormat);
        } // else do nothing since no file has been chosen
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
        final BooleanBinding editionAllowed =
                applicationViewModel.puzzleIsBeingSaved().or(applicationViewModel.solverRunning())
                                    .not();
        view.onSuggestionSelected().set(suggestion -> {
            if (editionAllowed.get()) {
                viewModel.currentSlotContent(suggestion);
            }
        });
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
     * Initializes bindings between the solver configuration view and the solver configuration view
     * model.
     */
    private void initializeSolverConfigurationBindings() {
        final SolverConfigurationViewModel viewModel =
                applicationViewModel.solverConfigurationViewModel();
        view.solversProperty().set(viewModel.availableSolversProperty());
        viewModel.selectedSolverProperty().bind(view.selectedSolverProperty());
        viewModel.fillCluesOnSuccessProperty().bind(view.fillClueOnSolverSuccessProperty());
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
        // Edition shall be disabled when solver is running or when puzzle is being saved
        final BooleanProperty solverRunning = applicationViewModel.solverRunning();
        final BooleanProperty clueServiceIsRunning = applicationViewModel.clueServiceIsRunning();
        final BooleanProperty puzzleIsBeingSaved = applicationViewModel.puzzleIsBeingSaved();
        view.gridEditionDisableProperty()
            .bind(solverRunning.or(puzzleIsBeingSaved).or(clueServiceIsRunning));

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
        final ReadOnlyMapProperty<GridCoord, CrosswordBoxViewModel> grid =
                applicationViewModel.crosswordGridViewModel().boxesProperty();
        view.solveButtonDisableProperty()
            .bind(solverRunning.not()
                               .and(selectedDictionaries.emptyProperty().or(grid.emptyProperty())
                                                        .or(clueServiceIsRunning)));
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
     * Initialize view navigation bindings.
     */
    private void initializeNavigationBindings() {
        view.onBackToPuzzleSelectionButtonActionProperty().set(e -> {
            sceneSwitcher.switchToWelcomeScreen();
            applicationViewModel.puzzleEditionViewModel().reset();
            view.resetLateralPanePositions();
        });
    }

    /**
     * Initializes clue bindings
     */
    private void initializeClueBindings() {
        final CluesViewModel cluesViewModel =
                applicationViewModel.puzzleEditionViewModel().cluesViewModel();
        view.acrossCluesProperty().set(cluesViewModel.acrossCluesProperty());
        view.downCluesProperty().set(cluesViewModel.downCluesProperty());
        view.selectedAcrossClueIndexProperty()
            .bindBidirectional(cluesViewModel.selectedAcrossClueIndexProperty());
        view.selectedDownClueIndexProperty()
            .bindBidirectional(cluesViewModel.selectedDownClueIndexProperty());
        view.onFillClueButtonActionProperty().set(event -> clueController.getClueForCurrentSlot());

        view.clueEditionDisableProperty().bind(cluesViewModel.clueServiceIsRunningProperty());
        view.fillClueButtonHideProperty()
            .bind(cluesViewModel.clueProvidersProperty().emptyProperty());
        final ReadOnlyStringProperty slotContentProperty =
                applicationViewModel.crosswordGridViewModel().currentSlotContentProperty();
        final BooleanBinding slotEmpty = slotContentProperty.isEmpty();
        final BooleanBinding slotPartiallyFilled =
                Bindings.createBooleanBinding(() -> slotContentProperty.get().contains("."),
                                              slotContentProperty);
        view.fillClueButtonDisableProperty().bind(slotEmpty.or(slotPartiallyFilled));
    }

    /**
     * Populates models.
     */
    private void populateModels() {
        dictionaryController.listDictionaries();
        solverController.listSolvers();
        clueController.listClueProviders();
        puzzleController.listPuzzleEncoders();
    }

}
