/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.model.aff.Aff;
import com.gitlab.super7ramp.croiseur.dictionary.hunspell.codec.parser.common.ParserException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Parses a Hunspell ".aff" file.
 */
public final class AffParser {

    /** Convenience interface. */
    private interface Parser extends BiConsumer<AffBuilder, String> {

        /** A dummy parser for ignored items. */
        Parser DUMMY = (model, event) -> {
        };

        /**
         * Modify the given model according to the given string.
         *
         * @param model the model to modify
         * @param event the string to parse
         */
        default void parse(final AffBuilder model, final String event) {
            accept(model, event);
        }
    }

    /** Parser per kind of item. */
    private static final Map<AffItemKind, Parser> PARSERS;

    /** The maximum number of lines to parse before abandoning the encoding SET option lookup. */
    private static final int ENCODING_OPTION_SEARCH_LIMIT = 50;

    static {
        PARSERS = new EnumMap<>(AffItemKind.class);
        PARSERS.put(AffItemKind.AFFIX_HEADER,
                (builder, line) -> builder.addAffixClassHeader(AffixClassHeaderParser.parse(line)));
        PARSERS.put(AffItemKind.AFFIX_RULE,
                (builder, line) -> builder.addAffixRule(AffixRuleParser.parse(line,
                        builder.flagType())));
        PARSERS.put(AffItemKind.COMPOUNDING_COMPOUNDFLAG,
                (builder, line) -> builder.setCompoundFlag(CompoundFlagParser.COMPOUNDFLAG.parse(line)));
        PARSERS.put(AffItemKind.COMPOUNDING_COMPOUNDBEGIN,
                (builder, line) -> builder.setCompoundBeginFlag(CompoundFlagParser.COMPOUNDBEGIN.parse(line)));
        PARSERS.put(AffItemKind.COMPOUNDING_COMPOUNDMIDDLE,
                (builder, line) -> builder.setCompoundMiddleFlag(CompoundFlagParser.COMPOUNDMIDDLE.parse(line)));
        PARSERS.put(AffItemKind.COMPOUNDING_COMPOUNDEND,
                (builder, line) -> builder.setCompoundEndFlag(CompoundFlagParser.COMPOUNDEND.parse(line)));
        PARSERS.put(AffItemKind.GENERAL_ENCODING,
                (builder, line) -> builder.setEncoding(EncodingParser.parse(line)));
        PARSERS.put(AffItemKind.GENERAL_FLAG_TYPE,
                (builder, line) -> builder.setFlagType(FlagTypeParser.parse(line)));
    }

    /**
     * Constructs an instance.
     */
    public AffParser() {
        // Nothing to do.
    }

    /**
     * Gets the appropriate parser.
     *
     * @param affItemKind item kind
     * @return the parser suited for given item kind
     */
    private static Parser parser(final AffItemKind affItemKind) {
        return PARSERS.computeIfAbsent(affItemKind, kind -> Parser.DUMMY);
    }

    /**
     * Identifies the encoding of the given lines.
     * <p>
     * Method searches the encoding option in the first 50 lines, after which identification
     * attempt stops and ASCII charset is assumed.
     * <p>
     * Typically caller should:
     * <ol>
     *    <li>Open the aff file using its preferred charset</li>
     *    <li>Identify the declared encoding using this method</li>
     *    <li>Reopen the file with the detected encoding and parse the file with
     *    {@link #parse(Iterator)}</li>
     * </ol>
     *
     * @param lines iterator on the lines of the file
     * @return the identified charset
     */
    public static Charset identifyEncoding(final Iterator<String> lines) {
        for (int i = 1; i <= ENCODING_OPTION_SEARCH_LIMIT; i++) {
            final String line = lines.next();
            if (AffItemKind.identify(line)
                           .filter(AffItemKind.GENERAL_ENCODING::equals)
                           .isPresent()) {
                return EncodingParser.parse(line);
            }
        }
        return StandardCharsets.US_ASCII;
    }

    /**
     * Parses an ".aff" file lines.
     *
     * @param lines iterator on the lines of the file
     * @return an {@link Aff}
     * @throws ParserException if parsing goes wrong
     */
    public Aff parse(final Iterator<String> lines) throws ParserException {

        final AffBuilder modelBuilder = new AffBuilder();

        while (lines.hasNext()) {
            final String line = lines.next();
            final AffItemKind kind = AffItemKind.identify(line)
                                                .orElseThrow(() -> new UnknownAffItemException(line));
            parser(kind).parse(modelBuilder, line);
        }

        return modelBuilder.build();
    }
}
