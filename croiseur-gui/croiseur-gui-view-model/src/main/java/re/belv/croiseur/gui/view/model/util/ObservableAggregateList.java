/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model.util;

import javafx.collections.ObservableList;

/**
 * An observable {@link AggregateList}.
 *
 * <p>Just like {@link AggregateList}, the list is modifiable only by using specific aggregation/disaggregation methods
 * defined in {@link AggregateList}.
 *
 * @param <E> element type
 */
public interface ObservableAggregateList<E> extends ObservableList<E>, AggregateList<E> {
    // Nothing to add.
}
