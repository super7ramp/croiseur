package com.gitlab.super7ramp.crosswords.solver.lib;

import com.gitlab.super7ramp.crosswords.solver.api.Coordinate;
import com.gitlab.super7ramp.crosswords.solver.api.PuzzleDefinition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

final class PuzzleDefinitionParser {

    private static Pattern CHARACTER_SEPARATOR = Pattern.compile("(\\|)");

    private static Character SHADED_CHARACTER = '#';

    /**
     * Constructor.
     */
    private PuzzleDefinitionParser() {
        // Nothing to do.
    }

    /**
     * Parse puzzle from multi-line string such as:
     * <pre>
     *  |H|E|Y|
     *  | | | |
     *  | | |#|
     * </pre>
     *
     * @param puzzle the string representation
     * @return the {@link PuzzleDefinition}
     */
    static PuzzleDefinition parsePuzzle(final String puzzle) {
        final Set<Coordinate> shaded = new HashSet<>();
        final Map<Coordinate, Character> prefilled = new HashMap<>();
        final String[] lines = puzzle.split(System.lineSeparator());
        final int height = lines.length;
        final int width = (int) CHARACTER_SEPARATOR.matcher(lines[0]).results().count() - 1;

        for (int x = 0; x < height; x++) {
            final String[] characters = CHARACTER_SEPARATOR.split(lines[x].substring(1) /* ignore starting "" */);
            for (int y = 0; y < width; y++) {
                final String parsed = characters[y].trim();
                if (!parsed.isEmpty()) {
                    final Character character = parsed.charAt(0);
                    final Coordinate coordinate = new Coordinate(x, y);
                    if (character.equals(SHADED_CHARACTER)) {
                        shaded.add(coordinate);
                    } else {
                        prefilled.put(coordinate, character);
                    }
                } else {
                    // Empty box.
                }
            }
        }

        return new PuzzleDefinition(width, height, shaded, prefilled);
    }
}
