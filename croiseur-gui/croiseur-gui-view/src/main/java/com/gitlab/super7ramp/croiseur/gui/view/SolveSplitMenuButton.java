/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view;

import com.gitlab.super7ramp.croiseur.gui.view.model.SolverItemViewModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * A start/stop solve button with additional menu to select solver.
 */
public final class SolveSplitMenuButton extends SplitMenuButton {

    /** The available solvers. */
    private final ListProperty<SolverItemViewModel> availableSolversProperty;

    /** The available solvers. */
    private final StringProperty selectedSolverProperty;

    /** The toggle group. */
    private final ToggleGroup toggleGroup;

    /**
     * Constructs an instance.
     */
    public SolveSplitMenuButton() {
        availableSolversProperty = new SimpleListProperty<>(this, "availableSolversProperty",
                FXCollections.observableArrayList());
        selectedSolverProperty = new SimpleStringProperty(this, "selectedSolverProperty", null);
        toggleGroup = new ToggleGroup();

        final String fxmlName = SolveSplitMenuButton.class.getSimpleName() + ".fxml";
        final URL location = Objects.requireNonNull(getClass().getResource(fxmlName), "Failed to "
                + "locate " + fxmlName);
        final ResourceBundle resourceBundle =
                ResourceBundle.getBundle(SolveSplitMenuButton.class.getName());
        final FXMLLoader fxmlLoader = new FXMLLoader(location, resourceBundle);
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    /**
     * Creates a new menu item with.
     *
     * @param solver the solver
     * @return a new menu item
     */
    private static RadioMenuItem createMenuItem(final SolverItemViewModel solver) {
        /*
         * MenuItem doesn't extend Control so a Tooltip cannot be added directly to it.
         * Here's a small hack that instead of setting text in the expected field puts it in a
         * Label inside the optional graphics node attached to the item. It displays fine and
         * tooltip can be attached to the Label (since it extends Control).
         */
        final Label label = new Label(solver.name());
        final Tooltip tooltip = new Tooltip(solver.description());
        label.setTooltip(tooltip);
        return new RadioMenuItem(null, label);
    }

    @FXML
    private void initialize() {
        availableSolversProperty.addListener((ListChangeListener<SolverItemViewModel>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(this::addMenuItem);
                } else {
                    /*
                     * Ignore other events, solver list is only supposed to be set once and for
                     * all at application startup.
                     */
                }
            }
        });
    }

    /**
     * Adds a new menu item representing the given solver item view model.
     * <p>
     * The first added item is automatically selected.
     *
     * @param solver the solver item view model
     */
    private void addMenuItem(final SolverItemViewModel solver) {
        final RadioMenuItem item = createMenuItem(solver);
        item.setToggleGroup(toggleGroup);
        item.setOnAction(event -> selectedSolverProperty.set(solver.name()));
        final List<MenuItem> items = getItems();
        if (items.isEmpty()) {
            item.setSelected(true);
            items.add(item);
            items.add(new SeparatorMenuItem());
        } else {
            items.add(item);
        }
    }

    /**
     * Returns the selected solver property.
     * <p>
     * Value is {@code null} if no solver is selected.
     *
     * @return the selected solver property
     */
    public ReadOnlyProperty<String> selectedSolverProperty() {
        return selectedSolverProperty;
    }

    /**
     * Returns the available solvers property.
     *
     * @return the available solvers property
     */
    public ListProperty<SolverItemViewModel> availableSolversProperty() {
        return availableSolversProperty;
    }

}
