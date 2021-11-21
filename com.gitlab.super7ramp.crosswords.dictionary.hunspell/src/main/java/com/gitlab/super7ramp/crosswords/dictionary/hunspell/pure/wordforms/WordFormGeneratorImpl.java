package com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.wordforms;

import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.aff.Aff;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.aff.Affix;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.aff.AffixRule;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.common.Flag;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.dic.Dic;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure.parser.dic.DicEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Pure Java implementation of {@link WordFormGenerator}.
 * <p>
 * Note: This implements a minimal amount of Hunspell options.
 */
final class WordFormGeneratorImpl implements WordFormGenerator {

    /** The parsed dictionary file. */
    private final Dic dic;

    /** Affixes indexed by name to avoid looping the affix list all the time. */
    private final Map<Flag, Affix> affixes;

    /** Affix applicators indexed by affix rule to avoid re-creating the same applicator over and over. */
    private final Map<AffixRule, AffixApplicator> affixApplicators;

    /**
     * Constructor.
     *
     * @param anAff the parsed affix file
     * @param aDic  the parsed dictionary file
     */
    WordFormGeneratorImpl(final Aff anAff, final Dic aDic) {
        affixes = new HashMap<>();
        for (final Affix affix : anAff.affixes()) {
            affixes.put(affix.header().flag(), affix);
        }
        affixApplicators = new HashMap<>();
        dic = aDic;
    }

    @Override
    public Stream<String> generate() {
        return dic.entries().stream()
                .mapMulti(applyAffixes().andThen((entry, consumer) -> consumer.accept(entry.word())));
    }

    private BiConsumer<DicEntry, Consumer<String>> affixForms() {
        return (dicEntry, stringConsumer) -> deaffix(dicEntry, stringConsumer);
    }

    private void deaffix(final DicEntry dicEntry, final Consumer<String> stringConsumer) {
        // TODO
    }

    private BiConsumer<DicEntry, Consumer<String>> applyAffixes() {
        return this::applyAffixes;
    }

    private void applyAffixes(final DicEntry entry, final Consumer<String> accumulator) {
        final String baseWord = entry.word();

        for (final Flag flag : entry.flags()) {
            final Affix affix = affixes.get(flag);
            if (affix != null) {
                for (final AffixRule affixRule : affix.rules()) {

                    final Optional<String> optStemWithAffix = applyAffix(affixRule, baseWord);

                    if (optStemWithAffix.isPresent()) {
                        final String stemWithAffix = optStemWithAffix.get();
                        accumulator.accept(stemWithAffix);

                        if (affix.header().crossProduct()) {
                            entry.flags().stream()
                                    .map(affixes::get)
                                    .mapMulti((BiConsumer<Affix, Consumer<AffixRule>>) (crossProductAffix, crossProductRules) -> {
                                        if (crossProductAffix != null) {
                                            crossProductAffix.rules().forEach(crossProductRules::accept);
                                        } else {
                                            // flag refers to another option than PFX/SFX
                                        }
                                    })
                                    .filter(rule -> rule.kind() != affixRule.kind())
                                    .map(rule -> applyAffix(rule, stemWithAffix))
                                    .filter(Optional::isPresent)
                                    .limit(2)
                                    .forEach(result -> accumulator.accept(result.get()));
                        }
                    }
                }
            }
        }
    }

    private Optional<String> applyAffix(final AffixRule affixRule, final String baseWord) {
        return affixApplicators.computeIfAbsent(affixRule, rule -> AffixApplicators.ofRule(affixRule)).apply(baseWord);
    }

}
