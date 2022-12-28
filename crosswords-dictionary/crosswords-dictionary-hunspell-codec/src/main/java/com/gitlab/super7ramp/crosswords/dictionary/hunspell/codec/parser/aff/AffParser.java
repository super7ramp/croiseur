package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.ParserException;

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
        default void parse(AffBuilder model, String event) {
            accept(model, event);
        }
    }

    /** Parser per kind of item. */
    private static final Map<AffItemKind, Parser> PARSERS = parsers();

    /**
     * Constructor.
     */
    public AffParser() {
        // Nothing to do.
    }

    /**
     * Get the appropriate parser.
     *
     * @param affItemKind item kind
     * @return the parser suited for given item kind
     */
    private static Parser parser(final AffItemKind affItemKind) {
        return PARSERS.computeIfAbsent(affItemKind, kind -> Parser.DUMMY);
    }

    private static Map<AffItemKind, Parser> parsers() {
        final EnumMap<AffItemKind, Parser> map = new EnumMap<>(AffItemKind.class);
        map.put(AffItemKind.AFFIX_HEADER,
                (builder, line) -> builder.addAffixHeader(AffixHeader.valueOf(line)));
        map.put(AffItemKind.AFFIX_RULE,
                (builder, line) -> builder.addAffixRule(AffixRule.valueOf(line)));
        map.put(AffItemKind.GENERAL_FLAG_TYPE,
                (builder, line) -> builder.setFlagType(FlagTypeParser.parse(line)));
        return map;
    }

    /**
     * Parse an ".aff" file lines.
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
