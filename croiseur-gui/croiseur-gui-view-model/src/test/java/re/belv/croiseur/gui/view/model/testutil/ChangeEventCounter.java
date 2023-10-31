/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model.testutil;

import javafx.beans.value.ObservableValue;

/**
 * A util for counting change events.
 */
public final class ChangeEventCounter<T> {

    private int count;

    /**
     * Constructs an instance.
     *
     * @param observable the observable value for which to count the changes
     */
    public ChangeEventCounter(final ObservableValue<? extends T> observable) {
        observable.addListener(this::changed);
    }

    /** @return The number of times this listener has received a change event. */
    public int count() {
        return count;
    }

    private void changed(final ObservableValue<? extends T> observable, final T oldValue,
                         final T newValue) {
        count++;
    }

}
