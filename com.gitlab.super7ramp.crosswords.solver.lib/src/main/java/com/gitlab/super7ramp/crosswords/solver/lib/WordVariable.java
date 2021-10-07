package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.grid.VariableIdentifier;
import com.gitlab.super7ramp.crosswords.util.solver.Variable;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents a variable in the crossword problem, i.e. a slot for a word.
 */
public final class WordVariable implements Variable<String> {

    /**
     * Default value.
     */
    private static final char NO_VALUE = 0;
    /**
     * Unique identifier.
     */
    private final VariableIdentifier uid;
    /**
     * Parts of the variable already filled by constraints or assignment.
     */
    private char[] characters;

    /**
     * Constructor for an unassigned variable.
     *
     * @param length length of the variable
     */
    WordVariable(final VariableIdentifier anUid, final int length) {
        uid = anUid;
        characters = new char[length];
    }

    /**
     * Constructor for an assigned variable.
     *
     * @param value the assigned value
     */
    private WordVariable(final VariableIdentifier anUid, final String value) {
        uid = anUid;
        characters = value.toCharArray();
    }

    /**
     * Returns a new {@link WordVariable} with same ID and given assigned value.
     *
     * @param value the assigned value
     * @return the new assigned {@link WordVariable}
     * @throws IllegalArgumentException if value cannot be assigned (i.e. wrong length)
     */
    public WordVariable withValue(final String value) {
        validateAssignment(value);
        return new WordVariable(uid, value);
    }

    /**
     * Returns a new {@link WordVariable} with the same ID and given part.
     *
     * @param part the variable part
     * @param index the index of the part
     * @return the new partially assigned {@link WordVariable}
     * @throws IllegalArgumentException if part cannot be assigned
     */
    public WordVariable withPart(final char part, int index) {
        validateIndex(index);
        final WordVariable result = new WordVariable(uid, characters.length);
        result.setLetter(index, part);
        return result;
    }

    @Override
    public String toString() {
        return "WordVariable{" +
                "characters=" + Arrays.toString(characters) +
                ", uid=" + uid +
                '}';
    }

    /**
     * @return this variable unique {@link VariableIdentifier}
     */
    public VariableIdentifier uid() {
        return uid;
    }

    /**
     * @return the value of the variable, if it has been assigned
     */
    public Optional<String> value() {
        for (final char c : characters) {
            if (c == NO_VALUE) {
                return Optional.empty();
            }
        }
        return Optional.of(String.valueOf(characters));
    }

    /**
     * @param index the index
     * @return the letter at given index, if present, otherwise {@link Optional#empty()}
     */
    public Optional<Character> getLetter(int index) {
        validateIndex(index);
        final char character = characters[index];
        final Optional<Character> result;
        if (character != NO_VALUE) {
            result = Optional.of(character);
        } else {
            result = Optional.empty();
        }
        return result;
    }

    /**
     * @return the length of the variable
     */
    public int length() {
        return characters.length;
    }

    void assign(final String value) {
        validateAssignment(value);
        value.getChars(0, value.length(), characters, 0);
    }

    void unassign(final BacktrackHint hint) {
        removeLetter(hint.indexToUnassign());
    }

    void setLetter(int index, char value) {
        validateIndex(index);
        characters[index] = value;
    }

    private void removeLetter(int index) {
        validateIndex(index);
        characters[index] = NO_VALUE;
    }

    private void validateAssignment(String value) {
        if (characters.length != value.length()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= characters.length) {
            throw new IllegalArgumentException();
        }
    }
}
