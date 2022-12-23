package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure.HunspellDictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

/**
 * Hunspell dictionary provider.
 */
public final class HunspellDictionaryProvider implements DictionaryProvider {

    /**
     * Constructor.
     */
    public HunspellDictionaryProvider() {
        // Nothing to do.
    }

    @Override
    public DictionaryProviderDescription description() {
        return new DictionaryProviderDescription("hunspell", "Hunspell dictionary backend");
    }

    @Override
    public Collection<Dictionary> get() {
        return DefaultDictionaries.get().stream().map(HunspellDictionary::new).collect(toList());
    }

}
