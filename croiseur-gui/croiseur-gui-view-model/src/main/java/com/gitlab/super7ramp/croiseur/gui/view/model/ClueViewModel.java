/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

/**
 * The clues view model: Represents a single clue in the grid.
 */
public final class ClueViewModel {

    /** The clue, as entered by the user. */
    private final StringProperty userContent;

    /** The clue, as determined by the system. */
    private final StringProperty systemContent;

    /**
     * Constructs an instance.
     */
    ClueViewModel() {
        userContent = new SimpleStringProperty();
        systemContent = new SimpleStringProperty();
    }

    /**
     * The clue, as entered by the user.
     *
     * @return the clue, as entered by the user.
     */
    public StringProperty userContentProperty() {
        return userContent;
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
     * Sets the value of the user content property.
     *
     * @param value the value to set
     */
    public void userContent(final String value) {
        userContent.set(value);
    }

    /**
     * The clue, as entered by the system.
     *
     * @return the clue, as determined by the system.
     */
    public StringProperty systemContentProperty() {
        return systemContent;
    }

    /**
     * Returns the value of the system content property.
     *
     * @return the value of the system content property
     */
    public String systemContent() {
        return systemContent.get();
    }

    /**
     * Sets the value of system content property.
     *
     * @param value the value to set
     */
    public void systemContent(final String value) {
        systemContent.set(value);
    }

    /**
     * Resets both user and system content to an empty string.
     */
    public void reset() {
        userContent.set(null);
        systemContent.set(null);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final ClueViewModel that)) return false;
        return Objects.equals(userContent.get(), that.userContent.get()) &&
               Objects.equals(systemContent.get(), that.systemContent.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userContent.get(), systemContent.get());
    }

    @Override
    public String toString() {
        return "ClueViewModel{" +
               "userContent=" + userContent.get() +
               ", systemContent=" + systemContent.get() +
               '}';
    }
}
