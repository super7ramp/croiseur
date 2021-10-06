package com.gitlab.super7ramp.crosswords.db;

import com.gitlab.super7ramp.crosswords.solver.lib.WordVariable;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Mock for {@link WordDatabase}.
 */
final class WordDatabaseMock implements WordDatabase {

    private static class MatchingWord implements Predicate<String> {

        private final WordVariable wordVariable;

        MatchingWord(WordVariable aWordVariable) {
            wordVariable = aWordVariable;
        }

        @Override
        public boolean test(String word) {
            if (wordVariable.length() != word.length()) {
                return false;
            }

            for (int i = 0; i < wordVariable.length(); i++) {
                final Optional<Character> variableLetter = wordVariable.getLetter(i);
                if (variableLetter.isPresent() && !variableLetter.get().equals(word.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    private final Set<String> words;

    WordDatabaseMock(String... someWords) {
        words = Arrays.stream(someWords).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Set<String> findPossibleValues(final WordVariable wordVariable) {
        final MatchingWord matchingWord = new MatchingWord(wordVariable);
        return words.stream()
                .filter(matchingWord)
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public long countPossibleValues(final WordVariable wordVariable) {
        return findPossibleValues(wordVariable).size();
    }

}
