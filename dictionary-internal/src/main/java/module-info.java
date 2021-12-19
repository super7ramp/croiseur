import com.gitlab.super7ramp.crosswords.dictionary.internal.InternalDictionaryProvider;
import com.gitlab.super7ramp.crosswords.dictionary.spi.DictionaryProvider;

/**
 * {@link DictionaryProvider} based on a basic serialized representation.
 */
module com.gitlab.super7ramp.crosswords.dictionary.internal {
    // Base modules
    requires java.logging;

    // Utilities
    requires com.gitlab.super7ramp.crosswords.util;

    // Implemented SPIs.
    requires com.gitlab.super7ramp.crosswords.dictionary.api;
    provides DictionaryProvider with InternalDictionaryProvider;
}