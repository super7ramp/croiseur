/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionaryViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverItemViewModel;
import javafx.application.Platform;
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
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import java.util.function.Consumer;

/**
 * An entire crossword solver view, based on {@link BorderPane}.
 */
public final class CrosswordEditorPane extends BorderPane {

    /** The selector for the split pane divider node. */
    private static final String DIVIDER_SELECTOR = ".split-pane-divider";

    /** The index of the unique divider of the {@link #centerSplitPane}. */
    private static final int DIVIDER = 0;

    /** The divider position when dictionaries pane is collapsed. */
    private static final double DIVIDER_POSITION_RIGHT_COLLAPSED = 1.0;

    /** The ideal position of the divider (1 / golden ratio). */
    private static final double DIVIDER_POSITION_IDEAL = 0.618;

    /** The toolbar. */
    @FXML
    private CrosswordEditorToolbar toolbar;

    /** The pane splitting the grid and the dictionary pane. */
    @FXML
    private SplitPane centerSplitPane;

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

    @FXML
    private void initialize() {
        initializeToolbarGridPaneBindings();
        initializeToolbarDictionariesPaneBindings();
        initializeDictionariesPaneSplitPaneBindings();
    }

    /**
     * Binds the grid pane properties to toolbar ones.
     */
    private void initializeToolbarGridPaneBindings() {
        grid.disableProperty().bind(toolbar.gridEditionButtonsDisableProperty());
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
     * Binds the split pane properties with dictionaries pane ones.
     */
    private void initializeDictionariesPaneSplitPaneBindings() {
        dictionariesPane.visibleProperty().addListener(
                (observable, wasDictionaryVisible, isDictionaryVisible) -> updateSplitPane(
                        isDictionaryVisible));
        final boolean dictionariesPaneInitiallyVisible = dictionariesPane.isVisible();
        updateSplitPane(dictionariesPaneInitiallyVisible);
    }

    /**
     * Updates split pane properties.
     *
     * @param dictionariesPaneVisible whether split pane should actually be split
     */
    private void updateSplitPane(final boolean dictionariesPaneVisible) {
        final Node divider = centerSplitPane.lookup(DIVIDER_SELECTOR);
        if (divider != null) {
            divider.setVisible(dictionariesPaneVisible);
            divider.setManaged(dictionariesPaneVisible);
            centerSplitPane.setDividerPosition(DIVIDER,
                                               dictionariesPaneVisible ? DIVIDER_POSITION_IDEAL :
                                                       DIVIDER_POSITION_RIGHT_COLLAPSED);
        } else {
            // Node may not be created yet
            Platform.runLater(() -> updateSplitPane(dictionariesPaneVisible));
        }
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
     * Returns the grid edition controls disable property.
     * <p>
     * The controls are the 'add column', 'delete column', 'add row','delete row' and the crossword
     * grid pane itself.
     *
     * @return the grid edition controls disable property
     */
    public BooleanProperty gridEditionDisableProperty() {
        return toolbar.gridEditionButtonsDisableProperty();
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
    public MapProperty<GridPosition, CrosswordBoxViewModel> gridBoxesProperty() {
        return grid.boxesProperty();
    }

    /**
     * Returns the crossword grid current box position property.
     *
     * @return the crossword grid current box position property
     */
    public ObjectProperty<GridPosition> gridCurrentBoxProperty() {
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
}