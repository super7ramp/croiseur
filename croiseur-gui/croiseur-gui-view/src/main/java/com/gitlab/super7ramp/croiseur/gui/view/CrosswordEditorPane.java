/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionaryViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.GridCoord;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverItemViewModel;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

/**
 * An entire crossword editor view, based on {@link BorderPane}.
 * <p>
 * In order to limit complexity, this big control is built as a composition of smaller controls such
 * as {@link CrosswordGridPane} or {@link DictionariesPane}. The relevant properties of each of
 * these controls are re-exported here.
 */
public final class CrosswordEditorPane extends BorderPane {

    /** The CSS selector for split pane divider nodes. */
    private static final String DIVIDER_SELECTOR = ".split-pane-divider";

    /** Identifiers of dividers between grid and lateral panes. */
    private static final int LEFT_DIVIDER_ID = 0, RIGHT_DIVIDER_ID = 1;

    /** The divider positions when lateral panes are collapsed. */
    private static final double LEFT_DIVIDER_COLLAPSED_POSITION = 0.0,
            RIGHT_DIVIDER_COLLAPSED_POSITION = 1.0;

    /** The ideal position of the dividers. */
    private static final double LEFT_DIVIDER_IDEAL_POSITION = 0.25, RIGHT_DIVIDER_IDEAL_POSITION =
            0.75;

    /** The toolbar. */
    @FXML
    private CrosswordEditorToolbar toolbar;

    /** The pane splitting the grid and the dictionary pane. */
    @FXML
    private SplitPane centerSplitPane;

    /** The puzzle pane. */
    @FXML
    private PuzzlePane puzzlePane;

    /** The grid. */
    @FXML
    private CrosswordGridPane grid;

    /** The solver progress indicator. */
    @FXML
    private ProgressIndicator solverProgressIndicator;

    /** The dictionaries pane. */
    @FXML
    private DictionariesPane dictionariesPane;

    /**
     * Constructs an instance.
     */
    public CrosswordEditorPane() {
        FxmlLoaderHelper.load(this);
    }

    /**
     * Returns the puzzle title property.
     *
     * @return the puzzle title property
     */
    public StringProperty puzzleTitleProperty() {
        return puzzlePane.titleProperty();
    }

    /**
     * Returns the puzzle author property.
     *
     * @return the puzzle author property
     */
    public StringProperty puzzleAuthorProperty() {
        return puzzlePane.authorProperty();
    }

    /**
     * Returns the puzzle editor property.
     *
     * @return the puzzle editor property
     */
    public StringProperty puzzleEditorProperty() {
        return puzzlePane.editorProperty();
    }

    /**
     * Returns the puzzle copyright property
     *
     * @return the puzzle copyright property
     */
    public StringProperty puzzleCopyrightProperty() {
        return puzzlePane.copyrightProperty();
    }

    /**
     * Returns the puzzle date property.
     *
     * @return the puzzle date property
     */
    public StringProperty puzzleDateProperty() {
        return puzzlePane.dateProperty();
    }

    /**
     * Returns the on export button action property.
     *
     * @return the on export button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onExportButtonActionProperty() {
        return puzzlePane.onExportButtonActionProperty();
    }

    /**
     * The puzzle export button disable property.
     * <p>
     * Allows to disable specifically the export button. Note that global
     * {@link #puzzleEditionDisableProperty()} takes precedence (i.e. if
     * {@link #puzzleEditionDisableProperty()} is {@code true} then the button will be effectively
     * disabled).
     *
     * @return the export button disable property
     */
    public BooleanProperty puzzleExportButtonDisableProperty() {
        return puzzlePane.exportButtonDisableProperty();
    }

    /**
     * Returns the puzzle edition disable property.
     *
     * @return the puzzle edition disable property
     */
    public BooleanProperty puzzleEditionDisableProperty() {
        return puzzlePane.disableProperty();
    }

