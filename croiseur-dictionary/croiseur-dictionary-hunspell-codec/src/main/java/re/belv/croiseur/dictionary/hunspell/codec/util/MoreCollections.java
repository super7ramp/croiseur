/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.util;

import java.util.ArrayList;
import java.util.Collection;

/** Additional collection utilities. */
public final class MoreCollections {

    /** Private constructor to prevent instantiation. */
    private MoreCollections() {
        // Nothing to do.
    }

    /**
     * Generates all the pairs of elements of given collection.
     *
     * <p>Example: [1,2,3] will give [(1,1), (1,2), (1,3), (2,1), (2,2), (2,3), (3,1), (3,2), (3,3)].
     *
     * @param collection the collection
     * @param <T> the type of elements
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

    /**
     * Generates all the triplets of elements of the given collections.
     *
     * <p>Example:
     *
     * <ul>
     *   <li>Lefts: ["left1"}
     *   <li>Middles: ["middle1", "middle2", "middle3"]
     *   <li>Rights: ["right1", "right2"]
     *   <li>Result: [("left1", "middle1", "end1"), ("left1", "middle1", "right2"), ("left1", "middle2", "right1"),
     *       ("left1", "middle2", "right2"), ("left1", "middle3", "right1"), ("left1", "middle3", "right2")]
     * </ul>
     *
     * @param lefts the elements that can be on the left of the triplets
     * @param middles the elements that can be in the middle of the triplets
     * @param rights the elements that can be on the right of the tripplets
     * @param <L> the type of the left elements
     * @param <M> the type of the middle elements
     * @param <R> the type of the right elements
     * @return all the triplets of elements
     * @throws NullPointerException if given collection is {@code null}
     */
    public static <L, M, R> Collection<Triplet<L, M, R>> triplets(
            final Collection<? extends L> lefts,
            final Collection<? extends M> middles,
            final Collection<? extends R> rights) {
        final Collection<Triplet<L, M, R>> triplets = new ArrayList<>(lefts.size() * middles.size() * rights.size());
        for (final L left : lefts) {
            for (final M middle : middles) {
                for (final R right : rights) {
                    triplets.add(new Triplet<>(left, middle, right));
                }
            }
        }
        return triplets;
    }
}
