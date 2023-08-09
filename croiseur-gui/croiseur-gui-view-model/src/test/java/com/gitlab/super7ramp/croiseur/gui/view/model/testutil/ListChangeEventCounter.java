/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.testutil;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * A util to count change events on {@link ObservableList}s.
 *
 * @param <T> the list element type
 */
public final class ListChangeEventCounter<T> {

    private int count, addedSize, removedSize, replacedSize;

    /**
     * Constructs an instance.
     *
     * @param observable the observable value for which to count the changes
     */
    public ListChangeEventCounter(final ObservableList<? extends T> observable) {
        observable.addListener(this::onChange);
    }

    /** @return the number of times this listener has received a change event. */
    public int count() {
        return count;
    }

    /** @return the cumulated changes removed size */
    public int removedSize() {
        return removedSize;
    }

    /** @return the cumulated changes added size */
    public int addedSize() {
        return addedSize;
    }

    /** @return the cumulated changed replaced size */
    public int replacedSize() {
        return addedSize;
    }

    private void onChange(final ListChangeListener.Change<? extends T> c) {
        while (c.next()) {
            count++;
            if (c.wasRemoved()) {
                removedSize += c.getRemovedSize();
            }
            if (c.wasAdded()) {
                addedSize += c.getAddedSize();
            }
            if (c.wasReplaced()) {
                replacedSize += c.getAddedSize();
            }
        }
    }

}
