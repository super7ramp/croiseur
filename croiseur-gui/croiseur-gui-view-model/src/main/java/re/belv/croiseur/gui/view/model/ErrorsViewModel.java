/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Errors view model.
 */
public final class ErrorsViewModel {

    /** The current error. */
    private final ReadOnlyStringWrapper currentError;

    /** The queued errors. */
    private final LinkedList<String> queuedErrors;

    /**
     * Constructs an instance.
     */
    ErrorsViewModel() {
        currentError = new ReadOnlyStringWrapper(this, "currentError");
        queuedErrors = new LinkedList<>();
    }

    /**
     * Returns the current error property.
     * <p>
     * Property value is {@code null} if no error is present.
     *
     * @return the current error property
     */
    public ReadOnlyStringProperty currentErrorProperty() {
        return currentError.getReadOnlyProperty();
    }

    /**
     * Adds an error.
     * <p>
     * The given error will be set as value of the {@link #currentErrorProperty()} unless this
     * property already contains an unacknowledged error (i.e. a non-{@code null} value), in which
     * case given error will be queued.
     *
     * @param error the error to add
     */
    public void addError(final String error) {
        Objects.requireNonNull(error, "Error given for presentation shall not be null");
        if (currentError.get() == null) {
            currentError.set(error);
        } else {
            queuedErrors.add(error);
        }
    }

    /**
     * Acknowledges the current error.
     * <p>
     * The {@link #currentErrorProperty()} value will be set to {@code null} unless a queued error
     * exists, in which case the queued error will be removed from queue and set as value of
     * {@link #currentErrorProperty()}.
     * <p>
     * If current error is {@code null}, the method has no effect.
     */
    public void acknowledgeError() {
        final String firstMessage = queuedErrors.pollFirst();
        currentError.set(firstMessage);
    }
}
