package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.dic;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.FlagType;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.parser.common.ParserException;

import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Parses a Hunspell ".dic" file.
 */
public final class DicParser {

    /** Pattern for the number of entries at the start of the "*.dic" file. */
    private static Pattern NUMBER_OF_ENTRIES = Pattern.compile("^[0-9]+$");

    /** The form of an affix flag, as defined in .aff file. */
    private final FlagType flagType;

    /**
     * Constructor.
     */
    public DicParser(FlagType aFlagType) {
        flagType = aFlagType;
    }

    private static int readNumberOfEntries(final Iterator<String> lines) throws MissingEstimatedNumberOfEntriesException {
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
     * Parse a ".dic" file lines.
     *
     * @param lines iterator on the lines of the file
     * @return a {@link Dic}
     * @throws ParserException if parsing goes wrong
     */
    public Dic parse(final Iterator<String> lines) throws ParserException {
        final int numberOfEntries = readNumberOfEntries(lines);
        return readEntries(numberOfEntries, lines);
    }

    private Dic readEntries(final int numberOfEntries, final Iterator<String> lines) throws InvalidDicEntryException {
        final DicBuilder builder = new DicBuilder(numberOfEntries);
        while (lines.hasNext()) {
            builder.add(DicEntry.valueOf(lines.next(), flagType));
        }
        return builder.build();
    }
}
