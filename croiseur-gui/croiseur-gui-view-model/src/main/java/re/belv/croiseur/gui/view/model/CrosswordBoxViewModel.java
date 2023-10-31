/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Crossword box model.
 */
public final class CrosswordBoxViewModel {

    /** Whether the box is shaded or not. */
    private final BooleanProperty shaded;

    /** Whether the box is unsolvable or not. */
    private final BooleanProperty unsolvable;

    /** Whether the box is selected or not. */
    private final BooleanProperty selected;

    /** The content of the box filled by the user. */
    private final StringProperty userContent;

    /** The content of the box filled by the solver. */
    private final StringProperty solverContent;

    /**
     * Constructs an instance.
     */
    public CrosswordBoxViewModel() {
        shaded = new SimpleBooleanProperty(this, "shaded", false);
        unsolvable = new SimpleBooleanProperty(this, "unsolvable", false);
        selected = new SimpleBooleanProperty(this, "selected", false);
        userContent = new SimpleStringProperty(this, "userContent", "");
        solverContent = new SimpleStringProperty(this, "solverContent", "");
    }

    /**
     * Returns the boolean property indicating whether the box is shaded.
     *
     * @return the boolean property indicating whether the box is shaded
     */
    public BooleanProperty shadedProperty() {
        return shaded;
    }

    /**
     * Sets the shaded property value to {@code true}.
     */
    public void shade() {
        shaded.set(true);
    }

    /** Sets the shaded property value to {@code false}. */
    public void lighten() {
        shaded.set(false);
    }

    /**
     * Returns whether the box is shaded.
     *
     * @return whether the box is shaded
     */
    public boolean isShaded() {
        return shaded.get();
    }

    /**
     * Returns the boolean property indicating whether the box cannot be solved.
     *
     * @return the boolean property indicating whether the box cannot be solved
     */
    public BooleanProperty unsolvableProperty() {
        return unsolvable;
    }

    /**
     * Sets the value of the unsolvable property to {@code false}.
     */
    public void solvable() {
        unsolvable.set(false);
    }

    /**
     * Returns whether the box cannot be solved.
     *
     * @return whether the box cannot be solved
     */
    public boolean isUnsolvable() {
        return unsolvable.get();
    }

    /**
     * Returns the content filled by user as string property.
     *
     * @return the content filled by user as string property
     */
    public StringProperty userContentProperty() {
        return userContent;
    }

    /**
     * Sets the value of the user content property.
     *
     * @param value the new user content value
     */
    public void userContent(final String value) {
        userContent.set(value);
    }

    /**
     * Returns the value of the user content property.
     *
     * @return the value of the user content property
     */
    public String userContent() {
        return userContent.get();
    }

    /**
     * Returns the content filled by solver as string property.
     *
     * @return the content filled by solver as string property
     */
    public StringProperty solverContentProperty() {
        return solverContent;
    }

    /**
     * Sets the value of the solver content property.
     *
     * @param value the value to set
     */
    public void solverContent(final String value) {
        solverContent.set(value);
    }

    /**
     * Returns the value of the solver content property.
     *
     * @return the value of the solver content property
     */
    public String solverContent() {
        return solverContent.get();
    }

    /**
     * Returns the selected property.
     *
     * @return the selected property
     */
    public BooleanProperty selectedProperty() {
        return selected;
    }

    /**
     * Returns the value of the selected property.
     *
     * @return the value of the selected property
     */
    public boolean isSelected() {
        return selected.get();
    }

    /**
     * Sets the value of the selected property to {@code true}.
     */
    public void select() {
        selected.set(true);
    }

    /**
     * Sets the value of the selected property to {@code false}.
     */
    public void deselect() {
        selected.set(false);
    }
}
