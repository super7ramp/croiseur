import com.gitlab.super7ramp.crosswords.dictionary.txt.plugin.TxtDictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;

/**
 * {@link DictionaryProvider} for simple text file dictionaries.
 */
module com.gitlab.super7ramp.crosswords.dictionary.txt.plugin {
    requires com.gitlab.super7ramp.crosswords.dictionary.common;
    requires transitive com.gitlab.super7ramp.crosswords.spi.dictionary;
    provides DictionaryProvider with TxtDictionaryProvider;
    // No API exposed, only a provider

    requires java.logging;
}