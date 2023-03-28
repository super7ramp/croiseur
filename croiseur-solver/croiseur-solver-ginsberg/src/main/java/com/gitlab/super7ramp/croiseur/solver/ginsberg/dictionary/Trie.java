/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.solver.ginsberg.dictionary;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * An implementation of the trie data structure.
 * <p>
 * All methods expect non-null arguments.
 *
 * <h2>Patterns</h2>
 * Some methods ({@link #containsMatching(String)}, {@link #removeNonMatching(String)},
 * {@link #streamMatching(String)})
 * support a simplistic form of pattern matching. The supported wildcards for these methods are:
 * <ul>
 *     <li>"{@value PatternMatcher#ANY_CHARACTER_WILDCARD}": Any character</li>
 * </ul>
 *
 * <h2>Thread safety</h2>
 * This collection is not thread-safe.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Trie">Trie on Wikipedia</a>
 */
final class Trie extends AbstractSet<String> {

    /**
     * A pattern matcher.
     * <p>
     * Used in {@link TrieIterator} to efficiently filter a trie, calling
     * {@link #nextLetterMatches(TrieNode, int)} when descending the trie.
     */
    private interface PatternMatcher {

        /** The any-character wildcard. */
        char ANY_CHARACTER_WILDCARD = ' ';

        /**
         * Returns the next children nodes satisfying the pattern.
         *
         * @param node     the node to get the valid children from
         * @param position the position in the pattern
         * @return the next children nodes satisfying the given pattern
         */
        Iterator<Map.Entry<Character, TrieNode>> nextLetterMatches(final TrieNode node,
                                                                   final int position);

        /**
         * Whether the given word matches with the pattern.
         * <p>
         * This is called when evaluating a terminal node, meaning that all letters come from an
         * iterator returned by a previous call to {@link #nextLetterMatches(TrieNode, int)}).
         *
         * @param word the word
         * @return {@code true} iff the given word length matches with the pattern length
         */
        boolean matches(final CharSequence word);
    }

    /** A dummy {@link PatternMatcher} that will match any word. */
    private enum NoPatternMatcher implements PatternMatcher {
        INSTANCE;

        @Override
        public Iterator<Map.Entry<Character, TrieNode>> nextLetterMatches(final TrieNode node,
                                                                          final int position) {
            return node.children.entrySet().iterator();
        }

        @Override
        public boolean matches(final CharSequence word) {
            return true;
        }
    }

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
     * The standard {@link PatternMatcher}.
     * <p>
     * For a pattern of letters (or wildcards of length 1) {@literal p_i}, a word positively
     * matches if and only if it has same size n and all its letters {@literal x_j} matches, i.e.
     * {@literal x_1 ~ p_1 && x_2 ~ p_2 && ... && x_n ~ p_n}.
     * <p>
     * This can be verified sequentially, i.e. branches can be filtered out as soon as a letter
     * does not match the pattern. It means that when {@link #matches(CharSequence)} is called
     * then all letters matches the pattern - only the word length needs to be verified against
     * pattern length.
     */
    private static final class PositivePatternMatcher implements PatternMatcher {

        /** The pattern to match. */
        private final String pattern;

        /**
         * Constructs an instance.
         *
         * @param patternArg the pattern to match
         */
        PositivePatternMatcher(final String patternArg) {
            pattern = Objects.requireNonNull(patternArg);
        }

        @Override
        public Iterator<Map.Entry<Character, TrieNode>> nextLetterMatches(final TrieNode node,
                                                                          final int position) {
            final Iterator<Map.Entry<Character, TrieNode>> nextLetterMatches;
            if (position >= pattern.length()) {
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
        public boolean matches(final CharSequence word) {
            return pattern.length() == word.length();
        }
    }

    /**
     * A {@link PatternMatcher} that matches words <em>not</em> respecting a given pattern.
     * <p>
     * For a pattern of letters (or wildcards of length 1) {@literal p_i}, a word
     * negatively matches if and only if it has different size, or it has same size n and all its
     * letters {@literal x_j} matches, i.e. {@literal x_1 !~ p_1 && x_2 !~ p_2 && ... && x_n !~
     * p_n}.
     * <p>
     * It is less natural than a positive matcher because a word may match the pattern thanks
     * only to its first letter, but it can only be definitive when checking the final word
     * {@link #matches(CharSequence)}. This means matcher has to maintain some kind of memory of
     * the previous matches or perform the entire check in the {@link #matches(CharSequence)}
     * method. Implementation performs the latter, which may not be the most efficient, but it
     * certainly is simpler than making the class stateful.
     */
    private static final class NegativePatternMatcher implements PatternMatcher {

        /** The pattern <em>not</em> to match. */
        private final String pattern;

        /**
         * Constructs an instance.
         *
         * @param patternArg the pattern <em>not</em> to match
         */
        NegativePatternMatcher(final String patternArg) {
            pattern = Objects.requireNonNull(patternArg);
        }

        @Override
        public Iterator<Map.Entry<Character, TrieNode>> nextLetterMatches(final TrieNode node,
                                                                          final int position) {
            // All children may match
            return node.children.entrySet().iterator();
        }

        @Override
        public boolean matches(final CharSequence word) {
            if (pattern.length() != word.length()) {
                return true;
            }
            for (int i = 0; i < pattern.length(); i++) {
                final char pi = pattern.charAt(i);
                if (pi != ANY_CHARACTER_WILDCARD && pi != word.charAt(i)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * A trie {@link Iterator} implementation. Optionally filters words according to a
     * {@link PatternMatcher} passed at construction time.
     */
    private final class TrieIterator implements Iterator<String> {

        /** The current node iterator stack. Each node is given its own iterator. */
        private final ListIterator<Iterator<Map.Entry<Character, TrieNode>>> nodeIterators;

        /** The pattern matcher. */
        private final PatternMatcher patternMatcher;

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

        /** The next node or {@code null} if iterator has no next element. */
        private TrieNode next;

        /** The current node, or {@code null} if {@link #next()} hasn't been called yet. */
        private TrieNode current;

        /**
         * Constructs an instance iterating on all words contained in the enclosing trie matching
         * the given pattern.
         *
         * @param patternMatcherArg the pattern matcher
         */
        TrieIterator(final PatternMatcher patternMatcherArg) {
            patternMatcher = patternMatcherArg;
            nextWordBuilder = new StringBuilder();
            nodeIterators =
                    new ArrayList<Iterator<Map.Entry<Character, TrieNode>>>().listIterator();
            nodeIterators.add(patternMatcher.nextLetterMatches(Trie.this.root, 0));
            findAndUpdateNextWord();
        }

        /**
         * Constructs an instance iterating on all words contained in the enclosing trie.
         */
        TrieIterator() {
            this(NoPatternMatcher.INSTANCE);
        }


        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }
            /*
             * Lazy removal: Do not actually remove node from trie. Not great, leaves useless
             * nodes in the trie which slows down iteration.
             */
            current.isTerminal = false;
            Trie.this.size--;
            current = null;
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
            current = next;
            final String currentWord = nextWord;
            findAndUpdateNextWord();
            return currentWord;
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
                    nodeIterators.add(patternMatcher.nextLetterMatches(node,
                            nodeIterators.nextIndex()));
                    nodeIterators.previous();
                    foundWord = isMatchingTerminalNode(node);
                    if (foundWord) {
                        next = node;
                    }
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
         * Assesses whether given node is a node terminating a word which is matching the pattern.
         *
         * @param node the node to test
         * @return {@code true} if given node is a node terminating a word which is matching the
         * pattern
         */
        private boolean isMatchingTerminalNode(final TrieNode node) {
            return node.isTerminal && patternMatcher.matches(nextWordBuilder);
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
    public boolean remove(final Object o) {
        final boolean removed;
        if (!(o instanceof String word)) {
            removed = false;
        } else {
            final Iterator<String> it = new TrieIterator(new PositivePatternMatcher(word));
            if (it.hasNext()) {
                it.next();
                it.remove();
                removed = true;
            } else {
                removed = false;
            }
        }
        return removed;
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
        return new TrieIterator();
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
        return new TrieIterator(new PositivePatternMatcher(pattern)).hasNext();
    }

    /**
     * Similar to {@link #remove(Object)} but the given string is a pattern removed elements
     * do not match.
     *
     * @param pattern the pattern the removed elements do not match
     * @return {@code true} if a word has been removed
     * @see Trie class documentation about patterns
     */
    boolean removeNonMatching(final String pattern) {
        final TrieIterator it = new TrieIterator(new NegativePatternMatcher(pattern));
        boolean removed = false;
        while (it.hasNext()) {
            it.next();
            it.remove();
            removed = true;
        }
        return removed;
    }

    /**
     * Returns a {@link Stream} of Strings contained in this trie and matching the given pattern.
     *
     * @param pattern the pattern to match
     * @return a stream of Strings matching the given pattern
     * @see Trie class documentation about patterns
     */
    Stream<String> streamMatching(final String pattern) {
        final Iterator<String> iterator = new TrieIterator(new PositivePatternMatcher(pattern));
        final Spliterator<String> splitIterator = Spliterators.spliteratorUnknownSize(iterator,
                0 /* TODO specify characteristics? */);
        return StreamSupport.stream(splitIterator, false /* no parallel. */);
    }

}
