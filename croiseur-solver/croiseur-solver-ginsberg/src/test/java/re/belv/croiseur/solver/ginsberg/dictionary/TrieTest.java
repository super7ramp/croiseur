/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.solver.ginsberg.dictionary;

import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        final var trie = new Trie();

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
        final var trie = new Trie(Set.of("AAA", "ABBA", "CAC"));

        assertTrue(trie.contains("AAA"));
        assertTrue(trie.contains("ABBA"));
        assertTrue(trie.contains("CAC"));
        assertFalse(trie.contains("CACA"));
        assertFalse(trie.contains("C"));
        assertFalse(trie.contains(""));
    }

    @Test
    void iterator() {
        final var source = List.of("AAA", "AB", "BBB", "CDE", "ABC", "ABD", "ABE", "AAA", "BBB");
        final Iterator<String> it = new Trie(source).iterator();
        final var expected = new HashSet<>(source);

        final var actual = new HashSet<>();
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
        final var trie = new Trie(List.of("AAA", "ABBA", "CAC"));
        final Iterator<String> it = trie.iterator();
        it.next();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    void streamMatching() {
        final var trie = new Trie(Set.of("AAA", "ABBA", "CAC"));
        final Set<String> matches = trie.streamMatching(" A ").collect(toSet());
        assertEquals(Set.of("AAA", "CAC"), matches);
    }

    @Test
    void streamMatching_Second() {
        final var trie = new Trie(Set.of("AAA", "ABBA", "CAC"));
        final Set<String> matches = trie.streamMatching("C  ").collect(toSet());
        assertEquals(Set.of("CAC"), matches);
    }

    @Test
    void streamMatching_NoMatch() {
        final var trie = new Trie(Set.of("AAA", "ABBA", "CAC"));
        final Set<String> matches = trie.streamMatching("").collect(toSet());
        assertEquals(Collections.emptySet(), matches);
    }

    @Test
    void streamMatching_NoMatchEmptyTrie() {
        final var trie = new Trie();
        final Set<String> matches = trie.streamMatching(" A ").collect(toSet());
        assertEquals(Collections.emptySet(), matches);
    }

    @Test
    void clear() {
        final var trie = new Trie(Set.of("AAA", "ABBA", "CAC"));
        assertThrows(UnsupportedOperationException.class, trie::clear);
    }

    @Test
    void remove() {
        final var trie = new Trie(Set.of("AAA", "ABBA", "CAC"));
        assertThrows(UnsupportedOperationException.class, () -> trie.remove("AAA"));
    }
}
