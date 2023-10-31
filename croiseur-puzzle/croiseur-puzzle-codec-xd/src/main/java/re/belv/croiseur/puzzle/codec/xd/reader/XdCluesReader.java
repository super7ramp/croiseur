/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.reader;

import re.belv.croiseur.puzzle.codec.xd.model.XdClues;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses text to {@link XdClues}.
 */
final class XdCluesReader {

    /** Regex matching a clue. */
    private static final Pattern CLUE_REGEX =
            Pattern.compile("(?<orientation>[AD])(?<number>\\d+)\\. (?<clue>.*) ~ (?<answer>.+)");

    /** The clues model builder. */
    private final XdClues.Builder builder;

    /**
     * Constructs an instance.
     */
    XdCluesReader() {
        builder = new XdClues.Builder();
    }

    /**
     * Reads the given clues.
     *
     * @param rawClues the clues to read
     * @return the parsed clues
     * @throws NullPointerException if given string is {@code null}
     */
    XdClues read(final String rawClues) throws XdClueReadException {
        Objects.requireNonNull(rawClues);
        builder.reset();
        final String[] clues = rawClues.split("\\R");
        boolean parsingAcrossClues = true;
        for (final String clue : clues) {
            if (parsingAcrossClues && clue.isEmpty()) {
                // A new line separate across clues from down clues
                parsingAcrossClues = false;
                continue;
            }
            final Matcher m = CLUE_REGEX.matcher(clue);
            if (!m.matches()) {
                throw new XdClueReadException("Invalid clue: '" + clue +
                                              "'. Expected format is: " + CLUE_REGEX.pattern() +
                                              "'.");
            }
            final boolean isAcross = m.group("orientation").equals("A");
            if (parsingAcrossClues && !isAcross) {
                throw new XdClueReadException("Invalid clue: '" + clue +
                                              "'. Expected across clue but was down clue.");
            }
            if (!parsingAcrossClues && isAcross) {
                throw new XdClueReadException("Invalid clue: '" + clue +
                                              "'. Expected down clue but was across clue.");
            }
            final int number = Integer.parseInt(m.group("number"));
            final String actualClue = m.group("clue");
            final String answer = m.group("answer");
            if (isAcross) {
                builder.across(number, actualClue, answer);
            } else {
                builder.down(number, actualClue, answer);
            }
        }
        return builder.build();
    }
}
