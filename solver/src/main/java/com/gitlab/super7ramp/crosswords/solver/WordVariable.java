package main.java.com.gitlab.super7ramp.crosswords.solver;

import main.java.com.gitlab.super7ramp.crosswords.grid.VariableIdentifier;

import java.util.Arrays;
import java.util.Optional;

/**
 * Represents a variable in the crossword problem, i.e. a slot for a word.
 *
 * Implementation:
 * <ul>
 *     <li>stores letters propagated by constraints.
 *     <li>is immutable
 * </ul>
 *
 */
public final class WordVariable {

    /** Default value. */
    private static final char NO_VALUE = 0;

    /** Parts of the variable already filled by constraints or assignment. */
    private final char[] characters;

    /** Unique identifier. */
    private final VariableIdentifier uid;

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
        result.addLetter(index, part);
        return result;
    }

    /**
     * @return this variable unique {@link VariableIdentifier}
     */
    public VariableIdentifier uid() {
        return uid;
    }

    private void validateAssignment(String value) {
        if(characters.length != value.length()) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return the value of the variable, if it has been assigned
     */
    public Optional<String> value() {
        return Optional.of(String.valueOf(characters));
    }

    /**
     * @return the length of the variable
     */
    public int length() {
        return characters.length;
    }

    private void addLetter(int index, char value) {
        validateIndex(index);
        characters[index] = value;
    }

    private Optional<Character> getLetter(int index) {
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

    private void removeLetter(int index) {
        validateIndex(index);
        characters[index] = NO_VALUE;
    }

    private void validateIndex(int index) {
        if (index < 0 || index >= characters.length) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return "WordVariable{" +
                "characters=" + Arrays.toString(characters) +
                ", uid=" + uid +
                '}';
    }
}
