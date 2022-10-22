package com.gitlab.super7ramp.crosswords.solver.ginsberg;

import com.gitlab.super7ramp.crosswords.spi.solver.GridPosition;
import com.gitlab.super7ramp.crosswords.spi.solver.PuzzleDefinition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public final class PuzzleDefinitionParser {

    private static final Pattern CHARACTER_SEPARATOR = Pattern.compile("(\\|)");

    private static final Character SHADED_CHARACTER = '#';

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
    public static PuzzleDefinition parsePuzzle(final String puzzle) {
        final Set<GridPosition> shaded = new HashSet<>();
        final Map<GridPosition, Character> prefilled = new HashMap<>();
        final String[] lines = puzzle.split(System.lineSeparator());
        final int height = lines.length;
        final int width = (int) CHARACTER_SEPARATOR.matcher(lines[0]).results().count() - 1;

        for (int y = 0; y < height; y++) {
            final String[] characters = CHARACTER_SEPARATOR.split(lines[y].substring(1) /* ignore
             starting "" */);
            for (int x = 0; x < width; x++) {
                final String parsed = characters[x].trim();
                if (!parsed.isEmpty()) {
                    final Character character = parsed.charAt(0);
                    final GridPosition gridPosition = new GridPosition(x, y);
                    if (character.equals(SHADED_CHARACTER)) {
                        shaded.add(gridPosition);
                    } else {
                        prefilled.put(gridPosition, character);
                    }
                } else {
                    // Empty box.
                }
            }
        }

        return new PuzzleDefinition(width, height, shaded, prefilled);
    }
}
