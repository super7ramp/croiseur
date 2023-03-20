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
import java.util.List;
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
 *     <li>"{@value ANY_CHARACTER_WILDCARD}": Any character</li>
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
    private static final class TrieIterator implements Iterator<String> {

        /** The current node iterator stack. Each node is given its own iterator. */
        private final List<Iterator<Map.Entry<Character, TrieNode>>> nodeIterators;

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
         * @param trieArg    the trie to iterate
         * @param patternArg the pattern to match, or {@code null} if no filter
         */
        TrieIterator(final Trie trieArg, final String patternArg) {
            pattern = patternArg;
            nodeIterators = new ArrayList<>();
            nextWordBuilder = new StringBuilder();
            if (!trieArg.isEmpty()) {
                nodeIterators.add(trieArg.root.children.entrySet().iterator());
                findAndUpdateNextWord();
            }
            // current intentionally left null
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

            /*
             * nextWordBuilder is a buffer used for building nextWord. It contains the last found
             *  word, if any.
             *
             * From this basis, the method finds the next word by repeating the following steps:
             *
             * 1. Chop the content of nextWordBuilder to the next valid prefix.
             * 2. When a prefix node is found, traverse the trie from this node until a valid
             * terminal node is found, appending nodes to nextWordBuilder.
             * 3. Repeat if current prefix lead to nowhere until a word is found or no more nodes
             *  left to explore.
             */

            boolean foundWord = false;
            while (!nodeIterators.isEmpty() && !foundWord) {

                // Cleanup terminated iterators, find next prefix, chopping current one
                final var nodeItIt = nodeIterators.listIterator(nodeIterators.size());
                boolean foundPrefix = false;
                while (nodeItIt.hasPrevious() && !foundPrefix) {
                    final var previousNodeIt = nodeItIt.previous();
                    while (previousNodeIt.hasNext() && !foundPrefix) {
                        final Map.Entry<Character, TrieNode> nodeEntry = previousNodeIt.next();
                        final char nodeChar = nodeEntry.getKey();
                        if (patternMatches(nodeChar, nodeItIt.nextIndex())) {
                            // We found the next potential prefix
                            foundPrefix = true;
                            nextWordBuilder.delete(nodeItIt.nextIndex(), nextWordBuilder.length());
                            nextWordBuilder.append(nodeChar);
                            // Add it after current. ListIterator usage is a bit awkward here.
                            nodeItIt.next();
                            nodeItIt.add(nodeEntry.getValue().children.entrySet().iterator());
                            nodeItIt.previous();
                            // Check if this prefix is actually a terminal node
                            if (isMatchingTerminalNode(nodeEntry.getValue(),
                                    nodeItIt.nextIndex())) {
                                foundWord = true;
                            }
                        }
                    }
                    if (!foundPrefix) {
                        nodeItIt.remove();
                    }
                }

                // Append not yet explored letters to the current prefix.
                while (nodeItIt.hasNext() && !foundWord) {
                    final var nodeIt = nodeItIt.next();
                    if (nodeIt.hasNext()) {
                        final Map.Entry<Character, TrieNode> nodeEntry = nodeIt.next();
                        final char nodeChar = nodeEntry.getKey();
                        if (patternMatches(nodeChar, nodeIterators.size() - 1)) {
                            nextWordBuilder.append(nodeChar);
                            nodeItIt.add(nodeEntry.getValue().children.entrySet().iterator());
                            nodeItIt.previous();
                            foundWord = isMatchingTerminalNode(nodeEntry.getValue(),
                                    nextWordBuilder.length());
                        }
                    }
                }
            }

            // Finally update next word
            nextWord = foundWord ? nextWordBuilder.toString() : null;
        }

        /**
         * Assesses whether given char matches {@link #pattern}, if any, at given position.
         *
         * @param actual   the char to test
         * @param position the char position
         * @return {@code true} if given char matches pattern at given position or if pattern is
         * {@code null}
         */
        private boolean patternMatches(final char actual, final int position) {
            return pattern == null || (position >= 0 && position < pattern.length() && (pattern.charAt(position) == actual || pattern.charAt(position) == ANY_CHARACTER_WILDCARD));
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

    /** The any-character wildcard. */
    private static final char ANY_CHARACTER_WILDCARD = ' ';

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
        return new TrieIterator(this, null);
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
        return new TrieIterator(this, pattern).hasNext();
    }

    /**
     * Returns a {@link Stream} of String matching the given pattern.
     *
     * @param pattern the pattern to match
     * @return a stream of Strings matching the given pattern
     * @see Trie class documentation about patterns
     */
    Stream<String> streamMatching(final String pattern) {
        final Iterator<String> iterator = new TrieIterator(this, pattern);
        final Spliterator<String> splitIterator = Spliterators.spliteratorUnknownSize(iterator,
                0 /* TODO specify characteristics? */);
        return StreamSupport.stream(splitIterator, false /* no parallel. */);
    }

}
