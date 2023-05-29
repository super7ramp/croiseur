/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

/**
 * Errors view model.
 */
public final class ErrorsViewModel {

    private final ReadOnlyStringWrapper currentError;

    private final ObservableList<String> queuedErrors;

    ErrorsViewModel() {
        currentError = new ReadOnlyStringWrapper(this, "message");
        queuedErrors = FXCollections.observableList(new LinkedList<>());
    }

    public ReadOnlyStringProperty currentErrorProperty() {
        return currentError.getReadOnlyProperty();
    }

    public void addError(final String errorMessage) {
        if (currentError.get() == null) {
            currentError.set(errorMessage);
        } else {
            queuedErrors.add(errorMessage);
        }
    }

    public void acknowledgeError() {
        if (queuedErrors.isEmpty()) {
            currentError.set(null);
        } else {
            final String firstMessage = queuedErrors.remove(0);
            currentError.set(firstMessage);
        }
    }
}
