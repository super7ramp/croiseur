import com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec.plugin.HunspellDictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;

/**
 * {@link DictionaryProvider} for Hunspell dictionaries.
 */
module com.gitlab.super7ramp.crosswords.dictionary.hunspell.plugin {
    requires com.gitlab.super7ramp.crosswords.dictionary.common;
    requires com.gitlab.super7ramp.crosswords.dictionary.hunspell.codec;
    requires com.gitlab.super7ramp.crosswords.spi.dictionary;
    provides DictionaryProvider with HunspellDictionaryProvider;

    requires java.logging;
}