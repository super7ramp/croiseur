import com.gitlab.super7ramp.crosswords.dictionary.internal.InternalDictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;

/**
 * {@link DictionaryProvider} based on a basic serialized representation.
 */
module com.gitlab.super7ramp.crosswords.dictionary.internal {
    // Base modules
    requires java.logging;

    // Utilities
    requires com.gitlab.super7ramp.crosswords.dictionary.common;

    // dictionary-internal is mainly a dictionary provider for crosswords.
    requires com.gitlab.super7ramp.crosswords.spi.dictionary;
    provides DictionaryProvider with InternalDictionaryProvider;

    // The dictionary-internal API, for other usages.
    exports com.gitlab.super7ramp.crosswords.dictionary.internal;
}