    /**
     * Returns the on add column button action property.
     *
     * @return the on add column button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onAddColumnActionButtonProperty() {
        return toolbar.onAddColumnActionButtonProperty();
    }

    /**
     * Returns the on delete column button action property.
     *
     * @return the on delete column button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onDeleteColumnActionButtonProperty() {
        return toolbar.onDeleteColumnActionButtonProperty();
    }

    /**
     * Returns the on add row button action property.
     *
     * @return the on add row button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onAddRowActionButtonProperty() {
        return toolbar.onAddRowActionButtonProperty();
    }

    /**
     * Returns the on delete row button action property.
     *
     * @return the on delete row button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onDeleteRowActionButtonProperty() {
        return toolbar.onDeleteRowActionButtonProperty();
    }

    /**
     * Returns the on 'clear letters filled by solver' menu item action property.
     *
     * @return the on 'clear letters filled by solver' menu item action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onClearGridLettersFilledBySolverMenuItemActionProperty() {
        return toolbar.onClearGridLettersFilledBySolverMenuItemActionProperty();
    }

    /**
     * Returns the on 'clear all grid letters' menu item action property.
     *
     * @return the on 'clear all grid letters' menu item action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onClearGridAllLettersMenuItemActionProperty() {
        return toolbar.onClearGridAllLettersMenuItemActionProperty();
    }

    /**
     * Returns the on clear grid content menu item action property.
     *
     * @return the on clear grid content menu item action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onClearGridContentMenuItemActionProperty() {
        return toolbar.onClearGridContentMenuItemActionProperty();
    }

    /**
     * Returns the on delete grid action property.
     *
     * @return the on delete grid action property.
     */
    public ObjectProperty<EventHandler<ActionEvent>> onDeleteGridActionProperty() {
        return toolbar.onDeleteGridActionProperty();
    }

    /**
     * Returns the solve button action property.
     *
     * @return the solve button action property
     */
    public ObjectProperty<EventHandler<ActionEvent>> onSolveButtonActionProperty() {
        return toolbar.onSolveButtonActionProperty();
    }

    /**
     * Returns the on save button action property.
     *
     * @return the on save button action property.
     */
    public ObjectProperty<EventHandler<ActionEvent>> onSaveButtonActionProperty() {
        return toolbar.onSaveButtonActionProperty();
    }

    /**
     * Returns the grid edition controls disable property.
     * <p>
     * The controls are the toolbar 'add column', 'delete column', 'add row','delete row', 'clear'
     * and 'save' buttons plus the central crossword grid pane.
     *
     * @return the grid edition controls disable property
     */
    public BooleanProperty gridEditionDisableProperty() {
        return toolbar.editionButtonsDisableProperty();
    }

    /**
     * Returns the solve button disable property.
     *
     * @return the solve button disable property
     */
    public BooleanProperty solveButtonDisableProperty() {
        return toolbar.solveButtonDisableProperty();
    }

    /**
     * Returns the solve button text property.
     *
     * @return the solve button text property
     */
    public StringProperty solveButtonTextProperty() {
        return toolbar.solveButtonTextProperty();
    }

    /**
     * Returns the solver progress indicator value property.
     *
     * @return the solver progress indicator value property
     */
    public DoubleProperty solverProgressIndicatorValueProperty() {
        return solverProgressIndicator.progressProperty();
    }

    /**
     * Returns the solver progress indicator visible property.
     *
     * @return the solver progress indicator visible property
     */
    public BooleanProperty solverProgressIndicatorVisibleProperty() {
        return solverProgressIndicator.visibleProperty();
    }

    /**
     * Returns the crossword grid map property.
     *
     * @return the crossword grid map property
     * @see CrosswordGridPane#boxesProperty()
     */
    public MapProperty<GridCoord, CrosswordBoxViewModel> gridBoxesProperty() {
        return grid.boxesProperty();
    }

    /**
     * Returns the crossword grid current box position property.
     *
     * @return the crossword grid current box position property
     */
    public ObjectProperty<GridCoord> gridCurrentBoxProperty() {
        return grid.currentBoxPositionProperty();
    }

    /**
     * Returns the crossword grid current slot orientation property.
     *
     * @return the crossword grid current slot orientation property
     */
    public BooleanProperty gridCurrentSlotOrientationVerticalProperty() {
        return grid.currentSlotVerticalProperty();
    }

