/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.util;

import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests on {@link MoreCollections}.
 */
final class MoreCollectionsTest {

    @Test
    void pairs() {
        final Collection<Integer> input = List.of(1, 2, 3);

        final Collection<Pair<Integer, Integer>> actual = MoreCollections.pairs(input);

        final Collection<Pair<Integer, Integer>> expected =
                List.of(new Pair<>(1, 1), new Pair<>(1, 2), new Pair<>(1, 3), new Pair<>(2, 1),
                        new Pair<>(2, 2), new Pair<>(2, 3), new Pair<>(3, 1), new Pair<>(3, 2),
                        new Pair<>(3, 3));
        assertEquals(expected.size(), actual.size());
        assertTrue(actual.containsAll(expected));
    }

    @Test
    void triplets() {
        final Collection<String> lefts = List.of("left1");
        final Collection<String> middles = List.of("middle1", "middle2", "middle3");
        final Collection<String> ends = List.of("right1", "right2");

        final Collection<Triplet<String, String, String>> actual =
                MoreCollections.triplets(lefts, middles, ends);

        final Collection<Triplet<String, String, String>> expected =
                List.of(new Triplet<>("left1", "middle1", "right1"), new Triplet<>("left1",
                                "middle1", "right2"), new Triplet<>("left1", "middle2", "right1"),
                        new Triplet<>("left1", "middle2", "right2"), new Triplet<>("left1",
                                "middle3", "right1"), new Triplet<>("left1", "middle3", "right2"));

        assertEquals(expected.size(), actual.size());
        assertTrue(actual.containsAll(expected));
    }
}
