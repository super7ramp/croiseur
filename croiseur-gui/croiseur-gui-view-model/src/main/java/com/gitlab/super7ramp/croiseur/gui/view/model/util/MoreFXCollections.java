/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.util;

/**
 * Additions to fx collections.
 */
public final class MoreFXCollections {

    /**
     * Private constructor, static factories only.
     */
    private MoreFXCollections() {
        // Nothing to do
    }

    /**
     * Creates a new empty {@link ObservableAggregateList}.
     *
     * @param <E> the element type
     * @return a new empty {@link ObservableAggregateList}
     */
    public static <E> ObservableAggregateList<E> observableAggregateList() {
        return new ObservableAggregateListImpl<>(new AggregateListImpl<>());
    }
}
