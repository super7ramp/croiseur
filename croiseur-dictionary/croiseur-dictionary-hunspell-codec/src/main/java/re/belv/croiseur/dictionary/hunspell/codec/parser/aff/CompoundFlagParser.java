/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.dictionary.hunspell.codec.parser.aff;

import re.belv.croiseur.dictionary.hunspell.codec.model.common.Flag;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Compound flag parser.
 */
enum CompoundFlagParser {

    /** Parser for {@code COMPOUNDFLAG}. */
    COMPOUNDFLAG("FLAG"),
    /** Parser for {@code COMPOUNDBEGIN}. */
    COMPOUNDBEGIN("BEGIN"),
    /** Parser for {@code COMPOUNDMIDDLE}. */
    COMPOUNDMIDDLE("MIDDLE"),
    /** Parser for {@code COMPOUNDEND}. */
    COMPOUNDEND("END");

    /** The textual representation. */
    private final Pattern pattern;

    /**
     * Constructs an instance.
     *
     * @param nameSuffix the suffix to add to "COMPOUND" to have the full option name
     */
    CompoundFlagParser(final String nameSuffix) {
        pattern = Pattern.compile("^COMPOUND" + nameSuffix + " +(?<identifier>[^ ])+$");
    }

    /**
     * Parses a compound flag.
     *
     * @param line the line to parse
     * @return the {@link CompoundFlagParser}
     */
    Flag parse(final String line) {
        final Matcher matcher = pattern.matcher(line);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Not a compound flag: " + line);
        }
        final String identifier = matcher.group("identifier");
        return new Flag(identifier);
    }
}
