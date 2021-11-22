import com.gitlab.super7ramp.crosswords.dictionary.api.spi.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.HunspellDictionaryProvider;
import com.gitlab.super7ramp.crosswords.dictionary.hunspell.HunspellExtDictionaryProvider;

/**
 * {@link DictionaryProvider}s for Hunspell dictionaries.
 */
module com.gitlab.super7ramp.crosswords.dictionary.hunspell {
    // Base modules
    requires java.logging;

    // Implemented SPIs.
    requires com.gitlab.super7ramp.crosswords.dictionary.api;
    provides DictionaryProvider with HunspellDictionaryProvider, HunspellExtDictionaryProvider;
}