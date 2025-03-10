/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.dictionary;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Tests for {@link Trie}. */
final class TrieTest {

    @Test
    void add() {
        final Trie trie = new Trie();

        trie.add("AAA");
        trie.add("ABBA");
        trie.add("CAC");

        assertTrue(trie.contains("AAA"));
        assertTrue(trie.contains("ABBA"));
        assertTrue(trie.contains("CAC"));
        assertEquals(3, trie.size());
    }

    @Test
    void contains() {
        final Trie trie = new Trie(Set.of("AAA", "ABBA", "CAC"));

        assertTrue(trie.contains("AAA"));
        assertTrue(trie.contains("ABBA"));
        assertTrue(trie.contains("CAC"));
        assertFalse(trie.contains("CACA"));
        assertFalse(trie.contains("C"));
        assertFalse(trie.contains(""));
    }

    @Test
    void containsPattern() {
        final Trie trie = new Trie(Set.of("AAA", "ABBA", "CAC"));

        assertTrue(trie.containsMatching("AA "));
        assertTrue(trie.containsMatching("A  A"));
        assertTrue(trie.containsMatching("  C"));
        assertFalse(trie.containsMatching("CAC "));
        assertFalse(trie.containsMatching("C"));
        assertFalse(trie.containsMatching(""));
    }

    @Test
    void iterator() {
        final Set<String> expected = Set.of("AAA", "AB", "BBB", "CDE", "ABC", "ABD", "ABE");
        final Iterator<String> it = new Trie(expected).iterator();

        final Set<String> actual = new HashSet<>();
        while (it.hasNext()) {
            actual.add(it.next());
        }

        assertEquals(expected, actual);
    }

    @Test
    void iteratorEmpty() {
        final Iterator<String> it = new Trie().iterator();
        assertFalse(it.hasNext());
    }

    @Test
    void iteratorRemove() {
        // Using list for reproducibility
        final Trie trie = new Trie(List.of("AAA", "ABBA", "CAC"));
        final Iterator<String> it = trie.iterator();
        it.next();

        it.remove();

        assertEquals(2, trie.size());
        assertFalse(trie.contains("AAA"));
        assertTrue(trie.containsAll(Set.of("ABBA", "CAC")));
    }

    @Test
    void streamMatching() {
        final Trie trie = new Trie(Set.of("AAA", "ABBA", "CAC"));
        final Set<String> matches = trie.streamMatching(" A ").collect(toSet());
        assertEquals(Set.of("AAA", "CAC"), matches);
    }

    @Test
    void streamMatching_Second() {
        final Trie trie = new Trie(Set.of("AAA", "ABBA", "CAC"));
        final Set<String> matches = trie.streamMatching("C  ").collect(toSet());
        assertEquals(Set.of("CAC"), matches);
    }

    @Test
    void streamMatching_NoMatch() {
        final Trie trie = new Trie(Set.of("AAA", "ABBA", "CAC"));
        final Set<String> matches = trie.streamMatching("").collect(toSet());
        assertEquals(Collections.emptySet(), matches);
    }

    @Test
    void streamMatching_NoMatchEmptyTrie() {
        final Trie trie = new Trie();
        final Set<String> matches = trie.streamMatching(" A ").collect(toSet());
        assertEquals(Collections.emptySet(), matches);
    }

    @Test
    void clear() {
        final Trie trie = new Trie(Set.of("AAA", "ABBA", "CAC"));

        trie.clear();

        assertTrue(trie.isEmpty());
        assertFalse(trie.contains("AAA"));
        assertFalse(trie.contains("ABBA"));
        assertFalse(trie.contains("CAC"));
    }

    @Test
    void remove() {
        final Trie trie = new Trie(Set.of("AAA", "ABBA", "CAC"));

        trie.remove("AAA");

        assertEquals(2, trie.size());
        assertFalse(trie.contains("AAA"));
        assertTrue(trie.containsAll(Set.of("ABBA", "CAC")));
    }

    @Test
    void removeIf() {
        final Trie trie = new Trie(Set.of("AAA", "AB", "BBB", "CDE", "ABC", "ABD", "ABE"));

        trie.removeIf(s -> true);

        assertTrue(trie.isEmpty());
    }

    @Test
    void removeNonMatching_middle() {
        final Trie trie = new Trie(Set.of("AAA", "AB", "BBB", "CDE", "ABC", "ABD", "ABE"));

        trie.removeNonMatching(" B ");

        assertEquals(4, trie.size());
        assertTrue(trie.containsAll(Set.of("BBB", "ABC", "ABD", "ABE")));
    }

    @Test
    void removeNonMatching_prefix() {
        final Trie trie = new Trie(Set.of("AAA", "AB", "BBB", "CDE", "ABC", "ABD", "ABE"));

        trie.removeNonMatching("A  ");

        assertEquals(4, trie.size());
        assertTrue(trie.containsAll(Set.of("AAA", "ABC", "ABD", "ABE")));
    }

    @Test
    void removeNonMatching_suffix() {
        final Trie trie = new Trie(Set.of("AAA", "AB", "BBB", "CDE", "ABC", "ABD", "ABE"));

        trie.removeNonMatching(" BE");

        assertEquals(1, trie.size());
        assertTrue(trie.contains("ABE"));
    }
}
