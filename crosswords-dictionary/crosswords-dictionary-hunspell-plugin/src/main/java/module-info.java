import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.HunspellDictionaryProvider;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.HunspellExtDictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;

/**
 * {@link DictionaryProvider}s for Hunspell dictionaries.
 */
module com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin {
    // Base modules
    requires java.logging;

    // Utilities
    requires com.gitlab.super7ramp.crosswords.dictionary.common;

    // dictionary-hunspell is mainly a dictionary provider for crosswords.
    requires com.gitlab.super7ramp.crosswords.spi.dictionary;
    provides DictionaryProvider with HunspellDictionaryProvider, HunspellExtDictionaryProvider;

    // The dictionary-hunspell API, for other usages.
    exports com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.pure;
    exports com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin.external;
}