    /**
     * Returns the displayed dictionaries.
     *
     * @return the displayed dictionaries
     */
    public ListProperty<DictionaryViewModel> dictionariesProperty() {
        return dictionariesPane.dictionariesProperty();
    }

    /**
     * Returns the suggestions.
     *
     * @return the suggestions
     */
    public ListProperty<String> suggestionsProperty() {
        return dictionariesPane.suggestionsProperty();
    }

    /**
     * Returns the "on suggestion selected" property.
     * <p>
     * The consumer will be given the selected suggested word, for every selection.
     *
     * @return the "on suggestion selected" property
     */
    public ObjectProperty<Consumer<String>> onSuggestionSelected() {
        return dictionariesPane.onSuggestionSelected();
    }

    /**
     * Returns the displayed solvers.
     *
     * @return the displayed solvers
     */
    public ListProperty<SolverItemViewModel> solversProperty() {
        return toolbar.solveButtonAvailableSolversProperty();
    }

    /**
     * Returns the selected solver.
     * <p>
     * If no selected solver, value is {@code null}.
     *
     * @return the displayed solver
     */
    public ReadOnlyProperty<String> selectedSolverProperty() {
        return toolbar.solveButtonSelectedSolverProperty();
    }

    /**
     * Returns the displayed dictionary words.
     *
     * @return the displayed dictionary words
     */
    public ListProperty<String> wordsProperty() {
        return dictionariesPane.wordsProperty();
    }

    /**
     * Initializes the control after object hierarchy has been loaded from FXML.
     */
    @FXML
    private void initialize() {
        initializeToolbarPuzzlePaneBindings();
        initializeToolbarGridPaneBindings();
        initializeToolbarDictionariesPaneBindings();
        initializePuzzlePaneSplitPaneBindings();
        initializeDictionariesPaneSplitPaneBindings();
    }

    /**
     * Binds the puzzle pane properties to toolbar ones.
     */
    private void initializeToolbarPuzzlePaneBindings() {
        final BooleanBinding puzzleToggleButtonSelectedProperty =
                toolbar.puzzleToggleButtonSelectedProperty()
                       .and(toolbar.resizeModeProperty().not());
        puzzlePane.visibleProperty().bind(puzzleToggleButtonSelectedProperty);
        puzzlePane.managedProperty().bind(puzzleToggleButtonSelectedProperty);
    }

    /**
     * Binds the grid pane properties to toolbar ones.
     */
    private void initializeToolbarGridPaneBindings() {
        grid.disableProperty().bind(toolbar.editionButtonsDisableProperty());
    }

    /**
     * Binds the dictionaries pane properties to toolbar ones.
     */
    private void initializeToolbarDictionariesPaneBindings() {
        final BooleanBinding dictionariesToggleButtonSelectedProperty =
                toolbar.dictionariesToggleButtonSelectedProperty()
                       .and(toolbar.resizeModeProperty().not());
        dictionariesPane.visibleProperty().bind(dictionariesToggleButtonSelectedProperty);
        dictionariesPane.managedProperty().bind(dictionariesToggleButtonSelectedProperty);
    }

    /**
     * Binds the split pane properties with puzzle pane ones.
     */
    private void initializePuzzlePaneSplitPaneBindings() {
        updateLeftDividerPosition();
        final InvalidationListener updateLeftDividerNodeOnFirstShow = new InvalidationListener() {
            @Override
            public void invalidated(final Observable observable) {
                if (getScene() != null) {
                    Platform.runLater(() -> updateLeftDividerNode());
                    observable.removeListener(this);
                }
            }
        };
        sceneProperty().addListener(updateLeftDividerNodeOnFirstShow);
        puzzlePane.visibleProperty().addListener(observable -> updateLeftDivider());
    }

    /**
     * Updates the left divider node visibility and position.
     */
    private void updateLeftDivider() {
        updateLeftDividerNode();
        updateLeftDividerPosition();
    }

