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

    /** Pattern for comments that should be ignored: Lines starting with /. */
    private static Pattern COMMENT = Pattern.compile("^/.*$");

    /** The form of an affix flag, as defined in .aff file. */
    private final FlagType flagType;

    /**
     * Constructor.
     */
    public DicParser(FlagType aFlagType) {
        flagType = aFlagType;
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
     * @param lines the iterator on lines
     * @return the {@link Dic} model
     * @throws InvalidDicEntryException if an invalid entry is encountered
     */
    private Dic readEntries(final int estimatedNumberOfEntries, final Iterator<String> lines) throws InvalidDicEntryException {
        final DicBuilder builder = new DicBuilder(estimatedNumberOfEntries);
        while (lines.hasNext()) {
            final String line = lines.next();
            if (!isCommented(line)) {
                builder.add(DicEntry.valueOf(line, flagType));
            } // else ignore comment
        }
        return builder.build();
    }

    /**
     * Reads the estimated number of entries which is supposed to be the first line of the dic file.
     *
     * @param lines the iterator of lines
     * @return the estimated number of entries
     * @throws MissingEstimatedNumberOfEntriesException if the estimated number of entries is
     * missing
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
}
