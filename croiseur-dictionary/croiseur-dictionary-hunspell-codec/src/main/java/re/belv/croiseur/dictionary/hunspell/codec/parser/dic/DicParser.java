/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.dic;

import re.belv.croiseur.dictionary.hunspell.codec.model.dic.Dic;
import re.belv.croiseur.dictionary.hunspell.codec.model.dic.DicEntry;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.FlagType;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.ParserException;

import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Parses a Hunspell ".dic" file.
 */
public final class DicParser {

    /** Pattern for the number of entries at the start of the "*.dic" file. */
    private static final Pattern NUMBER_OF_ENTRIES = Pattern.compile("^[0-9]+$");

    /** Pattern for comments that should be ignored: Lines starting with / or #. */
    private static final Pattern COMMENT = Pattern.compile("^[/#].*$");

    /** The form of an affix flag, as defined in .aff file. */
    private final FlagType flagType;

    /**
     * Constructs an instance.
     *
     * @param aFlagType the flag type
     */
    public DicParser(final FlagType aFlagType) {
        flagType = aFlagType;
    }

    /**
     * Reads the estimated number of entries which is supposed to be the first line of the dic file.
     *
     * @param lines the iterator of lines
     * @return the estimated number of entries
     * @throws MissingEstimatedNumberOfEntriesException if the estimated number of entries is
     *                                                  missing
     */
    private static int readEstimatedNumberOfEntries(final Iterator<String> lines) throws MissingEstimatedNumberOfEntriesException {
        if (!lines.hasNext()) {
            throw new MissingEstimatedNumberOfEntriesException();
        }
        final String firstLine = lines.next();
        if (!NUMBER_OF_ENTRIES.matcher(firstLine).matches()) {
            throw new MissingEstimatedNumberOfEntriesException(firstLine);
        }
        return Integer.parseInt(firstLine);
    }

    /**
     * Returns {@code true} iff line looks like a comment.
     *
     * @param line the line to assess
     * @return {@code true} iff line looks like a comment
     */
    private static boolean isCommented(final String line) {
        return COMMENT.matcher(line).matches();
    }

    /**
     * Parse a ".dic" file lines.
     *
     * @param lines iterator on the lines of the file
     * @return a {@link Dic}
     * @throws ParserException if parsing goes wrong
     */
    public Dic parse(final Iterator<String> lines) throws ParserException {
        final int estimatedNumberOfEntries = readEstimatedNumberOfEntries(lines);
        return readEntries(estimatedNumberOfEntries, lines);
    }

    /**
     * Reads the dic file entries.
     *
     * @param estimatedNumberOfEntries the estimated number of entries
     * @param lines                    the iterator on lines
     * @return the {@link Dic} model
     * @throws InvalidDicEntryException if an invalid entry is encountered
     */
    private Dic readEntries(final int estimatedNumberOfEntries, final Iterator<String> lines) throws InvalidDicEntryException {
        final DicBuilder builder = new DicBuilder(estimatedNumberOfEntries);
        while (lines.hasNext()) {
            final String line = lines.next();
            if (!isCommented(line) && !line.isBlank()) {
                final DicEntry dicEntry = DicEntryParser.parse(line, flagType);
                builder.add(dicEntry);
            } // else ignore comment or blank lines
        }
        return builder.build();
    }
}