    /**
     * Updates the left divider node visibility.
     * <p>
     * Left divider is on the window's left border when the left pane (the puzzle pane) is not
     * visible. But if it is kept visible, user may select it when trying to resize the window,
     * resulting in the blank left pane being resized instead of the window, which would be very
     * confusing for user.
     * <p>
     * Hence, this method masks the left divider when puzzle pane is not visible. It is meant to be
     * called every time puzzle pane visibility changes.
     */
    private void updateLeftDividerNode() {
        final boolean puzzlePaneVisible = puzzlePane.isVisible();
        dividerNodeOf(LEFT_DIVIDER_ID).ifPresent(node -> {
            node.setVisible(puzzlePaneVisible);
            node.setManaged(puzzlePaneVisible);
        });
    }

    /**
     * Updates the left divider position.
     */
    private void updateLeftDividerPosition() {
        final boolean puzzlePaneVisible = puzzlePane.isVisible();
        centerSplitPane.setDividerPosition(LEFT_DIVIDER_ID,
                                           puzzlePaneVisible ? LEFT_DIVIDER_IDEAL_POSITION :
                                                   LEFT_DIVIDER_COLLAPSED_POSITION);
    }

    /**
     * Binds the split pane properties with dictionaries pane ones.
     */
    private void initializeDictionariesPaneSplitPaneBindings() {
        updateRightDividerPosition();
        final InvalidationListener updateRightDividerNodeOnFirstShow = new InvalidationListener() {
            @Override
            public void invalidated(final Observable observable) {
                if (getScene() != null) {
                    Platform.runLater(() -> updateRightDividerNode());
                    observable.removeListener(this);
                }
            }
        };
        sceneProperty().addListener(updateRightDividerNodeOnFirstShow);
        dictionariesPane.visibleProperty().addListener(observable -> updateRightDivider());
    }

    /**
     * Updates the right divider node visibility and position.
     */
    private void updateRightDivider() {
        updateRightDividerNode();
        updateRightDividerPosition();
    }

    /**
     * Updates the right divider node visibility.
     * <p>
     * Right divider is on the window's right border when the right pane (the dictionaries pane) is
     * not visible. But if it is kept visible, user may select it when trying to resize the window,
     * resulting in the blank right pane being resized instead of the window, which would be very
     * confusing for user.
     * <p>
     * Hence, this method masks the right divider when dictionaries pane is not visible. It is meant
     * to be called every time dictionaries pane visibility changes.
     */
    private void updateRightDividerNode() {
        final boolean dictionariesPaneVisible = dictionariesPane.isVisible();
        dividerNodeOf(RIGHT_DIVIDER_ID).ifPresent(node -> {
            node.setVisible(dictionariesPaneVisible);
            node.setManaged(dictionariesPaneVisible);
        });
    }

    /**
     * Updates the right divider position.
     */
    private void updateRightDividerPosition() {
        final boolean dictionariesPaneVisible = dictionariesPane.isVisible();
        centerSplitPane.setDividerPosition(RIGHT_DIVIDER_ID,
                                           dictionariesPaneVisible ? RIGHT_DIVIDER_IDEAL_POSITION :
                                                   RIGHT_DIVIDER_COLLAPSED_POSITION);
    }

    /**
     * Finds the split pane divider node corresponding to given divider id.
     *
     * @param dividerId the divider id
     * @return the split pane divider node corresponding to given divider id, or
     * {@link Optional#empty()} if no such divider has been found (e.g. if pane hasn't been shown
     * yet)
     */
    private Optional<Node> dividerNodeOf(final int dividerId) {
        if (getScene() == null) {
            return Optional.empty();
        }
        /*
         * Select all dividers then filter by position: There is no way to get a specific divider
         * via a CSS selector.
         */
        final Set<Node> dividers = centerSplitPane.lookupAll(DIVIDER_SELECTOR);
        final double normalizedX = centerSplitPane.getDividerPositions()[dividerId];
        final double x = getScene().getWidth() * normalizedX;
        return dividers.stream().filter(node -> {
            final Bounds boundsInLocal = node.getBoundsInLocal();
            final Bounds boundsInScene = node.localToScene(boundsInLocal);
            return boundsInScene.getMinX() <= x && x <= boundsInScene.getMaxX();
        }).findFirst();
    }
}
