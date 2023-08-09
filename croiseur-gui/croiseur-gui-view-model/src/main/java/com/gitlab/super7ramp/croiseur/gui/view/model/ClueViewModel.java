/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import java.util.Objects;

/**
 * The clues view model: Represents a single clue in the grid.
 */
public final class ClueViewModel {

    /** The clue, as entered by the user. */
    private String userContent;

    /** The clue, as determined by the system. */
    private String systemContent;

    /**
     * Constructs an instance.
     */
    ClueViewModel() {
        userContent = "";
        systemContent = "";
    }

    /**
     * Returns the clue as entered by user.
     *
     * @return the clue as entered by user.
     */
    public String userContent() {
        return userContent;
    }

    /**
     * Sets the clue as entered by user.
     *
     * @param value the value to set
     */
    public void userContent(final String value) {
        Objects.requireNonNull(value,
                               "Clue user content shall be non null; Use empty string in absence of value");
        userContent = value;
    }

    /**
     * Returns the clue as determined by system.
     *
     * @return the clue as determined by system
     */
    public String systemContent() {
        return systemContent;
    }

    /**
     * Sets the clue as determined by system.
     *
     * @param value the value to set
     */
    public void systemContent(final String value) {
        Objects.requireNonNull(value,
                               "Clue system content shall be non null; Use empty string in absence of value");
        systemContent = value;
    }

    /**
     * Resets both user and system content to an empty string.
     */
    public void reset() {
        userContent = "";
        systemContent = "";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final ClueViewModel that)) return false;
        return Objects.equals(userContent, that.userContent) &&
               Objects.equals(systemContent, that.systemContent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userContent, systemContent);
    }

    @Override
    public String toString() {
        return "ClueViewModel{" +
               "userContent=" + userContent +
               ", systemContent=" + systemContent +
               '}';
    }
}
