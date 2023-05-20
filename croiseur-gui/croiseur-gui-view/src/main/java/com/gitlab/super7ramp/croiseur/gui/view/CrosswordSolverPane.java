/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordBoxViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.DictionaryViewModel;
import com.gitlab.super7ramp.croiseur.gui.view.model.SolverItemViewModel;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

/**
 * An entire crossword solver view, based on {@link BorderPane}.
 */
public final class CrosswordSolverPane extends BorderPane {

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
    private CrosswordEditionToolbar toolbar;

    /** The pane splitting the grid and the dictionary pane. */
    @FXML
    private SplitPane centerSplitPane;

    /** The grid. */
    @FXML
    private CrosswordGridPane grid;

    /** The dictionaries pane. */
    @FXML
    private DictionariesPane dictionariesPane;

    /**
     * Constructs an instance.
     */
    public CrosswordSolverPane() {
        final Class<CrosswordSolverPane> clazz = CrosswordSolverPane.class;
        final String fxmlName = clazz.getSimpleName() + ".fxml";
        final URL location =
                Objects.requireNonNull(clazz.getResource(fxmlName), "Failed to locate " + fxmlName);
        final FXMLLoader fxmlLoader = new FXMLLoader(location);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        // Only to help SceneBuilder find other custom controls shipped in the same jar
        fxmlLoader.setClassLoader(clazz.getClassLoader());
        try {
            fxmlLoader.load();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
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
        toolbar.onAddColumnActionButtonProperty().set(event -> grid.addColumn());
        toolbar.onAddRowActionButtonProperty().set(event -> grid.addRow());
        toolbar.onDeleteColumnActionButtonProperty().set(event -> grid.deleteLastColumn());
        toolbar.onDeleteRowActionButtonProperty().set(event -> grid.deleteLastRow());
        toolbar.onClearGridLettersMenuItemActionProperty()
               .set(event -> grid.resetContentLettersOnly());
        toolbar.onClearGridContentMenuItemActionProperty().set(event -> grid.resetContentAll());
        toolbar.onDeleteGridActionProperty().set(event -> grid.clear());
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
            centerSplitPane.setDividerPosition(DIVIDER,
                                               dictionariesPaneVisible ? DIVIDER_POSITION_IDEAL :
                                                       DIVIDER_POSITION_RIGHT_COLLAPSED);
        } else {
            // Node may not be created yet
            Platform.runLater(() -> updateSplitPane(dictionariesPaneVisible));
        }
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
    public BooleanProperty gridIsCurrentSlotOrientationVerticalProperty() {
        return grid.isCurrentSlotVerticalProperty();
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
