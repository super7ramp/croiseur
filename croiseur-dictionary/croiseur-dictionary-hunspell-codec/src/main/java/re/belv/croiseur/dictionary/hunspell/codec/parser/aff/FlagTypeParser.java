/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.aff;

import java.util.EnumMap;
import java.util.Map;
import java.util.regex.Pattern;
import re.belv.croiseur.dictionary.hunspell.codec.parser.common.FlagType;

final class FlagTypeParser {

    /** Identification patterns. */
    private static final Map<FlagType, Pattern> PATTERNS;

    static {
        PATTERNS = new EnumMap<>(FlagType.class);
        // SINGLE ASCII is the default type and cannot be specified explicitly via the FLAG option
        PATTERNS.put(FlagType.LONG_ASCII, Pattern.compile("^FLAG long$"));
        PATTERNS.put(FlagType.NUMERICAL, Pattern.compile("^FLAG num$"));
        PATTERNS.put(FlagType.UTF_8, Pattern.compile("^FLAG UTF-8$"));
    }

    /**
     * Constructor.
     */
    private FlagTypeParser() {
        // Nothing to do.
    }

    /**
     * Parse flag type option.
     *
     * @param line line to parse
     * @return the parsed {@link FlagType}
     */
    static FlagType parse(final String line) {
        return PATTERNS.entrySet().stream()
                .filter(entry -> entry.getValue().matcher(line).matches())
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow(IllegalArgumentException::new);
    }
}
