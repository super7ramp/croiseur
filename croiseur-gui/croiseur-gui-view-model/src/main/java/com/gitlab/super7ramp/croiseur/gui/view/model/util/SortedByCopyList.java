/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui.view.model.util;

import javafx.collections.ListChangeListener;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A sorted view of an {@link ObservableList} similar to
 * {@link javafx.collections.transformation.SortedList}.
 * <p>
 * The difference with {@link javafx.collections.transformation.SortedList} is that
 * {@link SortedByCopyList} fires only one event on every backing list change, doing a full copy of
 * the backing list and sorting that copy.
 * <p>
 * This is interesting when the source list is huge and receives only a few modifications of big
 * size. Memory footprint may be more important than
 * {@link javafx.collections.transformation.SortedList} but speed gain is significant on source list
 * with tens or hundreds of thousands of elements.
 *
 * @param <E> the element type
 */
public final class SortedByCopyList<E> extends ModifiableObservableListBase<E> implements ObservableList<E> {

    /** The sorted list. */
    private List<E> sorted;

    /**
     * Constructs an instance.
     *
     * @param backing    the backing observable list
     * @param comparator the comparator
     */
    public SortedByCopyList(final ObservableList<E> backing, final Comparator<E> comparator) {
        Objects.requireNonNull(backing);
        Objects.requireNonNull(comparator);
        sorted = new ArrayList<>();
        backing.addListener((ListChangeListener<E>) change -> {
            beginChange();
            try {
                while (change.next()) {
                    nextRemove(0, sorted);
                    sorted = new ArrayList<>(change.getList());
                    sorted.sort(comparator);
                    nextAdd(0, sorted.size());
                }
            } finally {
                endChange();
            }
        });
    }

    @Override
    public E get(final int index) {
        return sorted.get(index);
    }

    @Override
    public int size() {
        return sorted.size();
    }

    @Override
    protected void doAdd(final int index, final E element) {
        sorted.add(index, element);
    }

    @Override
    protected E doSet(final int index, final E element) {
        return sorted.set(index, element);
    }

    @Override
    protected E doRemove(final int index) {
        return sorted.remove(index);
    }
}
