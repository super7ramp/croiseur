/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Additional collection utilities.
 */
public final class MoreCollections {

    /**
     * Private constructor to prevent instantiation.
     */
    private MoreCollections() {
        // Nothing to do.
    }

    /**
     * Generates all the pairs of elements of given collection.
     * <p>
     * Example: [1,2,3] will give [(1,1), (1,2), (1,3), (2,1), (2,2), (2,3), (3,1), (3,2), (3,3)].
     *
     * @param collection the collection
     * @param <T>        the type of elements
     * @return all the pairs of elements
     * @throws NullPointerException if given collection is {@code null}
     */
    public static <T> Collection<Pair<T, T>> pairs(final Collection<? extends T> collection) {
        final Collection<Pair<T, T>> pairs = new ArrayList<>(collection.size() * collection.size());
        for (final T left : collection) {
            for (final T right : collection) {
                pairs.add(new Pair<>(left, right));
            }
        }
        return pairs;
    }
}
