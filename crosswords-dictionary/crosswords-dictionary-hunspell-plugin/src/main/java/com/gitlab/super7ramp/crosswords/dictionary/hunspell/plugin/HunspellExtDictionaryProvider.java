package com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin;

import com.gitlab.super7ramp.crosswords.common.dictionary.DictionaryProviderDescription;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.external.HunspellDictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.Dictionary;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

/**
 * Alternative Hunspell {@link DictionaryProvider} relying on deprecated "unmunch" utility of
 * Hunspell, called as an
 * external process.
 */
public final class HunspellExtDictionaryProvider implements DictionaryProvider {

    /**
     * Constructor.
     */
    public HunspellExtDictionaryProvider() {
        // Nothing to do.
    }

    @Override
    public DictionaryProviderDescription description() {
        return new DictionaryProviderDescription("hunspell-ext", "Alternative Hunspell dictionary" +
                " backend (relies on external utilities)");
    }

    @Override
    public Collection<Dictionary> get() {
        return DefaultDictionaries.get().stream().map(HunspellDictionary::new).collect(toList());
    }
}
