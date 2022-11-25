package com.gitlab.super7ramp.crosswords.gui.view.model.util;

import javafx.collections.ObservableListBase;

import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link ObservableAggregateList}.
 *
 * @param <T> element type
 */
final class ObservableAggregateListImpl<T> extends ObservableListBase<T> implements ObservableAggregateList<T> {

    /** The backing list. */
    private final AggregateList<T> backingList;

    /**
     * Constructs an instance.
     *
     * @param backingListArg the backing {@link AggregateList}
     */
    ObservableAggregateListImpl(final AggregateList<T> backingListArg) {
        backingList = backingListArg;
    }

    @Override
    public void aggregate(final Collection<T> collection) {
        beginChange();
        try {
            final int from = backingList.size();
            final int added = collection.size();
            backingList.aggregate(collection);
            if (added > 0) {
                nextAdd(from, from + added);
            }
        } finally {
            endChange();
        }
    }

    @Override
    public void aggregate(final int aggregateIndex, final Collection<T> collection) {
        beginChange();
        try {
            final int from = backingList.elementIndex(aggregateIndex);
            final int added = collection.size();
            backingList.aggregate(aggregateIndex, collection);
            if (added > 0) {
                nextAdd(from, from + added);
            }
        } finally {
            endChange();
        }
    }

    @Override
    public void disaggregate(final int aggregateIndex) {
        beginChange();
        try {
            final int where = backingList.elementIndex(aggregateIndex);
            final List<T> removed = backingList.aggregatedAt(aggregateIndex);
            backingList.disaggregate(aggregateIndex);
            if (removed.size() > 0) {
                nextRemove(where, removed);
            }
        } finally {
            endChange();
        }
    }

    @Override
    public List<T> aggregatedAt(final int aggregateIndex) {
        return backingList.aggregatedAt(aggregateIndex);
    }

    @Override
    public int elementIndex(final int aggregateIndex) {
        return backingList.elementIndex(aggregateIndex);
    }

    @Override
    public int aggregateCount() {
        return backingList.aggregateCount();
    }

    @Override
    public T get(final int index) {
        return backingList.get(index);
    }

    @Override
    public int size() {
        return backingList.size();
    }
}
