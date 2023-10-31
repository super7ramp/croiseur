/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model.util;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Allows to view several collections as a single {@link List}.
 *
 * @param <T> type of the element of the list
 */
final class AggregateListImpl<T> extends AbstractList<T> implements AggregateList<T> {

    /** The aggregated lists. */
    private final List<List<T>> lists;

    /**
     * Constructs an instance.
     */
    AggregateListImpl() {
        lists = new LinkedList<>();
    }

    /**
     * Constructs an instance from given collection.
     *
     * @param collection a collection
     * @throws NullPointerException if given collection is {@code null}
     */
    AggregateListImpl(final Collection<T> collection) {
        this();
        lists.add(new ArrayList<>(collection));
    }

    /**
     * Constructs an instance from given collections.
     *
     * @param collection a collection
     * @param others     other collections
     * @throws NullPointerException if a given collection is {@code null}
     */
    @SafeVarargs
    AggregateListImpl(final Collection<T> collection, final Collection<T>... others) {
        this(collection);
        for (final Collection<T> other : others) {
            lists.add(new ArrayList<>(other));
        }
    }

    @Override
    public T get(final int index) {
        int endIndex = 0;
        for (final List<T> list : lists) {
            final int startIndex = endIndex;
            endIndex += list.size();
            if (index < endIndex) {
                return list.get(index - startIndex);
            }
        }
        throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
    }

    @Override
    public int size() {
        int size = 0;
        for (final List<T> list : lists) {
            size += list.size();
        }
        return size;
    }

    @Override
    public void aggregate(final Collection<T> collection) {
        lists.add(new ArrayList<>(collection));
    }

    @Override
    public void aggregate(final int index, final Collection<T> collection) {
        lists.add(index, new ArrayList<>(collection));
    }

    @Override
    public void disaggregate(final int index) {
        lists.remove(index);
    }

    @Override
    public List<T> aggregatedAt(final int index) {
        return lists.get(index);
    }

    @Override
    public int elementIndex(final int aggregateIndex) {
        if (aggregateIndex >= lists.size()) {
            throw new IndexOutOfBoundsException();
        }
        int size = 0;
        for (final List<T> list : lists.subList(0, aggregateIndex)) {
            size += list.size();
        }
        return size;
    }

    @Override
    public int aggregateCount() {
        return lists.size();
    }
}
