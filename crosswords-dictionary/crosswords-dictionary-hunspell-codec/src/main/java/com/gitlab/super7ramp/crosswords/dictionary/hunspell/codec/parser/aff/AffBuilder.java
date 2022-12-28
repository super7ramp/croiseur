package com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.aff;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.FlagType;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.parser.common.ParserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link Aff} builder.
 */
final class AffBuilder {

    /** Affix builders, access by name of the affix. */
    private final Map<Flag, AffixBuilder> affixBuilders;

    /** Flag type. */
    private FlagType flagType;

    /**
     * Constructor.
     */
    AffBuilder() {
        affixBuilders = new HashMap<>();
        flagType = FlagType.SINGLE_ASCII;
    }

    /**
     * Add an {@link AffixHeader} to this builder
     *
     * @param affixHeader the affix header
     * @return this builder
     */
    AffBuilder addAffixHeader(final AffixHeader affixHeader) {
        affixBuilder(affixHeader.flag()).addHeader(affixHeader);
        return this;
    }

    /**
     * Set the {@link FlagType} to this builder
     *
     * @param aFlagType the flag type
     * @return this builder
     */
    AffBuilder setFlagType(final FlagType aFlagType) {
        flagType = aFlagType;
        return this;
    }

    /**
     * Add an {@link AffixRule} to the model under construction.
     *
     * @param affixRule the affix rule
     * @return this {@link AffBuilder}
     */
    AffBuilder addAffixRule(final AffixRule affixRule) {
        affixBuilder(affixRule.flag()).addRule(affixRule);
        return this;
    }

    /**
     * Build the {@link Aff}
     *
     * @return the {@link Aff} model of the parsed file
     * @throws ParserException if the parsed ".aff" presents a semantic error
     */
    Aff build() throws ParserException {
        final List<Affix> affixes = new ArrayList<>();
        for (final AffixBuilder affixBuilder : affixBuilders.values()) {
            affixes.add(affixBuilder.build());
        }
        return new Aff(flagType, affixes);
    }

    /**
     * Return the {@link AffixBuilder} associated to given affix name; Create it if necessary.
     *
     * @param affixName the affix name
     * @return the associated {@link AffixBuilder}
     */
    private AffixBuilder affixBuilder(final Flag affixName) {
        return affixBuilders.computeIfAbsent(affixName, name -> new AffixBuilder());
    }

}
