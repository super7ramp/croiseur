/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.gui.view.model.util;

import java.util.Collection;
import java.util.List;

/**
 * Combine several collections in a single {@link List} view.
 * <p>
 * This list is unmodifiable with the modifiers of the {@link List} interface. This list is
 * modifiable only by adding or removing collection using {@link #aggregate} and
 * {@link #disaggregate} methods.
 *
 * @param <T> type of the element of the list
 */
public interface AggregateList<T> extends List<T> {

    /**
     * Creates a new {@link AggregateList}.
     *
     * @param collection       a first aggregate
     * @param otherCollections other aggregates
     * @param <T>              the element type
     * @return the created {@link AggregateList}
     */
    static <T> AggregateList of(Collection<T> collection, Collection<T>... otherCollections) {
        return new AggregateListImpl<>(collection, otherCollections);
    }

    /**
     * Aggregates the given collection at the end of this {@link AggregateList}.
     *
     * @param collection the collection to aggregate
     */
    void aggregate(final Collection<T> collection);

    /**
     * Aggregates the given collection at the given index
     *
     * @param aggregateIndex the desired <em>aggregate</em> index (not an element index)
     * @param collection     the collection to aggregate
     */
    void aggregate(final int aggregateIndex, final Collection<T> collection);

    /**
     * Disaggregates the collection at given index.
     *
     * @param aggregateIndex the <em>aggregate</em> index (not an element index)
     */
    void disaggregate(final int aggregateIndex);

    /**
     * Returns the aggregated list at given aggregate index.
     *
     * @param aggregateIndex the desired <em>aggregate</em> index (not an element index)
     * @return the aggregate list at the given index
     * @throws IndexOutOfBoundsException if aggregate index doesn't exist
     */
    List<T> aggregatedAt(final int aggregateIndex);

    /**
     * Returns the actual start index of the aggregated collection at given <em>aggregate
     * index</em>.
     *
     * @param aggregateIndex the <em>aggregate index</em>
     * @return the actual (i.e. element) start index of the aggregated collection
     * @throws IndexOutOfBoundsException if aggregate index doesn't exist
     */
    int elementIndex(final int aggregateIndex);

    /**
     * Returns the number of aggregated collections.
     *
     * @return the number of aggregated collections
     */
    int aggregateCount();

}
