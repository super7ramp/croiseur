/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.javafx.beans.binding;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableNumberValue;

/** Additional bindings that are not provided by the standard JavaFX Bindings class. */
public final class MoreBindings {

    /** Private constructor to prevent instantiation, static methods only. */
    private MoreBindings() {
        // nothing to do
    }

    /**
     * Returns a {@link DoubleBinding} that represents the floor of the given {@link ObservableNumberValue}.
     *
     * @param number the observable number value to floor
     * @return an observable double value representing the floor of the given number
     */
    public static DoubleBinding floor(final ObservableNumberValue number) {
        return Bindings.createDoubleBinding(() -> Math.floor(number.doubleValue()), number);
    }
}
