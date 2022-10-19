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

    // Implemented SPIs.
    requires com.gitlab.super7ramp.crosswords.spi.dictionary;
    provides DictionaryProvider with InternalDictionaryProvider;

    // Export internals to tools
    exports com.gitlab.super7ramp.crosswords.dictionary.internal to
            com.gitlab.super7ramp.crosswords.dictionary.tools;
}