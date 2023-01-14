/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui.view.model;

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

    /** The content of the box. */
    private final StringProperty content;

    /**
     * Constructs an instance.
     */
    public CrosswordBoxViewModel() {
        shaded = new SimpleBooleanProperty(this, "shaded", false);
        unsolvable = new SimpleBooleanProperty(this, "unsolvable", false);
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
     * Resets the content of this box model to its defaults.
     */
    public void reset() {
        shaded.set(false);
        unsolvable.set(false);
        content.set("");
    }
}
