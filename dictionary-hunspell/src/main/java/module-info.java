import com.gitlab.super7ramp.crosswords.dictionary.hunspell.HunspellDictionaryProvider;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.HunspellExtDictionaryProvider;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;

/**
 * {@link DictionaryProvider}s for Hunspell dictionaries.
 */
module com.gitlab.super7ramp.crosswords.dictionary.hunspell {
    // Base modules
    requires java.logging;

    // Utilities
    requires com.gitlab.super7ramp.crosswords.util;

    // Implemented SPIs.
    requires com.gitlab.super7ramp.crosswords.dictionary.api;
    provides DictionaryProvider with HunspellDictionaryProvider, HunspellExtDictionaryProvider;

    // Export internals to tools
    exports com.gitlab.super7ramp.crosswords.dictionary.hunspell.pure to
            com.gitlab.super7ramp.crosswords.dictionary.tools;
}