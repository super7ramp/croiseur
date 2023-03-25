/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * An implementation of the trie data structure.
 * <p>
 * All methods expect non-null arguments.
 * <p>
 * Note that element removal is not supported, at the exception of {@link #clear()} which is
 * trivial.
 *
 * <h2>Thread safety</h2>
 * This collection is not thread-safe.
 *
 * <h2>Patterns</h2>
 * Some methods ({@link #containsMatching(String)}, {@link #streamMatching(String)}) support a
 * simplistic form of pattern matching. The supported wildcards for these methods are:
 * <ul>
 *     <li>"{@value Trie.TrieIterator#ANY_CHARACTER_WILDCARD}": Any character</li>
 * </ul>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Trie">Trie on Wikipedia</a>
 */
final class Trie extends AbstractCollection<String> {

    /**
     * A node of the trie.
     */
    private static final class TrieNode {

        /** The children nodes. LinkedHashMap for iteration reproducibility and speed. */
        private final Map<Character, TrieNode> children = new LinkedHashMap<>();

        /** Whether this node is terminal. */
        private boolean isTerminal;
    }

    /**
     * A trie {@link Iterator} implementation. Optionally filters words according to a pattern
     * passed at construction time.
     */
    private final class TrieIterator implements Iterator<String> {

        /** The any-character wildcard. */
        private static final char ANY_CHARACTER_WILDCARD = ' ';

        /** The current node iterator stack. Each node is given its own iterator. */
        private final ListIterator<Iterator<Map.Entry<Character, TrieNode>>> nodeIterators;

        /** The pattern to match, or {@code null} if no pattern to match. */
        private final String pattern;

        /**
         * The {@link #nextWord} builder.
         * <p>
         * A unique instance per iterator is used instead of a new instance per call to
         * {@link #findAndUpdateNextWord()} in order to avoid memory allocations.
         */
        private final StringBuilder nextWordBuilder;

        /**
         * The next word (i.e. the word that will be returned on next call to {@link #next()})
         * or {@code null} if iterator has no next element.
         */
        private String nextWord;

        /**
         * Constructs an instance.
         *
         * @param patternArg the pattern to match, or {@code null} if no filter
         */
        TrieIterator(final String patternArg) {
            pattern = patternArg;
            nextWordBuilder = new StringBuilder(patternArg != null ? patternArg.length() : 16);
            nodeIterators =
                    new ArrayList<Iterator<Map.Entry<Character, TrieNode>>>().listIterator();
            nodeIterators.add(nextLetterMatches(Trie.this.root, 0, pattern));
            findAndUpdateNextWord();
        }

        /**
         * Returns the next children nodes satisfying the given pattern.
         *
         * @param node     the node to get the valid children from
         * @param position the position in the pattern
         * @param pattern  the pattern
         * @return the next children nodes satisfying the given pattern
         */
        private static Iterator<Map.Entry<Character, TrieNode>> nextLetterMatches(final TrieNode node,
                                                                                  final int position,
                                                                                  final String pattern) {
            final Iterator<Map.Entry<Character, TrieNode>> nextLetterMatches;
            if (pattern == null) {
                nextLetterMatches = node.children.entrySet().iterator();
            } else if (position >= pattern.length()) {
                nextLetterMatches = Collections.emptyIterator();
            } else {
                final char patternLetter = pattern.charAt(position);
                if (patternLetter == ANY_CHARACTER_WILDCARD) {
                    nextLetterMatches = node.children.entrySet().iterator();
                } else {
                    final TrieNode exactLetterNode = node.children.get(patternLetter);
                    if (exactLetterNode != null) {
                        nextLetterMatches = Collections.singletonMap(patternLetter,
                                exactLetterNode).entrySet().iterator();
                    } else {
                        nextLetterMatches = Collections.emptyIterator();
                    }
                }
            }
            return nextLetterMatches;
        }

        @Override
        public boolean hasNext() {
            return nextWord != null;
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            final String current = nextWord;
            findAndUpdateNextWord();
            return current;
        }

        /**
         * Looks for next word inside the trie and updates {@link #nextWord}.
         * <p>
         * At the end of the method, {@link #nextWord} contains the next word or {@code null} if
         * no next word was found.
         */
        private void findAndUpdateNextWord() {
            boolean foundWord = descendToNextWord();
            while (!foundWord && ascendToNextPrefix()) {
                foundWord = descendToNextWord();
            }
            nextWord = foundWord ? nextWordBuilder.toString() : null;
        }

        /**
         * Descends the trie down to the next word.
         * <p>
         * After this function returns either:
         * <ul>
         *     <li>Next word is found and is contained in {@link #nextWordBuilder}: This is
         *     denoted by a return value of {@code true}</li>
         *     <li>Next word is not found and {@link #nodeIterators} is in position to ascend the
         *     trie</li>
         * </ul>
         *
         * @return {@code} true if the next word has been found
         */
        private boolean descendToNextWord() {
            boolean foundWord = false;
            while (nodeIterators.hasNext() && !foundWord) {
                final var nodeIt = nodeIterators.next();
                if (nodeIt.hasNext()) {
                    final Map.Entry<Character, TrieNode> nodeEntry = nodeIt.next();
                    final char nodeChar = nodeEntry.getKey();
                    nextWordBuilder.append(nodeChar);
                    final TrieNode node = nodeEntry.getValue();
                    nodeIterators.add(nextLetterMatches(node, nodeIterators.nextIndex(), pattern));
                    nodeIterators.previous();
                    foundWord = isMatchingTerminalNode(node, nextWordBuilder.length());
                } else {
                    nodeIterators.remove();
                }
            }
            return foundWord;
        }

        /**
         * Ascends the trie up to the next valid prefix.
         * <p>
         * After this function returns, either:
         * <ul>
         *     <li>A prefix is found and {@link #nextWordBuilder} and {@link #nodeIterators} are
         *     positioned so that a descent can proceed: This is denoted by a returned value of
         *     {@code true}.</li>
         *     <li>Iteration is finished, no word nor valid prefix was found: This is denoted
         *     by a returned value of {@code false}.</li>
         * </ul>
         *
         * @return {@code true} if the next valid prefix has been found
         */
        private boolean ascendToNextPrefix() {
            boolean foundPrefix = false;
            while (nodeIterators.hasPrevious() && !foundPrefix) {
                final var previousNodeIt = nodeIterators.previous();
                if (previousNodeIt.hasNext()) {
                    foundPrefix = true;
                    nextWordBuilder.delete(nodeIterators.nextIndex(), nextWordBuilder.length());
                } else {
                    nodeIterators.remove();
                }
            }
            return foundPrefix;
        }

        /**
         * Assesses whether given node is a node terminating a word which is matching
         * {@link #pattern}.
         *
         * @param node   the node to test
         * @param length the current word length
         * @return {@code true} if given node is a node terminating a word which is matching
         * {@link #pattern}
         */
        private boolean isMatchingTerminalNode(final TrieNode node, final int length) {
            return node.isTerminal && (pattern == null || pattern.length() == length);
        }
    }

    /** The root node. */
    private final TrieNode root;

    /** The number of elements. */
    private int size;

    /**
     * Constructs an instance from given collection.
     *
     * @param words the words to add to the trie
     */
    Trie(final Collection<String> words) {
        root = new TrieNode();
        for (final String word : words) {
            final boolean added = insertBelow(root, word);
            if (added) {
                size++;
            }
        }
    }

    /**
     * Constructs an empty instance.
     */
    Trie() {
        this(Collections.emptySet());
    }

    /**
     * Inserts the given word in the given root {@link TrieNode}.
     * <p>
     * This method does not update {@link #size}, caller must update it.
     *
     * @param root the root {@link TrieNode}
     * @param word the word to insert
     */
    private static boolean insertBelow(final TrieNode root, final String word) {
        TrieNode current = root;
        for (int i = 0; i < word.length(); i++) {
            final char character = word.charAt(i);
            current = current.children.computeIfAbsent(character, key -> new TrieNode());
        }
        final boolean added = !current.isTerminal;
        current.isTerminal = true;
        return added;
    }

    @Override
    public boolean add(final String word) {
        final boolean added = insertBelow(root, word);
        if (added) {
            size++;
        }
        return added;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    @Override
    public boolean contains(final Object object) {
        final boolean result;
        if (!(object instanceof String word)) {
            result = false;
        } else {
            TrieNode current = root;
            for (int i = 0; i < word.length() && current != null; i++) {
                final char character = word.charAt(i);
                current = current.children.get(character);
            }
            result = current != null && current.isTerminal;
        }
        return result;
    }

    @Override
    public Iterator<String> iterator() {
        return new TrieIterator(null);
    }

    @Override
    public int size() {
        return size;
    }

    /**
     * Similar to {@link #contains(Object)} but the given string is a pattern that may contain
     * wildcards.
     *
     * @param pattern the pattern
     * @return {@code true} iff the given candidate is
     * @see Trie class documentation about patterns
     */
    boolean containsMatching(final String pattern) {
        return new TrieIterator(pattern).hasNext();
    }

    /**
     * Returns a {@link Stream} of String matching the given pattern.
     *
     * @param pattern the pattern to match
     * @return a stream of Strings matching the given pattern
     * @see Trie class documentation about patterns
     */
    Stream<String> streamMatching(final String pattern) {
        final Iterator<String> iterator = new TrieIterator(pattern);
        final Spliterator<String> splitIterator = Spliterators.spliteratorUnknownSize(iterator,
                0 /* TODO specify characteristics? */);
        return StreamSupport.stream(splitIterator, false /* no parallel. */);
    }

}
