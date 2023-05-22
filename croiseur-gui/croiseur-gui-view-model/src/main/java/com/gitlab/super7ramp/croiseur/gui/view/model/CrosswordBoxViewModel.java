/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

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

    /** Whether the box is highlighted or not. */
    private final BooleanProperty highlighted;

    /** The content of the box. */
    private final StringProperty content;

    /**
     * Constructs an instance.
     */
    public CrosswordBoxViewModel() {
        shaded = new SimpleBooleanProperty(this, "shaded", false);
        unsolvable = new SimpleBooleanProperty(this, "unsolvable", false);
        highlighted = new SimpleBooleanProperty(this, "highlighted", false);
        content = new SimpleStringProperty(this, "content", "");
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
    public void unshade() {
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
     * Returns whether the box cannot be solved.
     *
     * @return whether the box cannot be solved
     */
    public boolean isUnsolvable() {
        return unsolvable.get();
    }

    /**
     * Returns the content as string property.
     *
     * @return the content as string property
     */
    public StringProperty contentProperty() {
        return content;
    }

    /**
     * Sets the value of the content property.
     *
     * @param value the new content value
     */
    public void content(final String value) {
        content.set(value);
    }

    /**
     * Returns the value of the content property.
     *
     * @return the value of the content property
     */
    public String content() {
        return content.get();
    }

    /**
     * Returns the box is highlighted.
     * <p>
     * This is an alternative to the built-in focused state, e.g. to highlight boxes that are part
     * of the same slot.
     *
     * @return the box is highlighted
     */
    public BooleanProperty highlightedProperty() {
        return highlighted;
    }

    /**
     * Returns the value of the highlighted property.
     * <p>
     * This is an alternative to the built-in focused state, e.g. to highlight boxes that are part
     * of the same slot.
     *
     * @return the value of the highlighted property
     */
    public boolean isHighlighted() {
        return highlighted.get();
    }

    /**
     * Sets the value of the highlighted property.
     *
     * @param highlightedValue the value to set
     */
    public void setHighlighted(final boolean highlightedValue) {
        highlighted.set(highlightedValue);
    }

    /**
     * Resets the content/unsolvable status/shade status of this box model to its defaults. Keeps
     * highlight status as is.
     */
    public void resetExceptHighlight() {
        unsolvable.set(false);
        shaded.set(false);
        content.set("");
    }
}
