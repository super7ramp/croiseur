/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.aff;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Parses the SET option. */
final class EncodingParser {

    /** The SET option pattern. */
    private static final Pattern PATTERN = Pattern.compile("^SET (UTF-8|ISO-?8859-1|ISO-?8859-10"
            + "|ISO-?8859-13|ISO-?8859-15|KOI8-R|KOI8-U|microsoft-cp1251|ISCII-DEVANAGARI)$");

    /** Private constructor to prevent instantiation. */
    private EncodingParser() {
        // Nothing to do.
    }

    /**
     * Parses the given SET option.
     *
     * @param line the line containing a SET option
     * @return the parsed charset
     */
    static Charset parse(final String line) {
        final Matcher matcher = PATTERN.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not a SET encoding option");
        }
        final String encoding = standardize(matcher.group(1));
        return Charset.forName(encoding);
    }

    /**
     * Standardizes the given encoding name so that it is recognized by the JVM.
     *
     * @param name the Hunspell encoding name
     * @return an encoding name that should be recognizable by the JVM
     */
    private static String standardize(final String name) {
        return switch (name) {
            case "microsoft-cp1251" -> "cp1251";
            case "ISCII-DEVANAGARI" -> "ISCII";
            default -> name;
        };
    }
}
