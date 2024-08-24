/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.puzzle.codec.xd.reader;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import re.belv.croiseur.puzzle.codec.xd.model.XdMetadata;

/** Parses text to {@link XdMetadata}. */
final class XdMetadataReader {

    /** Standard metadata property keys. */
    private static final class StandardKeys {
        private static final String TITLE = "Title";
        private static final String AUTHOR = "Author";
        private static final String EDITOR = "Editor";
        private static final String COPYRIGHT = "Copyright";
        private static final String DATE = "Date";
    }

    /**
     * Line separator regex. Handles both Unix and Windows line-endings (although format specification seems to assume
     * Unix line-endings).
     */
    private static final String LINE_SEPARATOR = "\\R";

    /** The separator between key and value ("Key: Value"). */
    private static final String PROPERTY_SEPARATOR = ":";

    /** The metadata model builder. */
    private final XdMetadata.Builder builder;

    /** Constructs an instance. */
    XdMetadataReader() {
        builder = new XdMetadata.Builder();
    }

    /**
     * Reads the given metadata.
     *
     * @param rawMetadata the metadata to read
     * @return the read {@link XdMetadata}
     * @throws XdReadException if read fails
     * @throws NullPointerException if given string is {@code null}
     */
    XdMetadata read(final String rawMetadata) throws XdReadException {
        Objects.requireNonNull(rawMetadata);
        builder.reset();
        for (final String line : rawMetadata.split(LINE_SEPARATOR)) {
            final String[] keyValue = splitKeyValues(line);
            final String key = keyValue[0].trim();
            final String value = keyValue[1].trim();
            switch (key) {
                case StandardKeys.TITLE -> builder.title(value);
                case StandardKeys.AUTHOR -> builder.author(value);
                case StandardKeys.EDITOR -> builder.editor(value);
                case StandardKeys.COPYRIGHT -> builder.copyright(value);
                case StandardKeys.DATE -> builder.date(parseDate(value));
                default -> builder.otherProperty(key, value);
            }
        }
        return builder.build();
    }

    /**
     * Split metadata line between key and value parts.
     *
     * @param line the line
     * @return an array of size 2, whose first element is the property key and its second element is the property value
     * @throws XdMetadataReadException if line is invalid (i.e. not respecting the format "Key: Value")
     */
    private static String[] splitKeyValues(final String line) throws XdMetadataReadException {
        if (line.isEmpty()) {
            throw new XdMetadataReadException("Blank line");
        }
        final String[] keyValue = line.split(PROPERTY_SEPARATOR);
        if (keyValue.length != 2) {
            throw new XdMetadataReadException(
                    "Invalid property '" + line + "'. Property must respect the format 'Key: Value'.");
        }
        return keyValue;
    }

    /**
     * Parses the given date.
     *
     * <p>The expected format is "YYYY-MM-DD".
     *
     * @param date the date to parse
     * @return the parsed {@link LocalDate}
     * @throws XdMetadataReadException if given string does not respect the expect format
     */
    private static LocalDate parseDate(final String date) throws XdMetadataReadException {
        try {
            return LocalDate.parse(date);
        } catch (final DateTimeParseException e) {
            throw new XdMetadataReadException(e);
        }
    }
}
