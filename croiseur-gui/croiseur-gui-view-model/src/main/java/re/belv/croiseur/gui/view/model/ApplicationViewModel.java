/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import javafx.beans.property.BooleanProperty;

/**
 * Access to the application view models.
 */
public final class ApplicationViewModel {

    /** The puzzle selection view model. */
    private final PuzzleSelectionViewModel puzzleSelectionViewModel;

    /** The puzzle edition view model. */
    private final PuzzleEditionViewModel puzzleEditionViewModel;

    /** The puzzle codecs view model. */
    private final PuzzleCodecsViewModel puzzleCodecsViewModel;

    /** The view model of the dictionary. */
    private final DictionariesViewModel dictionariesViewModel;

    /** The view model of the solver selection. */
    private final SolverConfigurationViewModel solverConfigurationViewModel;

    /** The view model of the solver progress. */
    private final SolverProgressViewModel solverProgressViewModel;

    /** The errors view model. */
    private final ErrorsViewModel errorsViewModel;

    /**
     * Constructs an instance.
     */
    public ApplicationViewModel() {
        puzzleSelectionViewModel = new PuzzleSelectionViewModel();
        puzzleEditionViewModel = new PuzzleEditionViewModel();
        puzzleCodecsViewModel = new PuzzleCodecsViewModel();
        dictionariesViewModel = new DictionariesViewModel();
        solverConfigurationViewModel = new SolverConfigurationViewModel();
        solverProgressViewModel = new SolverProgressViewModel();
        errorsViewModel = new ErrorsViewModel();

        // Initializes special bindings between dictionary and grid view models.
        final CrosswordGridViewModel crosswordGridViewModel = puzzleEditionViewModel.crosswordGridViewModel();
        dictionariesViewModel.suggestionFilterProperty().bind(crosswordGridViewModel.currentSlotContentProperty());
        crosswordGridViewModel
                .currentSlotUnsolvableProperty()
                .bind(dictionariesViewModel.suggestionsProperty().emptyProperty());
    }

    /**
     * Returns the puzzle selection view model.
     *
     * @return the puzzle selection view model
     */
    public PuzzleSelectionViewModel puzzleSelectionViewModel() {
        return puzzleSelectionViewModel;
    }

    /**
     * Returns the puzzle edition view model.
     *
     * @return the puzzle edition view model
     */
    public PuzzleEditionViewModel puzzleEditionViewModel() {
        return puzzleEditionViewModel;
    }

    /**
     * Returns the puzzle codecs view model.
     *
     * @return the puzzle codecs view model
     */
    public PuzzleCodecsViewModel puzzleCodecsViewModel() {
        return puzzleCodecsViewModel;
    }

    /**
     * Returns the view model of edited puzzle details.
     * <p>
     * Convenience shortcut for {@code puzzleEditionViewModel().puzzleDetailsViewModel()}.
     *
     * @return the view model of edited puzzle details
     */
    public PuzzleDetailsViewModel puzzleDetailsViewModel() {
        return puzzleEditionViewModel.puzzleDetailsViewModel();
    }

    /**
     * Returns the view model of the edited crossword grid.
     * <p>
     * Convenience shortcut for: {@code puzzleEditionViewModel().crosswordGridViewModel()}.
     *
     * @return the view model of the edited crossword grid
     */
    public CrosswordGridViewModel crosswordGridViewModel() {
        return puzzleEditionViewModel.crosswordGridViewModel();
    }

    /**
     * Returns the view model of the edited crossword clues.
     * <p>
     * Convenience shortcut for: {@code puzzleEditionViewModel().cluesViewModel()}.
     *
     * @return the view model of the edited crossword clues
     */
    public CluesViewModel cluesViewModel() {
        return puzzleEditionViewModel.cluesViewModel();
    }

    /**
     * Returns the property indicating whether the puzzle is being saved.
     * <p>
     * Convenience shortcut for {@code puzzleEditionViewModel().savingInProgress()}.
     *
     * @return the property indicating whether the puzzle is being saved
     */
    public BooleanProperty puzzleIsBeingSaved() {
        return puzzleEditionViewModel.savingInProgressProperty();
    }

    /**
     * Returns the view model of the dictionary.
     *
     * @return the view model of the dictionary
     */
    public DictionariesViewModel dictionaryViewModel() {
        return dictionariesViewModel;
    }

    /**
     * Returns the view model of the solver configuration.
     *
     * @return the view model of the solver configuration
     */
    public SolverConfigurationViewModel solverConfigurationViewModel() {
        return solverConfigurationViewModel;
    }

    /**
     * Returns the view model of the solver progress.
     *
     * @return the view model of the solver progress
     */
    public SolverProgressViewModel solverProgressViewModel() {
        return solverProgressViewModel;
    }

    /**
     * Returns whether the solver is running.
     * <p>
     * Shortcut for {@code solverProgressViewModel().solverRunningProperty()}.
     *
     * @return whether the solver is running
     */
    public BooleanProperty solverRunning() {
        return solverProgressViewModel.solverRunningProperty();
    }

    /**
     * Returns whether the clue service is running.
     * <p>
     * Shortcut for {@code cluesViewModel().clueServiceIsRunningProperty()}.
     *
     * @return whether the clue service is running
     */
    public BooleanProperty clueServiceIsRunning() {
        return cluesViewModel().clueServiceIsRunningProperty();
    }

    /**
     * Returns the errors view model.
     *
     * @return the errors view model
     */
    public ErrorsViewModel errorsViewModel() {
        return errorsViewModel;
    }
}
