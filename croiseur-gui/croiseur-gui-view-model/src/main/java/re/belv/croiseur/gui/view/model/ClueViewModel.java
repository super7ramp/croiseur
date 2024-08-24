/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
    public ClueViewModel() {
        this("");
    }

    /**
     * Constructs an instance.
     *
     * @param userContentArg the user content
     */
    public ClueViewModel(final String userContentArg) {
        userContent = new SimpleStringProperty(this, "userContent", userContentArg);
        systemContent = new SimpleStringProperty(this, "systemContent", "");
    }

    /**
     * The clue as entered by user.
     *
     * @return the clue as entered by user.
     */
    public StringProperty userContentProperty() {
        return userContent;
    }

    /**
     * The value of the {@link #userContentProperty()}.
     *
     * @return the value of the {@link #userContentProperty()}.
     */
    public String userContent() {
        return userContent.get();
    }

    /**
     * Sets the value of the {@link #userContentProperty()}.
     *
     * @param value the value to set
     * @throws NullPointerException if given clue is {@code null}
     */
    public void userContent(final String value) {
        Objects.requireNonNull(value, "Clue user content shall be non null; Use empty string in absence of value");
        userContent.set(value);
    }

    /**
     * The clue as determined by system.
     *
     * @return the clue as determined by system
     */
    public StringProperty systemContentProperty() {
        return systemContent;
    }

    /**
     * Returns the value of the {@link #systemContentProperty()}.
     *
     * @return the value of the {@link #systemContentProperty()}
     */
    public String systemContent() {
        return systemContent.get();
    }

    /**
     * Sets the value of the {@link #systemContentProperty()}.
     *
     * @param value the value to set
     * @throws NullPointerException if given clue is {@code null}
     */
    public void systemContent(final String value) {
        Objects.requireNonNull(value, "Clue system content shall be non null; Use empty string in absence of value");
        systemContent.set(value);
    }

    /**
     * Resets both user and system content to an empty string.
     */
    public void reset() {
        userContent.set("");
        systemContent.set("");
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof final ClueViewModel that)) return false;
        return Objects.equals(userContent.get(), that.userContent.get())
                && Objects.equals(systemContent.get(), that.systemContent.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userContent.get(), systemContent.get());
    }

    @Override
    public String toString() {
        return "ClueViewModel{" + "userContent=" + userContent.get() + ", systemContent=" + systemContent.get() + '}';
    }
}
